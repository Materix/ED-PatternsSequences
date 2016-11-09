package pl.edu.agh.ed.model.transactions;

import java.util.Collection;
import java.util.List;

import pl.edu.agh.ed.model.IItem;

public interface ITransaction {
	int getId(); 
	
	List<IItem> getItems();
	
	boolean contains(Collection<IItem> items);
}
