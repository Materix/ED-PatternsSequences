package pl.edu.agh.ed.algorithm;

import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.model.sequence.ISequenceSet;

public interface IFrequentSequencesExtractor {
	IFrequentSequenceSet extract(ISequenceSet sequenceSet, int minSupport);
	
	default IFrequentSequenceSet extract(ISequenceSet sequenceSet, double minRelativeSupport) {
		return extract(sequenceSet, (int)Math.floor(minRelativeSupport * sequenceSet.size()));
	};
}
