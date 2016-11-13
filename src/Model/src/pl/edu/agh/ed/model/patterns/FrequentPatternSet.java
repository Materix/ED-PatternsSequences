package pl.edu.agh.ed.model.patterns;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class FrequentPatternSet<T extends IItem> implements IFrequentPatternSet<T> {
	private final ITransactionSet<T> transactionSet;
	
	private final Set<IFrequentPattern<T>> frequentPatterns;
	
	public FrequentPatternSet(ITransactionSet<T> transactionSet, Set<IFrequentPattern<T>> frequentPatterns) {
		this.transactionSet = transactionSet;
		this.frequentPatterns = frequentPatterns;
	}

	public FrequentPatternSet(ITransactionSet<T> transactionSet) {
		this(transactionSet, new HashSet<>());
	}

	@Override
	public ITransactionSet<T> getTransactionSet() {
		return transactionSet;
	}

	@Override
	public Set<IFrequentPattern<T>> getFrequentPatterns() {
		return Collections.unmodifiableSet(frequentPatterns);
	}
}
