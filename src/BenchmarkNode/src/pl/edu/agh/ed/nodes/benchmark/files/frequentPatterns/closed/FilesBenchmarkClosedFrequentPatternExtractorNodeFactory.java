package pl.edu.agh.ed.nodes.benchmark.files.frequentPatterns.closed;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class FilesBenchmarkClosedFrequentPatternExtractorNodeFactory 
        extends NodeFactory<FilesBenchmarkClosedFrequentPatternExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FilesBenchmarkClosedFrequentPatternExtractorNodeModel createNodeModel() {
        return new FilesBenchmarkClosedFrequentPatternExtractorNodeModel();
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
    public NodeView<FilesBenchmarkClosedFrequentPatternExtractorNodeModel> createNodeView(final int viewIndex,
            final FilesBenchmarkClosedFrequentPatternExtractorNodeModel nodeModel) {
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
        return new FilesBenchmarkClosedFrequentPatternExtractorNodeDialog();
    }

}

