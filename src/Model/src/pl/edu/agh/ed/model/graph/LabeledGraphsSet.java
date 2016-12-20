package pl.edu.agh.ed.model.graph;

import java.util.Set;

public class LabeledGraphsSet implements ILabeledGraphsSet {

	private Set<ILabeledGraph> transactionGraphs;

	public LabeledGraphsSet(Set<ILabeledGraph> transactionGraphs) {
		this.transactionGraphs = transactionGraphs;
	}

	@Override
	public Set<ILabeledGraph> getTransactionGraphs() {
		return transactionGraphs;
	}

	@Override
	public int size() {
		return transactionGraphs.size();
	}
}
