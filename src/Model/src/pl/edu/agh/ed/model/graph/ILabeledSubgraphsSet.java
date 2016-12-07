package pl.edu.agh.ed.model.graph;

import java.util.Set;

public interface ILabeledSubgraphsSet {
	ILabeledGraph getGraph();

	Set<ILabeledGraph> getSubgraphs();
}
