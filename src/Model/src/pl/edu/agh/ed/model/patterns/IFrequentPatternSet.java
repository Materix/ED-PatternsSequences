package pl.edu.agh.ed.model.patterns;

import java.util.Set;
import java.util.stream.Stream;

import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IFrequentPatternSet {
	ITransactionSet getTransactionSet();
	
	Set<IFrequentPattern> getFrequentPatterns();
	
	default Stream<IFrequentPattern> stream() {
		return getFrequentPatterns().stream();
	}
	
	default int size() {
		return getFrequentPatterns().size();
	}
}
