package pl.edu.agh.ed.utils.runners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultAnalyzer {
	private static final Path INPUT_DIR_PATH = Paths.get("output");

	private static final Path OUTPUT_DIR_PATH = Paths.get("average");

	public static void main(String[] args) throws IOException {
		List<Path> resultsDir = new ArrayList<>();
		for (Path typeDir : Files.newDirectoryStream(INPUT_DIR_PATH)) {
			for (Path datasetDir : Files.newDirectoryStream(typeDir)) {
				for (Path expDir : Files.newDirectoryStream(datasetDir)) {
					resultsDir.add(expDir);
				}
			}
		}
		List<String> globalTime = new ArrayList<>();
		List<String> globalSize = new ArrayList<>();
		List<String> globalMem = new ArrayList<>();
		for (Path expDir : resultsDir) {
			Map<Integer, Map<Integer, Double>> averages = new HashMap<>();
			String header = "";
			int number = 0;
			for (Path runResultPath : Files.newDirectoryStream(expDir, "run-*")) {
				number++;
				List<String> lines = Files.readAllLines(runResultPath);
				header = lines.get(0);
				for (int i = 1; i < lines.size(); i++) {
					if (!averages.containsKey(i)) {
						averages.put(i, new HashMap<>());
					}
					String row = lines.get(i);
					List<Double> values = Arrays.stream(row.split(";")).map(Double::parseDouble)
							.collect(Collectors.toList());
					for (int valN = 0; valN < values.size(); valN++) {
						if (!averages.get(i).containsKey(valN)) {
							averages.get(i).put(valN, 0.0);
						}
						averages.get(i).put(valN, averages.get(i).get(valN) + values.get(valN));
					}
				}
			}
			Path averagePath = OUTPUT_DIR_PATH
					.resolve(expDir.toString().replace("output\\", "").replace("\\", "_") + ".txt");
			List<String> lines = new ArrayList<>();
			header = header.replace(";", "\t");
			lines.add(expDir.toString().replace("output\\", "").replace("\\", " "));
			lines.add(header);
			int numberF = number;
			for (Map<Integer, Double> line : averages.values()) {
				lines.add(line.values().stream().mapToDouble(d -> d).map(d -> d / numberF).mapToObj(Double::toString)
						.map(s -> s.replace(".", ",")).reduce((s1, s2) -> s1 + "\t" + s2).get());
			}
			if (lines.get(0).contains("time")) {
				globalTime.addAll(lines);
				globalTime.add("");
				globalTime.add("");
				globalTime.add("");
				globalTime.add("");
			} else if (lines.get(0).contains("memory")) {
				globalMem.addAll(lines);
				globalMem.add("");
				globalMem.add("");
				globalMem.add("");
				globalMem.add("");
			} else {
				globalSize.addAll(lines);
				globalSize.add("");
				globalSize.add("");
				globalSize.add("");
				globalSize.add("");
			}
			Files.write(averagePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		}
		Files.write(Paths.get("size.txt"), globalSize, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		Files.write(Paths.get("mem.txt"), globalMem, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		Files.write(Paths.get("time.txt"), globalTime, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}

}
