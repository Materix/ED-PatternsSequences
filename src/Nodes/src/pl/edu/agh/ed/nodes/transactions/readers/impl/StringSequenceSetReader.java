package pl.edu.agh.ed.nodes.transactions.readers.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.edu.agh.ed.model.Group;
import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.StringItem;
import pl.edu.agh.ed.model.sequence.ISequenceSet;
import pl.edu.agh.ed.model.sequence.Sequence;
import pl.edu.agh.ed.model.sequence.SequenceSet;
import pl.edu.agh.ed.nodes.transactions.readers.ISequenceSetReader;

public class StringSequenceSetReader implements ISequenceSetReader {

	@Override
	public ISequenceSet readSequenceSet(List<String> rows) {
		OfInt groupIdIterator = IntStream.iterate(1, id -> id + 1).iterator();
		OfInt itemIdIterator = IntStream.iterate(1, id -> id + 1).iterator();
		Map<String, IItem> items = rows.stream().map(s -> s.split("; ")).flatMap(Arrays::stream).map(s -> s.split(", "))
				.flatMap(Arrays::stream).distinct()
				.collect(Collectors.toMap(Function.identity(), item -> new StringItem(itemIdIterator.next(), item)));

		OfInt sequenceIdIterator = IntStream.iterate(1, id -> id + 1).iterator();
		return new SequenceSet(items.values().stream().collect(Collectors.toMap(IItem::getId, i -> i)),
				rows.stream()
						.map(s -> Arrays.stream(s.split("; "))
								.map(group -> Arrays.stream(group.split(", ")).map(items::get).distinct()
										.collect(Collectors.toList()))
						.map(group -> (IGroup) new Group(groupIdIterator.next(), group)).collect(Collectors.toList()))
				.map(groups -> new Sequence(sequenceIdIterator.next(), groups)).collect(Collectors.toSet()));
	}
}
