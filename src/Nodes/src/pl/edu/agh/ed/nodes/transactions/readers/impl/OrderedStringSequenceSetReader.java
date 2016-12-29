package pl.edu.agh.ed.nodes.transactions.readers.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.edu.agh.ed.model.Group;
import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.OrderedStringItem;
import pl.edu.agh.ed.model.sequence.ISequence;
import pl.edu.agh.ed.model.sequence.ISequenceSet;
import pl.edu.agh.ed.model.sequence.Sequence;
import pl.edu.agh.ed.model.sequence.SequenceSet;
import pl.edu.agh.ed.nodes.transactions.readers.ISequenceSetReader;

public class OrderedStringSequenceSetReader implements ISequenceSetReader {

	@Override
	public ISequenceSet readSequenceSet(List<String> rows) {
		OfInt groupIdIterator = IntStream.iterate(1, id -> id + 1).iterator();
		OfInt itemIdIterator = IntStream.iterate(1, id -> id + 1).iterator();
		OfInt sequenceIdIterator = IntStream.iterate(1, id -> id + 1).iterator();
		Map<Pair, IItem> cache = new HashMap<>();
		Set<ISequence> sequences = rows.stream().map(row -> {
			return new Sequence(sequenceIdIterator.next(), Arrays.stream(row.split("; ")).map(group -> {
				Map<String, Integer> frequency = new HashMap<>();
				return (IGroup) new Group(groupIdIterator.next(), Arrays.stream(group.split(" ")).map(item -> {
					int order = frequency.getOrDefault(item, 0) + 1;
					frequency.put(item, order);

					Pair key = Pair.of(item, order);
					if (!cache.containsKey(key)) {
						cache.put(key, new OrderedStringItem(itemIdIterator.nextInt(), item, order));
					}
					return cache.get(key);
				}).collect(Collectors.toList()));
			}).collect(Collectors.toList()));
		}).collect(Collectors.toSet());

		return new SequenceSet(cache.values().stream().collect(Collectors.toMap(IItem::getId, i -> i)), sequences);
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
