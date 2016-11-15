package pl.edu.agh.ed.nodes.frequentPatterns;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class FrequentPatternExtractorNodeFactory 
        extends NodeFactory<FrequentPatternExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FrequentPatternExtractorNodeModel createNodeModel() {
        return new FrequentPatternExtractorNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<FrequentPatternExtractorNodeModel> createNodeView(final int viewIndex,
            final FrequentPatternExtractorNodeModel nodeModel) {
        return new FrequentPatternExtractorNodeView(nodeModel);
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
        return new FrequentPatternExtractorNodeDialog();
    }

}

