package pl.edu.agh.ed.model;

import java.util.List;
import java.util.stream.Collectors;

public interface IGroup {
	int getGroupId();
	
	List<IItem> getItems();
	
	default List<Integer> getItemsIds() {
		return getItems().stream().map(IItem::getId).collect(Collectors.toList());
	}
}
