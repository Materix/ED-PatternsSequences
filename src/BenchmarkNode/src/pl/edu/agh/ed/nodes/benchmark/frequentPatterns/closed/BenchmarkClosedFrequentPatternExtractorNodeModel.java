package pl.edu.agh.ed.nodes.benchmark.frequentPatterns.closed;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.BooleanCell.BooleanCellFactory;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;
import pl.edu.agh.ed.nodes.transactions.readers.ITransactionSetReader;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringTransactionSetReader;
import pl.edu.agh.ed.nodes.transactions.readers.impl.StringTransactionSetReader;

/**
 * This is the model implementation of Apriori.
 * 
 *
 * @author
 */
public class BenchmarkClosedFrequentPatternExtractorNodeModel extends NodeModel {

	@SuppressWarnings("unused")
	private static final NodeLogger logger = NodeLogger.getLogger(BenchmarkClosedFrequentPatternExtractorNodeModel.class);

	private static final DataTableSpec OUTPUT_DATA_TABLE_SPEC = new DataTableSpec(
			new DataColumnSpecCreator("Execution time", DateAndTimeCell.TYPE).createSpec(),
			new DataColumnSpecCreator("Correctness", BooleanCell.TYPE).createSpec());

	protected BenchmarkClosedFrequentPatternExtractorNodeModel() {
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		List<String> algorithms = Arrays
				.asList(BenchmarkClosedFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS.getStringArrayValue());
		ITransactionSetReader<IItem> reader;
		if (BenchmarkClosedFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.getBooleanValue()) {
			reader = new OrderedStringTransactionSetReader();
		} else {
			reader = new StringTransactionSetReader();
		}
		BufferedDataContainer dataContainer = exec.createDataContainer(OUTPUT_DATA_TABLE_SPEC);

		for (String algorithm : algorithms) {
			ITransactionSet<IItem> transactionSet = reader.readTransactionSet(StreamSupport
					.stream(inData[0].spliterator(), false).map(row -> row.getCell(0)).map(cell -> (StringValue) cell)
					.map(cell -> cell.getStringValue()).collect(Collectors.toList()));
			IFrequentPatternsExtractor<IItem> extractor = BenchmarkClosedFrequentPatternExtractorNodeConstans
					.createExtractor(algorithm);
			Function<ITransactionSet<IItem>, IFrequentPatternSet<IItem>> executor;
			if (BenchmarkClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.getBooleanValue()) {
				executor = set -> extractor.extract(set,
						BenchmarkClosedFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.getDoubleValue());
			} else {
				executor = set -> extractor.extract(set,
						BenchmarkClosedFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.getIntValue());
			}
			long start = System.currentTimeMillis();
			IFrequentPatternSet<IItem> result = executor.apply(transactionSet);
			long stop = System.currentTimeMillis();

			RowKey key = new RowKey(algorithm);
			DataCell[] cells = new DataCell[2];
			cells[0] = new DateAndTimeCell(stop - start, false, true, true);
			cells[1] = BooleanCellFactory.create(true);
			DataRow row = new DefaultRow(key, cells);
			dataContainer.addRowToTable(row);
		}

		dataContainer.close();
		return new BufferedDataTable[] { dataContainer.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// TODO Code executed on reset.
		// Models build during execute are cleared here.
		// Also data handled in load/saveInternals will be erased here.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		// if (inSpecs.length != 2) {
		// throw new InvalidSettingsException("Missing input");
		// }
		// if (!inSpecs[0].containsCompatibleType(StringValue.class)) {
		// throw new InvalidSettingsException("Missing string value column in
		// transactions");
		// }
		// if (!inSpecs[1].containsCompatibleType(StringValue.class)) {
		// throw new InvalidSettingsException("Missing string value column in
		// transactions");
		// }

		return new DataTableSpec[] { OUTPUT_DATA_TABLE_SPEC };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		BenchmarkClosedFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.saveSettingsTo(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.saveSettingsTo(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.saveSettingsTo(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.saveSettingsTo(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		BenchmarkClosedFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.loadSettingsFrom(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.loadSettingsFrom(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.loadSettingsFrom(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.loadSettingsFrom(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		BenchmarkClosedFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.validateSettings(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.validateSettings(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.validateSettings(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.validateSettings(settings);
		BenchmarkClosedFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		// TODO load internal data.
		// Everything handed to output ports is loaded automatically (data
		// returned by the execute method, models loaded in loadModelContent,
		// and user settings set through loadSettingsFrom - is all taken care
		// of). Load here only the other internals that need to be restored
		// (e.g. data used by the views).

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		// TODO save internal models.
		// Everything written to output ports is saved automatically (data
		// returned by the execute method, models saved in the saveModelContent,
		// and user settings saved through saveSettingsTo - is all taken care
		// of). Save here only the other internals that need to be preserved
		// (e.g. data used by the views).

	}

}
