package pl.edu.agh.ed.model.patterns;

import java.util.List;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IFrequentPattern {
	ITransactionSet getTransactionSet();
	
	List<IItem> getItems();
	
	long getSupport();
	
	default double getNormalizedSupport() {
		return ((double)getSupport()) / getTransactionSet().size();
	}

	default int size() {
		return getItems().size();
	}
}
