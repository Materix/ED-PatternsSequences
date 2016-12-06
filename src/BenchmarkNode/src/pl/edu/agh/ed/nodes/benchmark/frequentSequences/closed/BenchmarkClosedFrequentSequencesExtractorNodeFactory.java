package pl.edu.agh.ed.nodes.benchmark.frequentSequences.closed;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class BenchmarkClosedFrequentSequencesExtractorNodeFactory 
        extends NodeFactory<BenchmarkClosedFrequentSequencesExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BenchmarkClosedFrequentSequencesExtractorNodeModel createNodeModel() {
        return new BenchmarkClosedFrequentSequencesExtractorNodeModel();
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
    public NodeView<BenchmarkClosedFrequentSequencesExtractorNodeModel> createNodeView(final int viewIndex,
            final BenchmarkClosedFrequentSequencesExtractorNodeModel nodeModel) {
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
        return new BenchmarkClosedFrequentSequencesExtractorNodeDialog();
    }

}

