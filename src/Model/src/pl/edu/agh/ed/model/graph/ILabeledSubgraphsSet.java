package pl.edu.agh.ed.model.graph;

import java.util.Set;

public interface ILabeledSubgraphsSet {
	ILabeledGraphsSet getGraphsSet();

	Set<ILabeledGraph> getSubgraphs();
}
