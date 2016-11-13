package pl.edu.agh.ed.model.transactions;

import java.util.Collection;
import java.util.List;

import pl.edu.agh.ed.model.IItem;

public interface ITransaction<T extends IItem> {
	int getId(); 
	
	List<T> getItems();
	
	boolean contains(Collection<T> items);
}
