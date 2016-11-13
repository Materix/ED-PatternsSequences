package pl.edu.agh.ed.model.transactions;

import java.util.Set;
import java.util.stream.Stream;

import pl.edu.agh.ed.model.IItem;

public interface ITransactionSet<T extends IItem> {
	Set<ITransaction<T>> getTransactions();
	
	default Stream<ITransaction<T>> stream() {
		return getTransactions().stream();
	}
	
	default int size() {
		return getTransactions().size();
	}
}
