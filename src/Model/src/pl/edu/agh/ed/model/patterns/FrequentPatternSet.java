package pl.edu.agh.ed.model.patterns;

import java.util.HashSet;
import java.util.Set;

import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class FrequentPatternSet implements IFrequentPatternSet {
	private final ITransactionSet transactionSet;
	
	private final Set<IFrequentPattern> frequentPatterns;
	
	public FrequentPatternSet(ITransactionSet transactionSet, Set<IFrequentPattern> frequentPatterns) {
		this.transactionSet = transactionSet;
		this.frequentPatterns = frequentPatterns;
	}

	public FrequentPatternSet(ITransactionSet transactionSet) {
		this(transactionSet, new HashSet<>());
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
