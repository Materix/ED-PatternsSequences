package pl.edu.agh.ed.model.patterns;

import java.util.Set;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class FrequentPattern implements IFrequentPattern {
	
	private final ITransactionSet transactionSet;
	
	private final Set<IItem> items;
	
	public FrequentPattern(ITransactionSet transactionSet, Set<IItem> items) {
		this.transactionSet = transactionSet;
		this.items = items;
	}

	@Override
	public ITransactionSet getTransactionSet() {
		return transactionSet;
	}

	@Override
	public Set<IItem> getItems() {
		return items;
	}

}
