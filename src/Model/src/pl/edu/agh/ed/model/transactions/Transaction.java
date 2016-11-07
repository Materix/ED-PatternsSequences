package pl.edu.agh.ed.model.transactions;

import java.util.Set;

import pl.edu.agh.ed.model.IItem;

public class Transaction implements ITransaction {
	
	private final int id;
	
	private final Set<IItem> items;

	public Transaction(int id, Set<IItem> items) {
		this.id = id;
		this.items = items;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Set<IItem> getItems() {
		return items;
	}

	@Override
	public boolean contains(Set<IItem> items) {
		return this.items.containsAll(items);
	}

}
