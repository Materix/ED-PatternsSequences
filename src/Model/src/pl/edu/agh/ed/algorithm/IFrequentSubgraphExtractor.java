package pl.edu.agh.ed.algorithm;

import pl.edu.agh.ed.model.graph.ILabeledGraphsSet;
import pl.edu.agh.ed.model.graph.ILabeledSubgraphsSet;

public interface IFrequentSubgraphExtractor {
	ILabeledSubgraphsSet extract(ILabeledGraphsSet graphsSet, int minSupport);

	default ILabeledSubgraphsSet extract(ILabeledGraphsSet graphsSet, double minRelativeSupport) {
		return extract(graphsSet, (int) Math.floor(minRelativeSupport * graphsSet.size()));
	};
}
