package pl.edu.agh.ed.model.transactions;

import java.util.Collections;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;

public class TransactionSet<T extends IItem> implements ITransactionSet<T> {
	
	private final Set<ITransaction<T>> transactions;

	public TransactionSet(Set<ITransaction<T>> transactions) {
		this.transactions = transactions;
	}

	@Override
	public Set<ITransaction<T>> getTransactions() {
		return Collections.unmodifiableSet(transactions);
	}	
}
