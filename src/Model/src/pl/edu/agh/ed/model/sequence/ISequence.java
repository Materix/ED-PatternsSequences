package pl.edu.agh.ed.model.sequence;

import java.util.Collection;
import java.util.List;

import pl.edu.agh.ed.model.IGroup;

public interface ISequence {
	int getId(); 
	
	List<IGroup> getGroups();
	
	boolean contains(Collection<IGroup> groups);
}
