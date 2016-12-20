package pl.edu.agh.ed.model.graph;

import java.util.List;

public interface ILabeledEdge extends ILabeledGraphElement {
	boolean isDirected();

	ILabeledVertex getStart();

	ILabeledVertex getEnd();

	List<ILabeledVertex> getVertices();
}
