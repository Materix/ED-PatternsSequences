package pl.edu.agh.ed.nodes.graphs.readers;

import java.util.List;

import pl.edu.agh.ed.model.graph.ILabeledGraphsSet;

public interface ILabeledGraphsSetReader {
	ILabeledGraphsSet readLabeledGraphsSet(List<String> lines);
}
