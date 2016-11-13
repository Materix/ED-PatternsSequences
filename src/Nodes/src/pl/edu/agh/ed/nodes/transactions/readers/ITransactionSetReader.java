package pl.edu.agh.ed.nodes.transactions.readers;

import java.util.List;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface ITransactionSetReader<T extends IItem> {
	ITransactionSet<T> readTransactionSet(List<String> rows);
}
