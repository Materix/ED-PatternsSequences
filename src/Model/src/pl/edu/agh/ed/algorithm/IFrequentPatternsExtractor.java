package pl.edu.agh.ed.algorithm;

import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IFrequentPatternsExtractor {
	IFrequentPatternSet extract(ITransactionSet transactionSet, int minSupport);
	
	default IFrequentPatternSet extract(ITransactionSet transactionSet, double minNormalizedSupport) {
		return extract(transactionSet, (int)Math.floor(minNormalizedSupport * transactionSet.size()));
	};
}
