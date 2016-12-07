package pl.edu.agh.ed.algorithm;

import pl.edu.agh.ed.model.graph.ILabeledGraph;
import pl.edu.agh.ed.model.graph.ILabeledSubgraphsSet;

public interface IFrequentSubgraphExtractor {
	ILabeledSubgraphsSet extract(ILabeledGraph sequenceSet, int minSupport);

	default ILabeledSubgraphsSet extract(ILabeledGraph sequenceSet, double minRelativeSupport) {
		return extract(sequenceSet, (int) Math.floor(minRelativeSupport * sequenceSet.size()));
	};
}
