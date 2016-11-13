package pl.edu.agh.ed.model.transactions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pl.edu.agh.ed.model.IItem;

public class Transaction<T extends IItem> implements ITransaction<T> {
	
	private final int id;
	
	private final List<T> items;

	public Transaction(int id, List<T> items) {
		this.id = id;
		this.items = items;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public List<T> getItems() {
		return Collections.unmodifiableList(items);
	}

	@Override
	public boolean contains(Collection<T> items) {
		return this.items.containsAll(items);
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", items=" + items + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction<?> other = (Transaction<?>) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
