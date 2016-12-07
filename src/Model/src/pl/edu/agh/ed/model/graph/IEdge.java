package pl.edu.agh.ed.model.graph;

import java.util.Set;

public interface IEdge extends ILabeledGraphElement {
	boolean isDirected();

	IVertex getStart();

	IVertex getEnd();

	Set<IVertex> getVertices();
}
