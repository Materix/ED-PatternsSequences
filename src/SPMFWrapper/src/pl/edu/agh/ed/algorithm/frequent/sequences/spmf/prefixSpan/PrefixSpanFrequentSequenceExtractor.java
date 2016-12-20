package pl.edu.agh.ed.algorithm.frequent.sequences.spmf.prefixSpan;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.AlgoPrefixSpan;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.SequentialPattern;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.SequentialPatterns;
import pl.edu.agh.ed.algorithm.IFrequentSequencesExtractor;
import pl.edu.agh.ed.model.Group;
import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequenceSet;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.model.sequence.ISequenceSet;

public class PrefixSpanFrequentSequenceExtractor implements IFrequentSequencesExtractor {
	private static final Path TEMP_PATH = Paths.get("F:\\TEMP");

	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, int minSupport) {
		return extract(sequenceSet, ((double) minSupport) / sequenceSet.getSequences().size());
	}

	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, double minRelativeSupport) {
		Path tempInputFile = null;
		try {
			tempInputFile = Files.createTempFile(TEMP_PATH, "spmf-input", ".text");
			List<String> lines = sequenceSet
					.stream().map(
							sequence -> sequence.getGroups().stream().map(IGroup::getItemsIds)
									.map(ids -> ids.stream().map(Object::toString).reduce((s1, s2) -> s1 + " " + s2)
											.orElse(""))
									.reduce((s1, s2) -> s1 + " -1 " + s2).map(s -> s + " -2").orElse(""))
					.collect(Collectors.toList());
			Files.write(tempInputFile, lines);

			AlgoPrefixSpan algo = new AlgoPrefixSpan();
			algo.setMaximumPatternLength(400);
			algo.setShowSequenceIdentifiers(false);
			SequentialPatterns result = algo.runAlgorithm(tempInputFile.toString(), minRelativeSupport, null);
			return new FrequentSequenceSet(sequenceSet,
					result.getLevels().stream().flatMap(List::stream).map(pattern -> {
						return (IFrequentSequence) new FrequentSequence(sequenceSet, createGroups(sequenceSet, pattern),
								pattern.getAbsoluteSupport());
					}).collect(Collectors.toSet()));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tempInputFile != null) {
				try {
					Files.delete(tempInputFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return new FrequentSequenceSet(sequenceSet);
	}

	private List<IGroup> createGroups(ISequenceSet sequenceSet, SequentialPattern pattern) {
		return pattern.getItemsets().stream().map(group -> {
			return group.getItems().stream().map(sequenceSet::getItem).collect(Collectors.toList());
		}).map(items -> new Group(0, items)).collect(Collectors.toList());
	}
}
