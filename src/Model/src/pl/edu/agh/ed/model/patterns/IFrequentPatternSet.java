package pl.edu.agh.ed.model.patterns;

import java.util.Set;
import java.util.stream.Stream;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IFrequentPatternSet<T extends IItem> {
	ITransactionSet<T> getTransactionSet();
	
	Set<IFrequentPattern<T>> getFrequentPatterns();
	
	default Stream<IFrequentPattern<T>> stream() {
		return getFrequentPatterns().stream();
	}
	
	default int size() {
		return getFrequentPatterns().size();
	}
}
