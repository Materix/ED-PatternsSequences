package pl.edu.agh.ed.model.frequent.sequence;

import java.util.List;

import pl.edu.agh.ed.model.IGroup;
import pl.edu.agh.ed.model.sequence.ISequenceSet;

public interface IFrequentSequence {
	ISequenceSet getSequenceSet();
	
	List<IGroup> getGroups();
	
	long getSupport();
	
	default double getRelativeSupport() {
		return ((double)getSupport()) / getSequenceSet().size();
	}
}
