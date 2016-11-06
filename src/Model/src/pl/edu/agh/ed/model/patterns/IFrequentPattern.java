package pl.edu.agh.ed.model.patterns;

import java.util.Set;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IFrequentPattern {
	ITransactionSet getTransactionSet();
	
	Set<IItem> getItems();
	
	default long getSupport() {
		Set<IItem> items = getItems();
		return getTransactionSet().stream()
			.filter(transaction -> transaction.contains(items))
			.count();
	}
	
	default double getNormalizedSupport() {
		return getSupport() / getTransactionSet().size();
	}
}
