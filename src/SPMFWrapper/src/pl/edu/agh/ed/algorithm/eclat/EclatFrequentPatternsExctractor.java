package pl.edu.agh.ed.algorithm.eclat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ca.pfv.spmf.algorithms.frequentpatterns.eclat.AlgoEclat;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.patterns.FrequentPattern;
import pl.edu.agh.ed.model.patterns.FrequentPatternSet;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransaction;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class EclatFrequentPatternsExctractor<T extends IItem> implements IFrequentPatternsExtractor<T> {

	@Override
	public IFrequentPatternSet<T> extract(ITransactionSet<T> transactionSet, int minSupport) {
		AlgoEclat algoEclat = new AlgoEclat();
		try {
			TransactionDatabase database = new TransactionDatabase();
			transactionSet.stream()
				.map(ITransaction::getItems)
				.map(List::stream)
				.map(stream -> stream.map(IItem::getId).collect(Collectors.toList()))
				.forEach(database::addTransaction);
			Itemsets result = algoEclat.runAlgorithm(null, database, ((double)minSupport) / transactionSet.getTransactions().size(), false);
			return new FrequentPatternSet<>(transactionSet, result.getLevels()
				.stream()
				.flatMap(List::stream)
				.map(itemset -> new FrequentPattern<>(transactionSet, Arrays.stream(itemset.itemset)
						.mapToObj(transactionSet::getItem)
						.collect(Collectors.toList())))
				.collect(Collectors.toSet()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
