package pl.edu.agh.ed.model.frequent.sequence;

import java.util.HashSet;
import java.util.Set;

import pl.edu.agh.ed.model.sequence.ISequenceSet;

public class FrequentSequenceSet implements IFrequentSequenceSet {
	
	private Set<IFrequentSequence> frequentSequences;
	
	private ISequenceSet sequenceSet;

	public FrequentSequenceSet(ISequenceSet sequenceSet, Set<IFrequentSequence> frequentSequences) {
		this.sequenceSet = sequenceSet;
		this.frequentSequences = frequentSequences;
	}

	public FrequentSequenceSet(ISequenceSet sequenceSet) {
		 this(sequenceSet, new HashSet<>());
	}

	@Override
	public ISequenceSet getSequenceSet() {
		return sequenceSet;
	}

	@Override
	public Set<IFrequentSequence> getFrequentSequences() {
		return frequentSequences;
	}

}
