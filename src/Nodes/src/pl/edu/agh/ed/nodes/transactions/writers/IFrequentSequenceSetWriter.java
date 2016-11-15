package pl.edu.agh.ed.nodes.transactions.writers;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;

import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;

public interface IFrequentSequenceSetWriter {
	BufferedDataTable write(IFrequentSequenceSet frequentSequenceSet, BufferedDataContainer dataContainer);
}
