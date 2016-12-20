package pl.edu.agh.ed.nodes.benchmark.files.frequentPatterns;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class FilesBenchmarkFrequentPatternExtractorNodeFactory 
        extends NodeFactory<FilesBenchmarkFrequentPatternExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FilesBenchmarkFrequentPatternExtractorNodeModel createNodeModel() {
        return new FilesBenchmarkFrequentPatternExtractorNodeModel();
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
    public NodeView<FilesBenchmarkFrequentPatternExtractorNodeModel> createNodeView(final int viewIndex,
            final FilesBenchmarkFrequentPatternExtractorNodeModel nodeModel) {
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
        return new FilesBenchmarkFrequentPatternExtractorNodeDialog();
    }

}

