package pl.edu.agh.ed.nodes.transactions.writers;

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

import pl.edu.agh.ed.model.patterns.IFrequentPattern;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;

public class TableFrequentPatternSetWriter {
	public BufferedDataTable write(IFrequentPatternSet frequentPatternSet, BufferedDataContainer dataContainer) {
		OfInt idGenerator = IntStream.iterate(0, id -> id + 1).iterator();
		for (IFrequentPattern pattern: frequentPatternSet.getFrequentPatterns()) {
			RowKey key = new RowKey(idGenerator.next().toString());
			DataCell[] cells = new DataCell[3];
            cells[0] = new StringCell(formatPattern(pattern));
            cells[1] = new LongCell(pattern.getSupport());
            cells[2] = new DoubleCell(pattern.getNormalizedSupport());
            DataRow row = new DefaultRow(key, cells);
            dataContainer.addRowToTable(row);
		}
		dataContainer.close();
		return dataContainer.getTable();
	}
	
	private String formatPattern(IFrequentPattern pattern) {
		return pattern.getItems().stream()
				.map(item -> item.toString())
				.reduce((s1, s2) -> s1 + " " + s2)
				.orElse("");
	}
}
