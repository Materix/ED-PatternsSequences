package pl.edu.agh.ed.nodes.closedFrequentPatterns;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class ClosedFrequentPatternExtractorNodeFactory 
        extends NodeFactory<ClosedFrequentPatternExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ClosedFrequentPatternExtractorNodeModel createNodeModel() {
        return new ClosedFrequentPatternExtractorNodeModel();
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
    public NodeView<ClosedFrequentPatternExtractorNodeModel> createNodeView(final int viewIndex,
            final ClosedFrequentPatternExtractorNodeModel nodeModel) {
        return new ClosedFrequentPatternExtractorNodeView(nodeModel);
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
        return new ClosedFrequentPatternExtractorNodeDialog();
    }

}

