package pl.edu.agh.ed.nodes.transactions.readers;

import java.util.stream.IntStream;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;

import pl.edu.agh.ed.model.patterns.IFrequentPattern;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;

public class TableFrequentPatternSetWriter {
	public BufferedDataTable write(IFrequentPatternSet frequentPatternSet, BufferedDataContainer dataContainer) {
		idGenerator = IntStream.iterate(0, id -> id + 1);
		for (IFrequentPattern pattern: frequentPatternSet.getFrequentPatterns()) {
			DataRow row = new Defa;
			pattern
		}
		
		for (int i = 0; i < m_count.getIntValue(); i++) {
            RowKey key = new RowKey("Row " + i);
            // the cells of the current row, the types of the cells must match
            // the column spec (see above)
            DataCell[] cells = new DataCell[3];
            cells[0] = new StringCell("String_" + i); 
            cells[1] = new DoubleCell(0.5 * i); 
            cells[2] = new IntCell(i);
            DataRow row = new DefaultRow(key, cells);
            container.addRowToTable(row);
            
            // check if the execution monitor was canceled
            exec.checkCanceled();
            exec.setProgress(i / (double)m_count.getIntValue(), 
                "Adding row " + i);
        }
		return dataContainer.getTable();
	}
}
