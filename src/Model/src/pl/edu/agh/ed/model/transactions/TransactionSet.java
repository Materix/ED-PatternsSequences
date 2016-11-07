package pl.edu.agh.ed.model.transactions;

import java.util.Set;

public class TransactionSet implements ITransactionSet {
	
	private final Set<ITransaction> transactions;

	public TransactionSet(Set<ITransaction> transactions) {
		this.transactions = transactions;
	}

	@Override
	public Set<ITransaction> getTransactions() {
		return transactions;
	}
}
