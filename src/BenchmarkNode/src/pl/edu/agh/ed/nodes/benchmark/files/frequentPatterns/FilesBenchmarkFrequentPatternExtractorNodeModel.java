package pl.edu.agh.ed.nodes.benchmark.files.frequentPatterns;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.LongCell.LongCellFactory;
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
import org.knime.core.util.FileUtil;

import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.FrequentPatternSet;
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
public class FilesBenchmarkFrequentPatternExtractorNodeModel extends NodeModel {

	@SuppressWarnings("unused")
	private static final NodeLogger logger = NodeLogger
			.getLogger(FilesBenchmarkFrequentPatternExtractorNodeModel.class);

	private DataTableSpec outputDataTableSpecTime = new DataTableSpec();

	private DataTableSpec outputDataTableSpecAmount = new DataTableSpec();

	protected FilesBenchmarkFrequentPatternExtractorNodeModel() {
		super(0, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		List<String> algorithms = Arrays
				.asList(FilesBenchmarkFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS.getStringArrayValue());
		ITransactionSetReader<IItem> reader;
		if (FilesBenchmarkFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.getBooleanValue()) {
			reader = new OrderedStringTransactionSetReader();
		} else {
			reader = new StringTransactionSetReader();
		}
		BufferedDataContainer dataContainerTime = exec.createDataContainer(outputDataTableSpecTime);
		BufferedDataContainer dataContainerAmount = exec.createDataContainer(outputDataTableSpecAmount);

		URL newUrl = FileUtil
				.toURL(FilesBenchmarkFrequentPatternExtractorNodeConstans.FILES_PATH_SETTINGS.getStringValue());
		Path path = FileUtil.resolveToPath(newUrl);
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path, "*.txt")) {
			for (Path filePath : dirStream) {
				RowKey keyTime = new RowKey(filePath.getFileName().toString());
				RowKey keyAmount = new RowKey(filePath.getFileName().toString());
				DataCell[] cellsTime = new DataCell[algorithms.size()];
				DataCell[] cellsAmount = new DataCell[algorithms.size()];
				int i = 0;
				for (String algorithm : algorithms) {
					ITransactionSet<IItem> transactionSet = reader.readTransactionSet(Files.readAllLines(filePath));
					IFrequentPatternsExtractor<IItem> extractor = FilesBenchmarkFrequentPatternExtractorNodeConstans
							.createExtractor(algorithm);
					Function<ITransactionSet<IItem>, IFrequentPatternSet<IItem>> executor;
					if (FilesBenchmarkFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.getBooleanValue()) {
						executor = set -> extractor.extract(set,
								FilesBenchmarkFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS
										.getDoubleValue());
					} else {
						executor = set -> extractor.extract(set,
								FilesBenchmarkFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.getIntValue());
					}
					boolean correctness = true;
					long start = System.currentTimeMillis();
					String message = "";
					IFrequentPatternSet<IItem> result = new FrequentPatternSet<>(transactionSet);
					try {
						result = executor.apply(transactionSet);
					} catch (Throwable e) {
						correctness = false;
					}
					long stop = System.currentTimeMillis();
					if (correctness) {
						cellsTime[i] = LongCellFactory.create(stop - start);
						cellsAmount[i] = IntCellFactory.create(result.size());
					} else {
						cellsTime[i] = LongCellFactory.create(Integer.MAX_VALUE);
						cellsAmount[i] = IntCellFactory.create(0);
					}
					// cells[1] = IntCellFactory.create(result.size());
					// cells[2] = BooleanCellFactory.create(correctness);
					// cells[3] = StringCellFactory.create(message);
					i++;
				}
				DataRow rowTime = new DefaultRow(keyTime, cellsTime);
				dataContainerTime.addRowToTable(rowTime);
				DataRow rowAmount = new DefaultRow(keyAmount, cellsAmount);
				dataContainerAmount.addRowToTable(rowAmount);
				exec.checkCanceled();
			}
		} catch (CanceledExecutionException e) {

		}
		dataContainerTime.close();
		dataContainerAmount.close();
		return new BufferedDataTable[] { dataContainerTime.getTable(), dataContainerAmount.getTable() };
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

		List<String> algorithms = Arrays
				.asList(FilesBenchmarkFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS.getStringArrayValue());
		outputDataTableSpecTime = new DataTableSpec(
				algorithms.stream().map(alg -> new DataColumnSpecCreator(alg, LongCell.TYPE).createSpec())
						.toArray(i -> new DataColumnSpec[i]));
		outputDataTableSpecAmount = new DataTableSpec(
				algorithms.stream().map(alg -> new DataColumnSpecCreator(alg, IntCell.TYPE).createSpec())
						.toArray(i -> new DataColumnSpec[i]));
		return new DataTableSpec[] { outputDataTableSpecTime, outputDataTableSpecAmount };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		FilesBenchmarkFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.saveSettingsTo(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.saveSettingsTo(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.saveSettingsTo(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.saveSettingsTo(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS.saveSettingsTo(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.FILES_PATH_SETTINGS.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		FilesBenchmarkFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.loadSettingsFrom(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.loadSettingsFrom(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.loadSettingsFrom(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.loadSettingsFrom(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS.loadSettingsFrom(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.FILES_PATH_SETTINGS.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		FilesBenchmarkFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.validateSettings(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.validateSettings(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.validateSettings(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS.validateSettings(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS.validateSettings(settings);
		FilesBenchmarkFrequentPatternExtractorNodeConstans.FILES_PATH_SETTINGS.validateSettings(settings);
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
