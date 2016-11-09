package pl.edu.agh.ed.model.patterns;

import java.util.Collections;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class FrequentPattern implements IFrequentPattern {
	
	private final ITransactionSet transactionSet;
	
	private final Set<IItem> items;
	
	private final long support;
	
	public FrequentPattern(ITransactionSet transactionSet, Set<IItem> items) {
		this(transactionSet, items, transactionSet.stream()
			.filter(transaction -> transaction.contains(items))
			.count());
	}
	
	public FrequentPattern(ITransactionSet transactionSet, Set<IItem> items, long support) {
		this.transactionSet = transactionSet;
		this.items = items;
		this.support = support;
	}

	@Override
	public ITransactionSet getTransactionSet() {
		return transactionSet;
	}

	@Override
	public Set<IItem> getItems() {
		return Collections.unmodifiableSet(items);
	}

	@Override
	public long getSupport() {
		return support;
	}

}
