package pl.edu.agh.ed.model.graph;

import java.util.Set;

public interface ILabeledGraphsSet {
	Set<ILabeledGraph> getTransactionGraphs();

	int size();
}
