package pl.edu.agh.ed.nodes.transactions.readers;

import java.util.List;

import pl.edu.agh.ed.model.sequence.ISequenceSet;

public interface ISequenceSetReader {
	ISequenceSet readSequenceSet(List<String> rows);
}
