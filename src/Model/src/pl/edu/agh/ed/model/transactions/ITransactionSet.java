package pl.edu.agh.ed.model.transactions;

import java.util.Set;
import java.util.stream.Stream;

public interface ITransactionSet {
	Set<ITransaction> getTransactions();
	
	default Stream<ITransaction> stream() {
		return getTransactions().stream();
	}
	
	default int size() {
		return getTransactions().size();
	}
}
