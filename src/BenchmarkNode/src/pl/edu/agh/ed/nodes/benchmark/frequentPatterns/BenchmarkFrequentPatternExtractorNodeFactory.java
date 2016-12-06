package pl.edu.agh.ed.nodes.benchmark.frequentPatterns;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class BenchmarkFrequentPatternExtractorNodeFactory 
        extends NodeFactory<BenchmarkFrequentPatternExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BenchmarkFrequentPatternExtractorNodeModel createNodeModel() {
        return new BenchmarkFrequentPatternExtractorNodeModel();
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
    public NodeView<BenchmarkFrequentPatternExtractorNodeModel> createNodeView(final int viewIndex,
            final BenchmarkFrequentPatternExtractorNodeModel nodeModel) {
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
        return new BenchmarkFrequentPatternExtractorNodeDialog();
    }

}

