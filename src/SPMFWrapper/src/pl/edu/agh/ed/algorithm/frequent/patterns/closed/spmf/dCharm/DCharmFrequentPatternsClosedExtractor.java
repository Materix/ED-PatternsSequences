package pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.dCharm;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ca.pfv.spmf.algorithms.frequentpatterns.charm.AlgoDCharm_Bitset;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;
import ca.pfv.spmf.patterns.itemset_array_integers_with_tids_bitset.Itemsets;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.AbstractSPMFClosedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPattern;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPatternSet;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class DCharmFrequentPatternsClosedExtractor<T extends IItem>
		extends AbstractSPMFClosedFrequentPatternsExtractor<T> {

	@Override
	public IFrequentPatternSet<T> extract(ITransactionSet<T> transactionSet, int minSupport) {
		AlgoDCharm_Bitset algo = new AlgoDCharm_Bitset();
		try {
			TransactionDatabase database = createDatabase(transactionSet);
			Itemsets result = algo.runAlgorithm(null, database,
					((double) minSupport) / transactionSet.getTransactions().size(), false, 10000);
			return new FrequentPatternSet<>(transactionSet,
					result.getLevels().stream().flatMap(List::stream)
							.map(itemset -> new FrequentPattern<>(transactionSet, Arrays.stream(itemset.itemset)
									.mapToObj(transactionSet::getItem).collect(Collectors.toList())))
					.collect(Collectors.toSet()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
