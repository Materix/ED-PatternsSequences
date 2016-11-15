package pl.edu.agh.ed.algorithm.frequent.sequences.spmf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.ed.algorithm.IFrequentSequencesExtractor;
import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequenceSet;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.model.sequence.ISequenceSet;

public abstract class AbstractSPMFFileBasedFrequentSequencesExtractor implements IFrequentSequencesExtractor {
	private static final String SEPARATOR = "#SUP:";

	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, int minSupport) {
		Path tempInputFile = null;
		Path tempOutputFile = null;
		try {
			tempInputFile = Files.createTempFile("spmf-input", ".text");
			List<String> lines = sequenceSet.stream()
				.map(sequence -> 
					sequence.getGroups().stream()
						.map(IGroup::getGroupId)
						.map(id -> id.toString())
						.reduce((s1, s2) -> s1 + " " + s2)
						.orElse("")
				).collect(Collectors.toList());
			Files.write(tempInputFile, lines);
			tempOutputFile = Files.createTempFile("spmf-output", ".text");
			extract(tempInputFile.toString(), tempOutputFile.toString(), ((double)minSupport) / sequenceSet.size());
//			return new IFrequentSequenceSet(sequenceSet, 
//				Files.readAllLines(tempOutputFile)
//					.stream()
//					.map(line -> new FrequentPattern<>(sequenceSet, getItems(line, sequenceSet), getSupport(line)))
//					.collect(Collectors.toSet())
//			);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tempInputFile != null) {
				try {
					Files.delete(tempInputFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
//			if (tempOutputFile != null) {
//				try {
//					Files.delete(tempOutputFile);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
		return new FrequentSequenceSet(sequenceSet);
	}
	
	private List<IGroup> getItems(String line, ISequenceSet sequenceSet) {
		return null;
//		return Arrays.stream(line.split(SEPARATOR)[0].trim().split(" "))
//				.map(Integer::parseInt)
//				.map(transactionSet::getItem)
//				.collect(Collectors.toList());
	}
	
	private int getSupport(String line) {
		return Integer.parseInt(line.split(SEPARATOR)[1].trim());
	}
	
	public abstract void extract(String input, String output, double minRelativeSupport) throws IOException;
}
