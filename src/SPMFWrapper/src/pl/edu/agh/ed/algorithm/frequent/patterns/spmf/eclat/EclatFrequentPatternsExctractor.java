package pl.edu.agh.ed.algorithm.frequent.patterns.spmf.eclat;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.eclat.AlgoEclat;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.AbstractSPMFFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class EclatFrequentPatternsExctractor<T extends IItem> extends AbstractSPMFFrequentPatternsExtractor<T>
		implements IFrequentPatternsExtractor<T> {

	@Override
	public IFrequentPatternSet<T> extract(ITransactionSet<T> transactionSet, int minSupport) {
		AlgoEclat algoEclat = new AlgoEclat();
		try {
			TransactionDatabase database = createDatabase(transactionSet);
			Itemsets result = algoEclat.runAlgorithm(null, database,
					((double) minSupport) / transactionSet.getTransactions().size(), false);
			return createFrequentPatterns(transactionSet, result);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
