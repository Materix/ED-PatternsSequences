package pl.edu.agh.ed.model.transactions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pl.edu.agh.ed.model.IItem;

public class Transaction implements ITransaction {
	
	private final int id;
	
	private final List<IItem> items;

	public Transaction(int id, List<IItem> items) {
		this.id = id;
		this.items = items;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public List<IItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	@Override
	public boolean contains(Collection<IItem> items) {
		return this.items.containsAll(items);
	}

}
