package pl.edu.agh.ed.algorithm;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IFrequentPatternsExtractor<T extends IItem> {
	IFrequentPatternSet<T> extract(ITransactionSet<T> transactionSet, int minSupport);
	
	default IFrequentPatternSet<T> extract(ITransactionSet<T> transactionSet, double minNormalizedSupport) {
		return extract(transactionSet, (int)Math.floor(minNormalizedSupport * transactionSet.size()));
	};
}
