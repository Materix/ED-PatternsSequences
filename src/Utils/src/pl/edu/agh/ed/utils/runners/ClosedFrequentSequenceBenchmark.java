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
import pl.edu.agh.ed.algorithm.IClosedFrequentSequencesExtractor;
import pl.edu.agh.ed.algorithm.IFrequentSequencesExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf.bidePlus.BidePlusClosedFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf.clasp.ClaSPClosedFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf.clospan.CloSpanClosedFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf.cmclasp.CMClaSPClosedFrequentSequenceExtractor;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.model.sequence.ISequenceSet;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringSequenceSetReader;

public class ClosedFrequentSequenceBenchmark {
	private static final int MAX_TIME_IN_SECONDS = 240;

	private static final double MAX_SUPPORT = 0.999;

	private static final String SEPARATOR = ";";

	private static final RunnerParameters DEFAULT_PARAMETERS = RunnerParameters.of(Paths.get("data", "kosarakSeq.txt"),
			0.01, 0.0011, 0.001, 1);

	public static void main(String[] args) throws IOException {
		Map<String, IClosedFrequentSequencesExtractor> extractors = new HashMap<>();

		extractors.put("BIDE", new BidePlusClosedFrequentSequenceExtractor());
		extractors.put("ClaSP", new ClaSPClosedFrequentSequenceExtractor());
		extractors.put("CloSpan", new CloSpanClosedFrequentSequenceExtractor());
		extractors.put("CMClaSP", new CMClaSPClosedFrequentSequenceExtractor());

		RunnerParameters parameters;
		String name;
		if (args.length != 1) {
			name = "default";
			parameters = DEFAULT_PARAMETERS;
		} else {
			name = args[0];
			parameters = RunnerParametersDB.getParameters(name);
		}

		OrderedStringSequenceSetReader reader = new OrderedStringSequenceSetReader();
		ISequenceSet inputDataSet = reader.readSequenceSet(Files.readAllLines(parameters.getDataSetPath()));
		Path outputTimeDirPath = Paths.get("output", "closedSequence", name, "time");
		if (!Files.exists(outputTimeDirPath)) {
			Files.createDirectories(outputTimeDirPath);
		}
		Path outputResultSizeDirPath = Paths.get("output", "closedSequence", name, "size");
		if (!Files.exists(outputResultSizeDirPath)) {
			Files.createDirectories(outputResultSizeDirPath);
		}
		Path outputMemoryDirPath = Paths.get("output", "closedSequence", name, "memory");
		if (!Files.exists(outputMemoryDirPath)) {
			Files.createDirectories(outputMemoryDirPath);
		}
		for (IFrequentSequencesExtractor extractor : extractors.values()) {
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
					for (IFrequentSequencesExtractor extractor : extractors.values()) {
						System.runFinalization();
						System.gc();
						MemoryLogger.getInstance().reset();
						MemoryLogger.getInstance().checkMemory();
						double startMemory = MemoryLogger.getInstance().getMaxMemory();
						long start = System.currentTimeMillis();

						IFrequentSequenceSet result = extractor.extract(inputDataSet, support);
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
