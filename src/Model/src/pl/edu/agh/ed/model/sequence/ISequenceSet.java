package pl.edu.agh.ed.model.sequence;

import java.util.Set;
import java.util.stream.Stream;

import pl.edu.agh.ed.model.IItem;

public interface ISequenceSet {
	Set<ISequence> getSequences();
	
	IItem getItem(int id);
	
	default Stream<ISequence> stream() {
		return getSequences().stream();
	}
	
	default int size() {
		return getSequences().size();
	}
}
