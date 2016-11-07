package pl.edu.agh.ed.algorithm;

import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IFrequentPatternsExtractor {
	IFrequentPatternSet extract(ITransactionSet transactionSet);
}
