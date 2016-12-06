package pl.edu.agh.ed.nodes.benchmark.frequentSequences;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class BenchmarkFrequentSequencesExtractorNodeFactory 
        extends NodeFactory<BenchmarkFrequentSequencesExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BenchmarkFrequentSequencesExtractorNodeModel createNodeModel() {
        return new BenchmarkFrequentSequencesExtractorNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<BenchmarkFrequentSequencesExtractorNodeModel> createNodeView(final int viewIndex,
            final BenchmarkFrequentSequencesExtractorNodeModel nodeModel) {
        throw new IndexOutOfBoundsException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new BenchmarkFrequentSequencesExtractorNodeDialog();
    }

}

