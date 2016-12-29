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
import pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf.bidePlus.BidePlusClosedFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf.clasp.ClaSPClosedFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf.clospan.CloSpanClosedFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf.cmclasp.CMClaSPClosedFrequentSequenceExtractor;
import pl.edu.agh.ed.model.sequence.ISequenceSet;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringSequenceSetReader;
import pl.edu.agh.ed.utils.runners.BenchmarkUtils;

public class ClosedFrequentSequenceKosarakBenchmark
		extends KosarakBenchmark<Function<List<String>, ISequenceSet>, ISequenceSet, SupplierFrequentSequenceSet> {

	public ClosedFrequentSequenceKosarakBenchmark(Supplier<Function<List<String>, ISequenceSet>> readerSupplier,
			String category, Map<String, BiFunction<ISequenceSet, Double, SupplierFrequentSequenceSet>> extractors,
			int maxRun, BiConsumer<SupplierFrequentSequenceSet, Path> resultWriter) {
		super(readerSupplier, category, extractors, maxRun, resultWriter, true);
	}

	public static void main(String[] args) throws IOException {
		Map<String, BiFunction<ISequenceSet, Double, SupplierFrequentSequenceSet>> extractors = new HashMap<>();
		extractors.put("BIDEPlus", wrap(new BidePlusClosedFrequentSequenceExtractor()));
		extractors.put("ClaSP", wrap(new ClaSPClosedFrequentSequenceExtractor()));
		extractors.put("CloSpan", wrap(new CloSpanClosedFrequentSequenceExtractor()));
		extractors.put("CMClaSP", wrap(new CMClaSPClosedFrequentSequenceExtractor()));

		new ClosedFrequentSequenceKosarakBenchmark( //
				() -> list -> new OrderedStringSequenceSetReader().readSequenceSet(list), //
				"closedSequence", //
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