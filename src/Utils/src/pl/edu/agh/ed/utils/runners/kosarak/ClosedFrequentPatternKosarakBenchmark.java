package pl.edu.agh.ed.utils.runners.kosarak;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import pl.edu.agh.ed.algorithm.IClosedFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.aprioriClose.AprioriCloseFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.charm.CharmFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.dciClosed.DCIClosedFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.fpclose.FPCloseFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.lcm.LCMFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringTransactionSetReader;
import pl.edu.agh.ed.utils.runners.BenchmarkUtils;

public class ClosedFrequentPatternKosarakBenchmark extends
		KosarakBenchmark<Function<List<String>, ITransactionSet<IItem>>, ITransactionSet<IItem>, SupplierFrequentPatternSet> {

	public ClosedFrequentPatternKosarakBenchmark(Supplier<Function<List<String>, //
	ITransactionSet<IItem>>> readerSupplier, //
			String category, Map<String, BiFunction<ITransactionSet<IItem>, //
			Double, SupplierFrequentPatternSet>> extractors, int maxRun, //
			BiConsumer<SupplierFrequentPatternSet, Path> resultWriter) {
		super(readerSupplier, category, extractors, maxRun, resultWriter, false);
	}

	public static void main(String[] args) throws IOException {
		Map<String, BiFunction<ITransactionSet<IItem>, Double, SupplierFrequentPatternSet>> extractors = new HashMap<>();
		extractors.put("FPClose", wrap(new FPCloseFrequentPatternsClosedExtractor<>()));
		extractors.put("AprioriClose", wrap(new AprioriCloseFrequentPatternsClosedExtractor<>()));
		extractors.put("DCIClosed", wrap(new DCIClosedFrequentPatternsClosedExtractor<>()));
		extractors.put("Charm", wrap(new CharmFrequentPatternsClosedExtractor<>()));
		extractors.put("LCM", wrap(new LCMFrequentPatternsClosedExtractor<>()));

		new ClosedFrequentPatternKosarakBenchmark( //
				() -> list -> new OrderedStringTransactionSetReader().readTransactionSet(list), //
				"closedFP", //
				extractors, //
				5, //
				(result, path) -> {
					try {
						BenchmarkUtils.writeResultToFile(result.getFrequentPatterns(), path);
					} catch (Exception e) {
					}
				}).benchmark();
	}

	private static BiFunction<ITransactionSet<IItem>, Double, SupplierFrequentPatternSet> wrap(
			IClosedFrequentPatternsExtractor<IItem> extractor) {
		return (input, support) -> new SupplierFrequentPatternSet(extractor.extract(input, support));
	}
}
