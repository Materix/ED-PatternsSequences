package pl.edu.agh.ed.nodes.benchmark.frequentPatterns.closed;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class BenchmarkClosedFrequentPatternExtractorNodeFactory 
        extends NodeFactory<BenchmarkClosedFrequentPatternExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BenchmarkClosedFrequentPatternExtractorNodeModel createNodeModel() {
        return new BenchmarkClosedFrequentPatternExtractorNodeModel();
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
    public NodeView<BenchmarkClosedFrequentPatternExtractorNodeModel> createNodeView(final int viewIndex,
            final BenchmarkClosedFrequentPatternExtractorNodeModel nodeModel) {
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
        return new BenchmarkClosedFrequentPatternExtractorNodeDialog();
    }

}

