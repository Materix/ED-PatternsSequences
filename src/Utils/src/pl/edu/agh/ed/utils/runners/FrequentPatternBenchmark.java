package pl.edu.agh.ed.utils.runners;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import ca.pfv.spmf.tools.MemoryLogger;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.apriori.AprioriFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.eclat.EclatFrequentPatternsExctractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.fpgrowth.FPGrowthFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.hmine.HMineFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.lcmfreq.LCMFreqFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.prepost.PrePostFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.prepostplus.PrePostPlusFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.relim.RelimFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;
import pl.edu.agh.ed.nodes.transactions.readers.ITransactionSetReader;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringTransactionSetReader;

public class FrequentPatternBenchmark {
	private static final int MAX_TIME_IN_SECONDS = 240;

	private static final double MAX_SUPPORT = 0.999;

	private static final String SEPARATOR = ";";

	private static final RunnerParameters DEFAULT_PARAMETERS = RunnerParameters.of(Paths.get("data", "default.txt"),
			0.001, 0.0001, 0.0001, 3);

	public static void main(String[] args) throws IOException {
		Map<String, IFrequentPatternsExtractor<IItem>> extractors = new HashMap<>();

		extractors.put("PrePost+", new PrePostPlusFrequentPatternsExtractor<>());
		extractors.put("FPGrowth", new FPGrowthFrequentPatternsExtractor<>());
		extractors.put("Eclat", new EclatFrequentPatternsExctractor<>());
		extractors.put("PrePost", new PrePostFrequentPatternsExtractor<>());
		extractors.put("H-Mine", new HMineFrequentPatternsExtractor<>());
		extractors.put("LCMFreq", new LCMFreqFrequentPatternsExtractor<>());
		extractors.put("Relim", new RelimFrequentPatternsExtractor<>());
		extractors.put("Apriori", new AprioriFrequentPatternsExtractor<>());

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
		Path outputTimeDirPath = Paths.get("output", "FP", name, "time");
		if (!Files.exists(outputTimeDirPath)) {
			Files.createDirectories(outputTimeDirPath);
		}
		Path outputMemoryDirPath = Paths.get("output", "FP", name, "memory");
		if (!Files.exists(outputMemoryDirPath)) {
			Files.createDirectories(outputMemoryDirPath);
		}
		Path outputResultSizeDirPath = Paths.get("output", "FP", name, "size");
		if (!Files.exists(outputResultSizeDirPath)) {
			Files.createDirectories(outputResultSizeDirPath);
		}
		for (IFrequentPatternsExtractor<IItem> extractor : extractors.values()) {
			extractor.extract(inputDataSet, MAX_SUPPORT);
		}
		for (int run = 0; run < parameters.getMaxRuns(); run++) {
			System.out.println(run);
			String fileName = "run-" + run + ".txt";
			Path outputPath = outputTimeDirPath.resolve(fileName);
			Path outputResultSizePath = outputResultSizeDirPath.resolve(fileName);
			Path outputMemoryPath = outputMemoryDirPath.resolve(fileName);
			try (PrintStream outputTime = new PrintStream(Files.newOutputStream(outputPath, //
					StandardOpenOption.CREATE, //
					StandardOpenOption.TRUNCATE_EXISTING));
					PrintStream outputResultSize = new PrintStream(Files.newOutputStream(outputResultSizePath, //
							StandardOpenOption.CREATE, //
							StandardOpenOption.TRUNCATE_EXISTING));
					PrintStream outputMemory = new PrintStream(Files.newOutputStream(outputMemoryPath, //
							StandardOpenOption.CREATE, //
							StandardOpenOption.TRUNCATE_EXISTING))) {
				println(extractors.keySet() //
						.stream() //
						.reduce((s1, s2) -> s1 + SEPARATOR + s2) //
						.map(s -> SEPARATOR + s) //
						.get(), outputTime, outputResultSize, outputMemory);
				for (double support = parameters.getMaxSupport(); support >= parameters
						.getMinSupport(); support -= parameters.getSupportStep()) {
					print(support, outputTime, outputResultSize, outputMemory);
					for (IFrequentPatternsExtractor<IItem> extractor : extractors.values()) {
						System.runFinalization();
						System.gc();
						MemoryLogger.getInstance().reset();
						MemoryLogger.getInstance().checkMemory();
						double startMemory = MemoryLogger.getInstance().getMaxMemory();
						long start = System.currentTimeMillis();

						IFrequentPatternSet<IItem> result = extractor.extract(inputDataSet, support);
						MemoryLogger.getInstance().checkMemory();

						long stop = System.currentTimeMillis();
						double stopMemory = MemoryLogger.getInstance().getMaxMemory();
						print(SEPARATOR, outputTime, outputResultSize, outputMemory);

						print(result.size(), outputResultSize);
						print(stop - start, outputTime);
						print(stopMemory - startMemory, outputMemory);
					}
					println(outputTime, outputResultSize, outputMemory);
				}
			}
		}
	}

	private static void print(Object string, PrintStream... printStreams) {
		System.out.print(string + " ");
		for (PrintStream printStream : printStreams) {
			printStream.print(string);
		}
	}

	private static void println(Object string, PrintStream... printStreams) {
		System.out.println(string + " ");
		for (PrintStream printStream : printStreams) {
			printStream.println(string);
		}
	}

	private static void println(PrintStream... printStreams) {
		System.out.println();
		for (PrintStream printStream : printStreams) {
			printStream.println();
		}
	}
}