package pl.edu.agh.ed.model.patterns;

import java.util.Set;

import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class FrequentPatternSet implements IFrequentPatternSet {
	private final ITransactionSet transactionSet;
	
	private final Set<IFrequentPattern> frequentPatterns;
	
	public FrequentPatternSet(ITransactionSet transtionSet, Set<IFrequentPattern> frequentPatterns) {
		this.transactionSet = transtionSet;
		this.frequentPatterns = frequentPatterns;
	}

	@Override
	public ITransactionSet getTransactionSet() {
		return transactionSet;
	}

	@Override
	public Set<IFrequentPattern> getFrequentPatterns() {
		return frequentPatterns;
	}
}
