package pl.edu.agh.ed.model.transactions;

import java.util.Set;

import pl.edu.agh.ed.model.IItem;

public interface ITransaction {
	int getId(); 
	
	Set<IItem> getItems();
	
	boolean contains(Set<IItem> items);
}
