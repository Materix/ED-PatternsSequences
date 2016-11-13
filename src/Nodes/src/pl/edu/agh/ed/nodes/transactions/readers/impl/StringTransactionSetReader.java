package pl.edu.agh.ed.nodes.transactions.readers.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.StringItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;
import pl.edu.agh.ed.model.transactions.Transaction;
import pl.edu.agh.ed.model.transactions.TransactionSet;
import pl.edu.agh.ed.nodes.transactions.readers.ITransactionSetReader;

public class StringTransactionSetReader implements ITransactionSetReader<IItem> {
	public ITransactionSet<IItem> readTransactionSet(List<String> list) {
		OfInt itemIdIterator = IntStream.iterate(0, id -> id + 1).iterator();
		Map<String, IItem> items = list.stream()
			.map(s -> s.split(" "))
			.flatMap(Arrays::stream)
			.distinct()
			.collect(Collectors.toMap(Function.identity(), item -> new StringItem(itemIdIterator.next(), item)));
		
		OfInt transactionIdIterator = IntStream.iterate(0, id -> id + 1).iterator();
		return new TransactionSet<>(list.stream().map(s -> s.split(" "))
			.map(Arrays::stream)
			.map(s -> s.map(i -> items.get(i)).collect(Collectors.toList()))
			.map(i -> new Transaction<>(transactionIdIterator.next(), i))
			.collect(Collectors.toSet()));
	}
}
