package pl.edu.agh.ed.nodes.transactions.writers.impl;

import java.util.PrimitiveIterator.OfInt;
import java.util.stream.IntStream;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;

import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequence;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.nodes.transactions.writers.IFrequentSequenceSetWriter;

public class FrequentSequenceSetWriter implements IFrequentSequenceSetWriter {
	public BufferedDataTable write(IFrequentSequenceSet frequentSequenceSet, BufferedDataContainer dataContainer) {
		OfInt idGenerator = IntStream.iterate(0, id -> id + 1).iterator();
		for (IFrequentSequence sequence: frequentSequenceSet.getFrequentSequences()) {
			RowKey key = new RowKey(idGenerator.next().toString());
			DataCell[] cells = new DataCell[3];
            cells[0] = new StringCell(formatPattern(sequence));
            cells[1] = new LongCell(sequence.getSupport());
            cells[2] = new DoubleCell(sequence.getRelativeSupport());
            DataRow row = new DefaultRow(key, cells);
            dataContainer.addRowToTable(row);
		}
		dataContainer.close();
		return dataContainer.getTable();
	}
	
	private String formatPattern(IFrequentSequence sequence) {
		return sequence.getGroups().stream()
				.map(group -> group.toString())
				.reduce((s1, s2) -> s1 + " -> " + s2)
				.orElse("");
	}
}
