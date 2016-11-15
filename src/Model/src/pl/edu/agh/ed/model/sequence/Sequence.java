package pl.edu.agh.ed.model.sequence;

import java.util.Collection;
import java.util.List;

import pl.edu.agh.ed.model.IGroup;

public class Sequence implements ISequence {
	
	private final int id;

	private final List<IGroup> groups;

	public Sequence(int id, List<IGroup> groups) {
		this.id = id;
		this.groups = groups;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public List<IGroup> getGroups() {
		return groups;
	}

	@Override
	public boolean contains(Collection<IGroup> groups) {
		return this.groups.contains(groups);
	}

}
