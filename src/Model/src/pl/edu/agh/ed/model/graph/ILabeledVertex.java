package pl.edu.agh.ed.model.graph;

import java.util.Set;

public interface ILabeledVertex extends ILabeledGraphElement {
	Set<ILabeledEdge> getEdges();

	void addEdge(ILabeledEdge edge);
}
