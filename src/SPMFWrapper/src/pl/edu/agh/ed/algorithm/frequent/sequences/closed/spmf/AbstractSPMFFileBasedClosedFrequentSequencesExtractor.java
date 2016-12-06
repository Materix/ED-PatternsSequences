package pl.edu.agh.ed.algorithm.frequent.sequences.closed.spmf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.ed.algorithm.IClosedFrequentSequencesExtractor;
import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequenceSet;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.model.sequence.ISequenceSet;

public abstract class AbstractSPMFFileBasedClosedFrequentSequencesExtractor
		implements IClosedFrequentSequencesExtractor {

	@Override
	public IFrequentSequenceSet extract(ISequenceSet sequenceSet, int minSupport) {
		Path tempInputFile = null;
		Path tempOutputFile = null;
		try {
			tempInputFile = Files.createTempFile("spmf-input", ".text");
			List<String> lines = sequenceSet
					.stream().map(sequence -> sequence.getGroups().stream().map(IGroup::getGroupId)
							.map(id -> id.toString()).reduce((s1, s2) -> s1 + " " + s2).orElse(""))
					.collect(Collectors.toList());
			Files.write(tempInputFile, lines);
			tempOutputFile = Files.createTempFile("spmf-output", ".text");
			extract(tempInputFile.toString(), tempOutputFile.toString(), ((double) minSupport) / sequenceSet.size());
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
		}
		return new FrequentSequenceSet(sequenceSet);
	}

	public abstract void extract(String input, String output, double minRelativeSupport) throws IOException;
}
