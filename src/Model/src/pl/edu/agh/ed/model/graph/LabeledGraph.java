package pl.edu.agh.ed.model.graph;

import java.util.HashSet;
import java.util.Set;

public class LabeledGraph implements ILabeledGraph {
	private final int id;

	private final Set<ILabeledVertex> vertices;

	private final Set<ILabeledEdge> edges;

	private final Set<ILabel> labels;

	public LabeledGraph(int id, Set<ILabeledVertex> vertices, Set<ILabeledEdge> edges, Set<ILabel> labels) {
		this.id = id;
		this.vertices = vertices;
		this.edges = edges;
		this.labels = labels;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Set<ILabeledVertex> getVertices() {
		return vertices;
	}

	@Override
	public Set<ILabeledEdge> getEdges() {
		return edges;
	}

	@Override
	public Set<ILabel> getLabels() {
		return labels;
	}

	@Override
	public ILabel getLabel(ILabeledGraphElement element) {
		return element.getLabel();
	}

	@Override
	public int size() {
		return vertices.size();
	}

	public static class LabeledGraphBuilder {
		private Set<ILabeledVertex> vertices;

		private Set<ILabeledEdge> edges;

		private Set<ILabel> labels;

		public LabeledGraphBuilder() {
			vertices = new HashSet<>();
			edges = new HashSet<>();
			labels = new HashSet<>();
		}

		public LabeledGraphBuilder add(ILabeledVertex vertex) {
			vertices.add(vertex);
			return this;
		}

		public LabeledGraphBuilder add(ILabel label) {
			labels.add(label);
			return this;
		}

		public LabeledGraphBuilder add(ILabeledEdge edge) {
			edges.add(edge);
			return this;
		}

		public ILabeledGraph build(int id) {
			LabeledGraph graph = new LabeledGraph(id, vertices, edges, labels);
			vertices = new HashSet<>();
			edges = new HashSet<>();
			labels = new HashSet<>();
			return graph;
		}

		public boolean isEmpty() {
			return vertices.isEmpty() && edges.isEmpty() && labels.isEmpty();
		}
	}
}
