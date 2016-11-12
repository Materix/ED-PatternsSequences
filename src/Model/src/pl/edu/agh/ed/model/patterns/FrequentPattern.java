package pl.edu.agh.ed.model.patterns;

import java.util.Collections;
import java.util.List;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class FrequentPattern implements IFrequentPattern {
	
	private final ITransactionSet transactionSet;
	
	private final List<IItem> items;
	
	private final long support;
	
	public FrequentPattern(ITransactionSet transactionSet, List<IItem> items) {
		this(transactionSet, items, transactionSet.stream()
			.filter(transaction -> transaction.contains(items))
			.count());
	}
	
	public FrequentPattern(ITransactionSet transactionSet, List<IItem> items, long support) {
		this.transactionSet = transactionSet;
		this.items = items;
		this.support = support;
	}

	@Override
	public ITransactionSet getTransactionSet() {
		return transactionSet;
	}

	@Override
	public List<IItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	@Override
	public long getSupport() {
		return support;
	}

	@Override
	public String toString() {
		return "FrequentPattern [items=" + items + ", support=" + support + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transactionSet == null) ? 0 : transactionSet.hashCode());
		result = prime * result + ((items == null) ? 0 : items.size());
		if (items != null) {
			result = prime * result + items.stream().mapToInt(Object::hashCode).sum();
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FrequentPattern other = (FrequentPattern) obj;
		if (items == null) {
			if (other.items != null) {
				return false;
			}
		} 
		if (transactionSet == null) {
			if (other.transactionSet != null) {
				return false;
			}
		} else if (!transactionSet.equals(other.transactionSet)) {
			return false;
		} else if (items.size() != other.items.size()) {
			return false;
		}
		return items.containsAll(other.items);
	}
}
