package pl.edu.agh.ed.model.graph;

import java.util.Set;

public interface ILabeledGraph {
	int getId();

	Set<ILabeledVertex> getVertices();

	Set<ILabeledEdge> getEdges();

	Set<ILabel> getLabels();

	ILabel getLabel(ILabeledGraphElement element);

	int size();
}
