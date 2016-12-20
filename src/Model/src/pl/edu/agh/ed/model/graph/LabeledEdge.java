package pl.edu.agh.ed.model.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LabeledEdge extends AbstractLabeledGraphElement implements ILabeledEdge {
	private final EdgeDirection direction;
	private final ILabeledVertex start;
	private final ILabeledVertex end;

	public LabeledEdge(ILabel label, EdgeDirection direction, ILabeledVertex start, ILabeledVertex end) {
		super(label);
		this.direction = direction;
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean isDirected() {
		return direction == EdgeDirection.DIRECTED;
	}

	@Override
	public ILabeledVertex getStart() {
		return start;
	}

	@Override
	public ILabeledVertex getEnd() {
		return end;
	}

	@Override
	public List<ILabeledVertex> getVertices() {
		List<ILabeledVertex> vertices = new ArrayList<>(2);
		vertices.add(start);
		vertices.add(end);
		return Collections.unmodifiableList(vertices);
	}

	@Override
	public String toString() {
		return "[" + start + (isDirected() ? " -> " : " -- ") + end + "]";
	}
}
