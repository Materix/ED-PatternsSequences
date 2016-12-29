package pl.edu.agh.ed.utils.runners.kosarak;

import java.util.function.Supplier;

import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;

public class SupplierFrequentSequenceSet implements Supplier<Integer> {

	private final IFrequentSequenceSet frequentSequences;

	public SupplierFrequentSequenceSet(IFrequentSequenceSet frequentSequences) {
		this.frequentSequences = frequentSequences;
	}

	@Override
	public Integer get() {
		return getFrequentSequences().size();
	}

	public IFrequentSequenceSet getFrequentSequences() {
		return frequentSequences;
	}

}
