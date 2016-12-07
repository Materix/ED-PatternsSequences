package pl.edu.agh.ed.model.graph;

import java.util.Set;

public interface IVertex extends ILabeledGraphElement {
	Set<IEdge> getEdges();
}
