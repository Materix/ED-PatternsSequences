package pl.edu.agh.ed.model.graph;

import java.util.HashSet;
import java.util.Set;

public class LabeledVertex extends AbstractLabeledGraphElement implements ILabeledVertex {

	private final Set<ILabeledEdge> edges;

	public LabeledVertex(ILabel label) {
		this(label, new HashSet<>());
	}

	public LabeledVertex(ILabel label, Set<ILabeledEdge> edges) {
		super(label);
		this.edges = edges;
	}

	@Override
	public Set<ILabeledEdge> getEdges() {
		return edges;
	}

	@Override
	public void addEdge(ILabeledEdge edge) {
		edges.add(edge);
	}

	@Override
	public String toString() {
		return getLabel().toString();
	}
}
