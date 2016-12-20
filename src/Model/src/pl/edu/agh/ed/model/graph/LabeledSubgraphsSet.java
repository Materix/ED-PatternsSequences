package pl.edu.agh.ed.model.graph;

import java.util.Set;

public class LabeledSubgraphsSet implements ILabeledSubgraphsSet {
	private final ILabeledGraphsSet graphsSet;

	private final Set<ILabeledGraph> subgraphs;

	public LabeledSubgraphsSet(ILabeledGraphsSet graphsSet, Set<ILabeledGraph> subgraphs) {
		this.graphsSet = graphsSet;
		this.subgraphs = subgraphs;
	}

	@Override
	public ILabeledGraphsSet getGraphsSet() {
		return graphsSet;
	}

	@Override
	public Set<ILabeledGraph> getSubgraphs() {
		return subgraphs;
	}
}
