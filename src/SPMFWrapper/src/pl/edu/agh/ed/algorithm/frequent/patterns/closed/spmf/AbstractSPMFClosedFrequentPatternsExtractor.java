package pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
import pl.edu.agh.ed.algorithm.IClosedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPattern;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPatternSet;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransaction;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public abstract class AbstractSPMFClosedFrequentPatternsExtractor<T extends IItem>
		implements IClosedFrequentPatternsExtractor<T> {

	protected TransactionDatabase createDatabase(ITransactionSet<T> transactionSet) {
		TransactionDatabase database = new TransactionDatabase();
		transactionSet.stream().map(ITransaction::getItems).map(List::stream)
				.map(stream -> stream.map(IItem::getId).collect(Collectors.toList())).forEach(database::addTransaction);
		return database;
	}

	protected IFrequentPatternSet<T> createFrequentPatterns(ITransactionSet<T> transactionSet, Itemsets result) {
		return new FrequentPatternSet<>(transactionSet,
				result.getLevels().stream().flatMap(List::stream)
						.map(itemset -> new FrequentPattern<>(transactionSet, Arrays.stream(itemset.itemset)
								.mapToObj(transactionSet::getItem).collect(Collectors.toList())))
				.collect(Collectors.toSet()));
	}
}
