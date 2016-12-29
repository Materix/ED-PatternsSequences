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

import pl.edu.agh.ed.algorithm.IFrequentSequencesExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.cmspade.CMSPADEFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.cmspam.CMSPAMFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.lapin.LAPINFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.prefixSpan.PrefixSpanFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.spade.SPADEFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.spam.SPAMFrequentSequenceExtractor;
import pl.edu.agh.ed.model.sequence.ISequenceSet;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringSequenceSetReader;
import pl.edu.agh.ed.utils.runners.BenchmarkUtils;

public class FrequentSequenceKosarakBenchmark
		extends KosarakBenchmark<Function<List<String>, ISequenceSet>, ISequenceSet, SupplierFrequentSequenceSet> {

	public FrequentSequenceKosarakBenchmark(Supplier<Function<List<String>, ISequenceSet>> readerSupplier,
			String category, Map<String, BiFunction<ISequenceSet, Double, SupplierFrequentSequenceSet>> extractors,
			int maxRun, BiConsumer<SupplierFrequentSequenceSet, Path> resultWriter) {
		super(readerSupplier, category, extractors, maxRun, resultWriter, true);
	}

	public static void main(String[] args) throws IOException {
		Map<String, BiFunction<ISequenceSet, Double, SupplierFrequentSequenceSet>> extractors = new HashMap<>();
		extractors.put("CMSPAM", wrap(new CMSPAMFrequentSequenceExtractor()));
		extractors.put("CMSPADE", wrap(new CMSPADEFrequentSequenceExtractor()));
		extractors.put("SPADE", wrap(new SPADEFrequentSequenceExtractor()));
		extractors.put("SPAM", wrap(new SPAMFrequentSequenceExtractor()));
		extractors.put("PrefixSpan", wrap(new PrefixSpanFrequentSequenceExtractor()));
		extractors.put("LAPIN", wrap(new LAPINFrequentSequenceExtractor()));

		new FrequentSequenceKosarakBenchmark( //
				() -> list -> new OrderedStringSequenceSetReader().readSequenceSet(list), //
				"sequence", //
				extractors, //
				5, //
				(result, path) -> {
					try {
						BenchmarkUtils.writeResultToFile(result.getFrequentSequences(), path);
					} catch (Exception e) {
					}
				}).benchmark();
	}

	private static BiFunction<ISequenceSet, Double, SupplierFrequentSequenceSet> wrap(
			IFrequentSequencesExtractor extractor) {
		return (input, support) -> new SupplierFrequentSequenceSet(extractor.extract(input, support));
	}
}