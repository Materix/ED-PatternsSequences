package pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf.clasp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.AlgoClaSP;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator_Qualitative;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreatorStandard_Map;
import pl.edu.agh.ed.algorithm.IClosedFrequentSequencesExtractor;
import pl.edu.agh.ed.model.Group;
import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequenceSet;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.model.sequence.ISequenceSet;

public class ClaSPClosedFrequentSequenceExtractor implements IClosedFrequentSequencesExtractor {
	private static final String SEPARATOR = "#SUP:";
	private static final Path TEMP_PATH = Paths.get("F:\\TEMP");

	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, int minSupport) {
		return extract(sequenceSet, ((double) minSupport) / sequenceSet.getSequences().size());
	}

	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, double minRelativeSupport) {
		Path tempOutputFile = null;
		Path tempInputFile = null;
		try {
			tempInputFile = Files.createTempFile(TEMP_PATH, "spmf-input", ".text");
			List<String> lines = sequenceSet
					.stream().map(
							sequence -> sequence.getGroups().stream().map(IGroup::getItemsIds)
									.map(ids -> ids.stream().map(Object::toString).reduce((s1, s2) -> s1 + " " + s2)
											.orElse(""))
									.reduce((s1, s2) -> s1 + " -1 " + s2).map(s -> s + " -1 -2").orElse(""))
					.collect(Collectors.toList());
			Files.write(tempInputFile, lines);
			tempOutputFile = Files.createTempFile(TEMP_PATH, "spmf-output", ".text");

			AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative.getInstance();
			IdListCreator idListCreator = IdListCreatorStandard_Map.getInstance();

			SequenceDatabase sequenceDatabase = new SequenceDatabase(abstractionCreator, idListCreator);

			double support = sequenceDatabase.loadFile(tempInputFile.toString(), minRelativeSupport);

			AlgoClaSP algo = new AlgoClaSP(support, abstractionCreator, true, true);
			algo.runAlgorithm(sequenceDatabase, true, false, tempOutputFile.toString(), false);

			return new FrequentSequenceSet(sequenceSet,
					Files.readAllLines(tempOutputFile).stream()
							.map(line -> (IFrequentSequence) new FrequentSequence(sequenceSet,
									getGroups(line, sequenceSet), getSupport(line)))
							.collect(Collectors.toSet()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tempOutputFile != null) {
				try {
					Files.delete(tempOutputFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return new FrequentSequenceSet(sequenceSet);
	}

	private List<IGroup> getGroups(String line, ISequenceSet sequenceSet) {
		return Arrays.stream(line.split(SEPARATOR)[0].trim().split("-1")).map(String::trim)
				.filter(s -> s.length() > 0).map(group -> new Group(0, Arrays.stream(group.split(" "))
						.map(Integer::parseInt).map(sequenceSet::getItem).collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

	private int getSupport(String line) {
		return Integer.parseInt(line.split(SEPARATOR)[1].trim());
	}
}
