package pl.edu.agh.ed.model;

public class Item implements IItem {
	
	private final int id;

	public Item(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

}
