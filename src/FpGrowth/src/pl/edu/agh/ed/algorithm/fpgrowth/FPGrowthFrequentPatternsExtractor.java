package pl.edu.agh.ed.algorithm.fpgrowth;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.fpgrowth.impl.FPGrowth;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.patterns.FrequentPattern;
import pl.edu.agh.ed.model.patterns.FrequentPatternSet;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class FPGrowthFrequentPatternsExtractor<T extends IItem> implements IFrequentPatternsExtractor<T> {

	@Override
	public IFrequentPatternSet<T> extract(ITransactionSet<T> transactionSet, int minSupport) {
		return new FrequentPatternSet<>(transactionSet, new FPGrowth((ITransactionSet<IItem>) transactionSet, minSupport)
			.getFrequentPatterns()
			.entrySet()
			.stream()
			.map(entry -> new FrequentPattern<T>(transactionSet, (List<T>) new ArrayList<IItem>(entry.getKey()), (long)((int)entry.getValue())))
			.collect(Collectors.toSet()));
	}

}
