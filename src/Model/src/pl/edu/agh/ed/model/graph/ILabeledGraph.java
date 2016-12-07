package pl.edu.agh.ed.model.graph;

import java.util.Set;

public interface ILabeledGraph {
	Set<IVertex> getVertices();

	Set<IEdge> getEdges();

	Set<ILabel> getLabels();

	ILabel getLabel(ILabeledGraphElement element);

	int size();
}
