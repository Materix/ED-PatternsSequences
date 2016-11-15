package pl.edu.agh.ed.model.frequent.patterns;

import java.util.List;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IFrequentPattern<T extends IItem> {
	ITransactionSet<T> getTransactionSet();
	
	List<T> getItems();
	
	long getSupport();
	
	default double getRelativeSupport() {
		return ((double)getSupport()) / getTransactionSet().size();
	}

	default int size() {
		return getItems().size();
	}
}
