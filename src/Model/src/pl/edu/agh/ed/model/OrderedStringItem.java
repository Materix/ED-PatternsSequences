package pl.edu.agh.ed.model;

public class OrderedStringItem implements IItem {

	private final int id;

	private final String value;

	private final int order;

	public OrderedStringItem(int id, String value, int order) {
		this.id = id;
		this.value = value;
		this.order = order;
	}

	@Override
	public int getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public int getOrder() {
		return order;
	}

	@Override
	public String toString() {
		return Integer.toString(id);
		// return value + ":" + Integer.toString(order);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + order;
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
		OrderedStringItem other = (OrderedStringItem) obj;
		if (id != other.id)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value)) {
			return false;
		} else if (order != other.order) {
			return false;
		}
		return true;
	}
}
