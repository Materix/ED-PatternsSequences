package pl.edu.agh.ed.model;

import java.util.List;

public class Group implements IGroup {
	private final int id;
	
	private final List<IItem> items;
	
	public Group(int id, List<IItem> items) {
		this.id = id;
		this.items = items;
	}

	@Override
	public int getGroupId() {
		return id;
	}

	@Override
	public List<IItem> getItems() {
		return items;
	}
	
	@Override
	public String toString() {
		return items.stream().map(Object::toString).reduce((s1, s2) -> s1 + " " + s2).orElse("");
	}
}
