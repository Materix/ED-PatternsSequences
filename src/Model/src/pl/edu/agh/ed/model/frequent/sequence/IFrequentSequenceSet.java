package pl.edu.agh.ed.model.frequent.sequence;

import java.util.Set;
import java.util.stream.Stream;

import pl.edu.agh.ed.model.sequence.ISequenceSet;

public interface IFrequentSequenceSet {
	ISequenceSet getSequenceSet();
	
	Set<IFrequentSequence> getFrequentSequences();
	
	default Stream<IFrequentSequence> stream() {
		return getFrequentSequences().stream();
	}
	
	default int size() {
		return getFrequentSequences().size();
	}
}
