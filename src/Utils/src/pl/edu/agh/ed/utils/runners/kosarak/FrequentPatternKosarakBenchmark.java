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
import pl.edu.agh.ed.model.transactions.ITransactionSet;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringTransactionSetReader;
import pl.edu.agh.ed.utils.runners.BenchmarkUtils;

public class FrequentPatternKosarakBenchmark extends
		KosarakBenchmark<Function<List<String>, ITransactionSet<IItem>>, ITransactionSet<IItem>, SupplierFrequentPatternSet> {

	public FrequentPatternKosarakBenchmark(Supplier<Function<List<String>, //
	ITransactionSet<IItem>>> readerSupplier, //
			String category, Map<String, BiFunction<ITransactionSet<IItem>, //
			Double, SupplierFrequentPatternSet>> extractors, int maxRun, //
			BiConsumer<SupplierFrequentPatternSet, Path> resultWriter) {
		super(readerSupplier, category, extractors, maxRun, resultWriter, false);
	}

	public static void main(String[] args) throws IOException {
		Map<String, BiFunction<ITransactionSet<IItem>, Double, SupplierFrequentPatternSet>> extractors = new HashMap<>();
		extractors.put("PrePost+", wrap(new PrePostPlusFrequentPatternsExtractor<>()));
		extractors.put("FPGrowth", wrap(new FPGrowthFrequentPatternsExtractor<>()));
		extractors.put("Eclat", wrap(new EclatFrequentPatternsExctractor<>()));
		extractors.put("PrePost", wrap(new PrePostFrequentPatternsExtractor<>()));
		extractors.put("H-Mine", wrap(new HMineFrequentPatternsExtractor<>()));
		extractors.put("LCMFreq", wrap(new LCMFreqFrequentPatternsExtractor<>()));
		extractors.put("Relim", wrap(new RelimFrequentPatternsExtractor<>()));
		extractors.put("Apriori", wrap(new AprioriFrequentPatternsExtractor<>()));

		new FrequentPatternKosarakBenchmark( //
				() -> list -> new OrderedStringTransactionSetReader().readTransactionSet(list), //
				"FP", //
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
			IFrequentPatternsExtractor<IItem> extractor) {
		return (input, support) -> new SupplierFrequentPatternSet(extractor.extract(input, support));
	}
}
