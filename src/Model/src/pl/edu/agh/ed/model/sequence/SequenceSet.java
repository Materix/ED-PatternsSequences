package pl.edu.agh.ed.model.sequence;

import java.util.Map;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;

public class SequenceSet implements ISequenceSet {
	
	private final Set<ISequence> sequences;
	private Map<Integer, IItem> items;
	
	public SequenceSet(Map<Integer, IItem> items, Set<ISequence> sequences) {
		this.items = items;
		this.sequences = sequences;
	}

	@Override
	public Set<ISequence> getSequences() {
		return sequences;
	}

	@Override
	public IItem getItem(int id) {
		return items.get(id);
	}

}
