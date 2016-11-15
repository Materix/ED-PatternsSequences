package pl.edu.agh.ed.nodes.transactions.writers;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;

public interface IFrequentPatternSetWriter<T extends IItem> {
	BufferedDataTable write(IFrequentPatternSet<T> frequentPatternSet, BufferedDataContainer dataContainer);
}
