package pl.edu.agh.ed.nodes.benchmark.frequentSequences.closed;

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
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.StringCell.StringCellFactory;
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

import pl.edu.agh.ed.algorithm.IFrequentSequencesExtractor;
import pl.edu.agh.ed.model.frequent.sequence.FrequentSequenceSet;
import pl.edu.agh.ed.model.frequent.sequence.IFrequentSequenceSet;
import pl.edu.agh.ed.model.sequence.ISequenceSet;
import pl.edu.agh.ed.nodes.frequentSequences.FrequentSequencesExtractorNodeConstans;
import pl.edu.agh.ed.nodes.transactions.readers.ISequenceSetReader;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringSequenceSetReader;
import pl.edu.agh.ed.nodes.transactions.readers.impl.StringSequenceSetReader;

/**
 * This is the model implementation of Apriori.
 * 
 *
 * @author
 */
public class BenchmarkClosedFrequentSequencesExtractorNodeModel extends NodeModel {

	@SuppressWarnings("unused")
	private static final NodeLogger logger = NodeLogger
			.getLogger(BenchmarkClosedFrequentSequencesExtractorNodeModel.class);

	private static final DataTableSpec OUTPUT_DATA_TABLE_SPEC = new DataTableSpec(
			new DataColumnSpecCreator("Execution time", DateAndTimeCell.TYPE).createSpec(),
			new DataColumnSpecCreator("Pattern count", IntCell.TYPE).createSpec(),
			new DataColumnSpecCreator("Correctness", BooleanCell.TYPE).createSpec(),
			new DataColumnSpecCreator("Message", StringCell.TYPE).createSpec());

	protected BenchmarkClosedFrequentSequencesExtractorNodeModel() {
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		List<String> algorithms = Arrays.asList(
				BenchmarkClosedFrequentSequencesExtractorNodeConstans.ALGORITHMS_SETTINGS.getStringArrayValue());
		ISequenceSetReader reader;
		if (FrequentSequencesExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.getBooleanValue()) {
			reader = new OrderedStringSequenceSetReader();
		} else {
			reader = new StringSequenceSetReader();
		}
		BufferedDataContainer dataContainer = exec.createDataContainer(OUTPUT_DATA_TABLE_SPEC);

		for (String algorithm : algorithms) {
			ISequenceSet sequenceSet = reader.readSequenceSet(StreamSupport.stream(inData[0].spliterator(), false)
					.map(row -> row.getCell(0)).map(cell -> (StringValue) cell).map(cell -> cell.getStringValue())
					.collect(Collectors.toList()));
			IFrequentSequencesExtractor extractor = BenchmarkClosedFrequentSequencesExtractorNodeConstans
					.createExtractor(algorithm);
			Function<ISequenceSet, IFrequentSequenceSet> executor;
			if (BenchmarkClosedFrequentSequencesExtractorNodeConstans.IS_RELATIVE_SETTINGS.getBooleanValue()) {
				executor = set -> extractor.extract(set,
						BenchmarkClosedFrequentSequencesExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS
								.getDoubleValue());
			} else {
				executor = set -> extractor.extract(set,
						BenchmarkClosedFrequentSequencesExtractorNodeConstans.SUPPORT_SETTINGS.getIntValue());
			}
			boolean correctness = true;
			long start = System.currentTimeMillis();
			String message = "";
			IFrequentSequenceSet result = new FrequentSequenceSet(sequenceSet);
			try {
				result = executor.apply(sequenceSet);
			} catch (Throwable e) {
				correctness = false;
			}
			long stop = System.currentTimeMillis();

			RowKey key = new RowKey(algorithm);
			DataCell[] cells = new DataCell[4];
			cells[0] = new DateAndTimeCell(stop - start, false, true, true);
			cells[1] = IntCellFactory.create(result.size());
			cells[2] = BooleanCellFactory.create(correctness);
			cells[3] = StringCellFactory.create(message);
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
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.SUPPORT_SETTINGS.saveSettingsTo(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.saveSettingsTo(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.IS_RELATIVE_SETTINGS.saveSettingsTo(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.saveSettingsTo(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.ALGORITHMS_SETTINGS.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.SUPPORT_SETTINGS.loadSettingsFrom(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.loadSettingsFrom(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.IS_RELATIVE_SETTINGS.loadSettingsFrom(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.loadSettingsFrom(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.ALGORITHMS_SETTINGS.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.SUPPORT_SETTINGS.validateSettings(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.validateSettings(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.IS_RELATIVE_SETTINGS.validateSettings(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.validateSettings(settings);
		BenchmarkClosedFrequentSequencesExtractorNodeConstans.ALGORITHMS_SETTINGS.validateSettings(settings);
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
