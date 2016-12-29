package pl.edu.agh.ed.utils.runners.kosarak;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import ca.pfv.spmf.tools.MemoryLogger;
import pl.edu.agh.ed.utils.runners.BenchmarkUtils;

public class KosarakBenchmark<TReader extends Function<List<String>, TInput>, TInput, TOutput extends Supplier<Integer>> {
	private static final String SEPARATOR = ";";
	private static final double MAX_SUPPORT = 0.999;

	private static final Path KOSARAK_PATH = Paths.get("data", "kosarak.txt");
	private static final Path KOSARAK_SEQ_PATH = Paths.get("data", "kosarakSeq.txt");

	private static final Map<Integer, Double> LINE_TO_SUPPORT;

	static {
		LINE_TO_SUPPORT = new TreeMap<>();
		LINE_TO_SUPPORT.put(5000, 0.0035); /// -> 4009
		LINE_TO_SUPPORT.put(10000, 0.0034); // -> 4065
		LINE_TO_SUPPORT.put(15000, 0.0035); // -> 3968
		LINE_TO_SUPPORT.put(20000, 0.0034); // -> 3961
		LINE_TO_SUPPORT.put(25000, 0.00335); // -> 4003
		LINE_TO_SUPPORT.put(30000, 0.0033); // -> 4009
		LINE_TO_SUPPORT.put(35000, 0.0033); // -> 4107
		LINE_TO_SUPPORT.put(40000, 0.0033); // -> 4064
	}

	private final Supplier<TReader> readerSupplier;
	private final String category;
	private final Map<String, BiFunction<TInput, Double, TOutput>> extractors;
	private final int maxRun;
	private final BiConsumer<TOutput, Path> resultWriter;
	private final Path inputPath;

	/**
	 * 
	 * @param readerSupplier
	 * @param category
	 *            "closedFp, FP, sequence, closedSequence
	 */
	public KosarakBenchmark(Supplier<TReader> readerSupplier, String category,
			Map<String, BiFunction<TInput, Double, TOutput>> extractors, int maxRun,
			BiConsumer<TOutput, Path> resultWriter, boolean seq) {
		this.readerSupplier = readerSupplier;
		this.category = category;
		this.extractors = extractors;
		this.maxRun = maxRun;
		this.resultWriter = resultWriter;
		inputPath = seq ? KOSARAK_SEQ_PATH : KOSARAK_PATH;
	}

	public void benchmark() throws IOException {
		Path outputTimeDirPath = Paths.get("output", "kosarak", category, "time");
		if (!Files.exists(outputTimeDirPath)) {
			Files.createDirectories(outputTimeDirPath);
		}
		Path outputResultSizeDirPath = Paths.get("output", "kosarak", category, "size");
		if (!Files.exists(outputResultSizeDirPath)) {
			Files.createDirectories(outputResultSizeDirPath);
		}
		Path outputMemoryDirPath = Paths.get("output", "kosarak", category, "memory");
		if (!Files.exists(outputMemoryDirPath)) {
			Files.createDirectories(outputMemoryDirPath);
		}
		Path outputResultDirPath = Paths.get("output", "kosarak", category, "result");
		if (!Files.exists(outputResultDirPath)) {
			Files.createDirectories(outputResultDirPath);
		}
		TReader initReader = readerSupplier.get();
		TInput initInputDataSet = initReader.apply(Files.readAllLines(inputPath).subList(0, 1000));
		for (BiFunction<TInput, Double, TOutput> extractor : extractors.values()) {
			extractor.apply(initInputDataSet, MAX_SUPPORT);
		}

		for (int run = 0; run < maxRun; run++) {
			System.out.println(run);
			String fileName = "run-" + run + ".txt";
			Path outputPath = outputTimeDirPath.resolve(fileName);
			Path outputResultSizePath = outputResultSizeDirPath.resolve(fileName);
			Path outputMemoryPath = outputMemoryDirPath.resolve(fileName);
			Path outputResultPath = outputResultDirPath.resolve("run-" + run);
			if (run == 0 && !Files.exists(outputResultPath)) {
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
				for (Entry<Integer, Double> params : LINE_TO_SUPPORT.entrySet()) {
					TReader reader = readerSupplier.get();
					TInput inputDataSet = reader.apply(Files.readAllLines(inputPath).subList(0, params.getKey()));
					BenchmarkUtils.print(params.getKey(), outputTime, outputResultSize, outputMemory);
					for (Entry<String, BiFunction<TInput, Double, TOutput>> entry : extractors.entrySet()) {
						System.runFinalization();
						System.gc();
						MemoryLogger.getInstance().reset();
						MemoryLogger.getInstance().checkMemory();
						double startMemory = MemoryLogger.getInstance().getMaxMemory();
						long start = System.currentTimeMillis();

						TOutput result = entry.getValue().apply(inputDataSet, params.getValue());

						MemoryLogger.getInstance().checkMemory();
						long stop = System.currentTimeMillis();
						double stopMemory = MemoryLogger.getInstance().getMaxMemory();

						if (run == 0) {
							resultWriter.accept(result, outputResultPath.resolve(entry.getKey() + ".txt"));
						}
						BenchmarkUtils.print(SEPARATOR, outputTime, outputResultSize, outputMemory);
						BenchmarkUtils.print(result.get(), outputResultSize);
						BenchmarkUtils.print(stop - start, outputTime);
						BenchmarkUtils.print(stopMemory - startMemory, outputMemory);
					}
					BenchmarkUtils.println(outputTime, outputResultSize, outputMemory);
				}
			}
		}
	}
}
