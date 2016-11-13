package pl.edu.agh.ed.model.transactions;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;

public class TransactionSet<T extends IItem> implements ITransactionSet<T> {
	
	private final Set<ITransaction<T>> transactions;
	private final Map<Integer, T> items;

	public TransactionSet(Map<Integer, T> items, Set<ITransaction<T>> transactions) {
		this.items = items;
		this.transactions = transactions;
	}

	@Override
	public Set<ITransaction<T>> getTransactions() {
		return Collections.unmodifiableSet(transactions);
	}

	@Override
	public T getItem(int id) {
		return items.get(id);
	}	
}
