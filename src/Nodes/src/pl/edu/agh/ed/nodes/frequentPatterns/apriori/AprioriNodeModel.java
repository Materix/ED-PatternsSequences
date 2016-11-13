package pl.edu.agh.ed.nodes.frequentPatterns.apriori;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
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
import pl.edu.agh.ed.algorithm.apriori.AprioriFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransactionSet;
import pl.edu.agh.ed.nodes.transactions.readers.ITransactionSetReader;
import pl.edu.agh.ed.nodes.transactions.readers.impl.OrderedStringTransactionSetReader;
import pl.edu.agh.ed.nodes.transactions.readers.impl.StringTransactionSetReader;
import pl.edu.agh.ed.nodes.transactions.writers.IFrequentPatternSetWriter;
import pl.edu.agh.ed.nodes.transactions.writers.impl.FrequentPatternSetWriter;

/**
 * This is the model implementation of Apriori.
 * 
 *
 * @author 
 */
public class AprioriNodeModel extends NodeModel {
    
    @SuppressWarnings("unused")
	private static final NodeLogger logger = NodeLogger
            .getLogger(AprioriNodeModel.class);
    
    private static final DataTableSpec OUTPUT_DATA_TABLE_SPEC = new DataTableSpec(
    		new DataColumnSpecCreator("Pattern", StringCell.TYPE).createSpec(),
    		new DataColumnSpecCreator("Support", LongCell.TYPE).createSpec(),
    		new DataColumnSpecCreator("Relative support", DoubleCell.TYPE).createSpec()
		);
    
    protected AprioriNodeModel() {
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	ITransactionSetReader<IItem> reader;
    	if (AprioriNodeConstans.READ_AS_ORDERED_SETTINGS.getBooleanValue()) {
    		reader = new OrderedStringTransactionSetReader();
    	} else {
    		reader = new StringTransactionSetReader();
    	}
    	
    	ITransactionSet<IItem> transactionSet = 
			reader.readTransactionSet(StreamSupport.stream(inData[0].spliterator(), false)
    			.map(row -> row.getCell(0))
    			.map(cell -> (StringValue)cell)
    			.map(cell -> cell.getStringValue())
    			.collect(Collectors.toList()));
    	IFrequentPatternsExtractor<IItem> extractor = new AprioriFrequentPatternsExtractor<>();
    	IFrequentPatternSet<IItem> patterns;
    	if (AprioriNodeConstans.IS_RELATIVE_SETTINGS.getBooleanValue()) {
    		patterns = extractor.extract(transactionSet, AprioriNodeConstans.RELATIVE_SUPPORT_SETTINGS.getDoubleValue());
    	} else {
    		patterns = extractor.extract(transactionSet, AprioriNodeConstans.SUPPORT_SETTINGS.getIntValue());
    	}
    	
    	IFrequentPatternSetWriter<IItem> writer = new FrequentPatternSetWriter<>();

    	BufferedDataContainer dataTable = exec.createDataContainer(OUTPUT_DATA_TABLE_SPEC);
        return new BufferedDataTable[]{writer.write(patterns, dataTable)};
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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
    	if (inSpecs.length == 0) {
    		throw new InvalidSettingsException("Missing input");
    	};
    	if (!inSpecs[0].containsCompatibleType(StringValue.class)) {
    		throw new InvalidSettingsException("Missing string value column");
    	};

        return new DataTableSpec[]{OUTPUT_DATA_TABLE_SPEC};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	AprioriNodeConstans.SUPPORT_SETTINGS.saveSettingsTo(settings);
    	AprioriNodeConstans.RELATIVE_SUPPORT_SETTINGS.saveSettingsTo(settings);
    	AprioriNodeConstans.IS_RELATIVE_SETTINGS.saveSettingsTo(settings);
    	AprioriNodeConstans.READ_AS_ORDERED_SETTINGS.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	AprioriNodeConstans.SUPPORT_SETTINGS.loadSettingsFrom(settings);
    	AprioriNodeConstans.RELATIVE_SUPPORT_SETTINGS.loadSettingsFrom(settings);
    	AprioriNodeConstans.IS_RELATIVE_SETTINGS.loadSettingsFrom(settings);
    	AprioriNodeConstans.READ_AS_ORDERED_SETTINGS.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	AprioriNodeConstans.SUPPORT_SETTINGS.validateSettings(settings);
    	AprioriNodeConstans.RELATIVE_SUPPORT_SETTINGS.validateSettings(settings);
    	AprioriNodeConstans.IS_RELATIVE_SETTINGS.validateSettings(settings);
    	AprioriNodeConstans.READ_AS_ORDERED_SETTINGS.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
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
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }

}

