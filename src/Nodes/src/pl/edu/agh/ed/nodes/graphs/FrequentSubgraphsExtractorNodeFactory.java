package pl.edu.agh.ed.nodes.graphs;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class FrequentSubgraphsExtractorNodeFactory 
        extends NodeFactory<FrequentSubgraphsExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FrequentSubgraphsExtractorNodeModel createNodeModel() {
        return new FrequentSubgraphsExtractorNodeModel();
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
    public NodeView<FrequentSubgraphsExtractorNodeModel> createNodeView(final int viewIndex,
            final FrequentSubgraphsExtractorNodeModel nodeModel) {
        return new FrequentSubgraphsExtractorNodeView(nodeModel);
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
        return new FrequentSubgraphsExtractorNodeDialog();
    }

}

