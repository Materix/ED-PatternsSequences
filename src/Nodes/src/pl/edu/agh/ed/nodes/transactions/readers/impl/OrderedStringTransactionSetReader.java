package pl.edu.agh.ed.nodes.transactions.readers.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.OrderedStringItem;
import pl.edu.agh.ed.model.transactions.ITransaction;
import pl.edu.agh.ed.model.transactions.ITransactionSet;
import pl.edu.agh.ed.model.transactions.Transaction;
import pl.edu.agh.ed.model.transactions.TransactionSet;
import pl.edu.agh.ed.nodes.transactions.readers.ITransactionSetReader;

public class OrderedStringTransactionSetReader implements ITransactionSetReader<IItem> {
	public ITransactionSet<IItem> readTransactionSet(List<String> rows) {
		OfInt itemIdIterator = IntStream.iterate(0, id -> id + 1).iterator();
		OfInt transactionIdIterator = IntStream.iterate(0, id -> id + 1).iterator();
		Map<Pair, IItem> cache = new HashMap<>();
		Set<ITransaction<IItem>> transactions = rows.stream().map(row -> {
			Map<String, Integer> frequency = new HashMap<>();
			return new Transaction<>(transactionIdIterator.nextInt(), Arrays.asList(row.split(" ")).stream().map(item -> {
				int order = frequency.getOrDefault(item, 0) + 1;
				frequency.put(item, order);
				
				Pair key = Pair.of(item, order);
				if (!cache.containsKey(key)) {
					cache.put(key, new OrderedStringItem(itemIdIterator.nextInt(), item, order));
				}
				return cache.get(key);
			}).collect(Collectors.toList()));
		}).collect(Collectors.toSet());
		return new TransactionSet<>(cache.values().stream().collect(Collectors.toMap(IItem::getId, i -> i)), transactions);
	}
	
	private static final class Pair {
		private final String value;
		
		private final int order;
		
		private Pair(String value, int order) {
			this.value = value;
			this.order = order;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + order;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
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
			Pair other = (Pair) obj;
			if (order != other.order)
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
		
		public static Pair of(String value, int order) {
			return new Pair(value, order);
		}
	}
}
