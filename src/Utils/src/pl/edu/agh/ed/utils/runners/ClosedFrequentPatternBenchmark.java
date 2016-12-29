package pl.edu.agh.ed.utils.runners;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ca.pfv.spmf.tools.MemoryLogger;
import pl.edu.agh.ed.algorithm.IClosedFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.aprioriClose.AprioriCloseFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.charm.CharmFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.dciClosed.DCIClosedFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.fpclose.FPCloseFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.lcm.LCMFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;
import pl.edu.agh.ed.nodes.transactions.readers.ITransactionSetReader;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringTransactionSetReader;

public class ClosedFrequentPatternBenchmark {
	private static final int MAX_TIME_IN_SECONDS = 240;

	private static final double MAX_SUPPORT = 0.999;

	private static final String SEPARATOR = ";";

	private static final RunnerParameters DEFAULT_PARAMETERS = RunnerParameters.of(Paths.get("data", "default.txt"),
			0.65, 0.65, 0.1, 1);

	public static void main(String[] args) throws IOException {
		Map<String, IClosedFrequentPatternsExtractor<IItem>> extractors = new HashMap<>();

		extractors.put("FPClose", new FPCloseFrequentPatternsClosedExtractor<>());
		extractors.put("AprioriClose", new AprioriCloseFrequentPatternsClosedExtractor<>());
		extractors.put("DCIClosed", new DCIClosedFrequentPatternsClosedExtractor<>());
		extractors.put("Charm", new CharmFrequentPatternsClosedExtractor<>());
		extractors.put("LCM", new LCMFrequentPatternsClosedExtractor<>());

		RunnerParameters parameters;
		String name;
		if (args.length != 1) {
			name = "default";
			parameters = DEFAULT_PARAMETERS;
		} else {
			name = args[0];
			parameters = RunnerParametersDB.getParameters(name);
		}

		ITransactionSetReader<IItem> reader = new OrderedStringTransactionSetReader();
		ITransactionSet<IItem> inputDataSet = reader
				.readTransactionSet(Files.readAllLines(parameters.getDataSetPath()));
		Path outputTimeDirPath = Paths.get("output", "closedFP", name, "time");
		if (!Files.exists(outputTimeDirPath)) {
			Files.createDirectories(outputTimeDirPath);
		}
		Path outputResultSizeDirPath = Paths.get("output", "closedFP", name, "size");
		if (!Files.exists(outputResultSizeDirPath)) {
			Files.createDirectories(outputResultSizeDirPath);
		}
		Path outputMemoryDirPath = Paths.get("output", "closedFP", name, "memory");
		if (!Files.exists(outputMemoryDirPath)) {
			Files.createDirectories(outputMemoryDirPath);
		}
		Path outputResultDirPath = Paths.get("output", "closedFP", name, "result");
		if (!Files.exists(outputResultDirPath)) {
			Files.createDirectories(outputResultDirPath);
		}
		for (IClosedFrequentPatternsExtractor<IItem> extractor : extractors.values()) {
			extractor.extract(inputDataSet, MAX_SUPPORT);
		}
		for (int run = 0; run < parameters.getMaxRuns(); run++) {
			System.out.println(run);
			String fileName = "run-" + run + ".txt";
			Path outputPath = outputTimeDirPath.resolve(fileName);
			Path outputResultSizePath = outputResultSizeDirPath.resolve(fileName);
			Path outputMemoryPath = outputMemoryDirPath.resolve(fileName);
			Path outputResultPath = outputResultDirPath.resolve("run-" + run);
			if (!Files.exists(outputResultPath)) {
				Files.createDirectories(outputResultPath);
			}
			try (PrintStream outputTime = new PrintStream(Files.newOutputStream(outputPath, //
					StandardOpenOption.CREATE, //
					StandardOpenOption.TRUNCATE_EXISTING));
					PrintStream outputResultSize = new PrintStream(Files.newOutputStream(outputResultSizePath, //
							StandardOpenOption.CREATE, //
							StandardOpenOption.TRUNCATE_EXISTING));
					PrintStream outputMemory = new PrintStream(Files.newOutputStream(outputMemoryPath, //
							StandardOpenOption.CREATE, //
							StandardOpenOption.TRUNCATE_EXISTING))) {
				BenchmarkUtils.println(extractors.keySet() //
						.stream() //
						.reduce((s1, s2) -> s1 + SEPARATOR + s2) //
						.map(s -> SEPARATOR + s) //
						.get(), outputTime, outputResultSize, outputMemory);
				for (double support = parameters.getMaxSupport(); support >= parameters
						.getMinSupport(); support -= parameters.getSupportStep()) {
					BenchmarkUtils.print(support, outputTime, outputResultSize, outputMemory);
					for (Entry<String, IClosedFrequentPatternsExtractor<IItem>> entry : extractors.entrySet()) {
						System.runFinalization();
						System.gc();
						MemoryLogger.getInstance().reset();
						MemoryLogger.getInstance().checkMemory();
						double startMemory = MemoryLogger.getInstance().getMaxMemory();
						long start = System.currentTimeMillis();

						IFrequentPatternSet<IItem> result = entry.getValue().extract(inputDataSet, support);

						MemoryLogger.getInstance().checkMemory();
						long stop = System.currentTimeMillis();
						double stopMemory = MemoryLogger.getInstance().getMaxMemory();

						if (run == 0) {
							BenchmarkUtils.writeResultToFile(result, outputResultPath.resolve(entry.getKey() + ".txt"));
						}
						BenchmarkUtils.print(SEPARATOR, outputTime, outputResultSize, outputMemory);
						BenchmarkUtils.print(result.size(), outputResultSize);
						BenchmarkUtils.print(stop - start, outputTime);
						BenchmarkUtils.print(stopMemory - startMemory, outputMemory);
					}
					BenchmarkUtils.println(outputTime, outputResultSize, outputMemory);
				}
			}
		}
	}
}
