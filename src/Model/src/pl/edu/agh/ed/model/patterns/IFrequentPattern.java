package pl.edu.agh.ed.model.patterns;

import java.util.Set;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IFrequentPattern {
	ITransactionSet getTransactionSet();
	
	Set<IItem> getItems();
	
	long getSupport();
	
	default double getNormalizedSupport() {
		return getSupport() / getTransactionSet().size();
	}

	default int size() {
		return getItems().size();
	}
}
