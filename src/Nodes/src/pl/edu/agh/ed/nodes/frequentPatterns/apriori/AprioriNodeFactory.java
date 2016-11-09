package pl.edu.agh.ed.nodes.frequentPatterns.apriori;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class AprioriNodeFactory 
        extends NodeFactory<AprioriNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AprioriNodeModel createNodeModel() {
        return new AprioriNodeModel();
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
    public NodeView<AprioriNodeModel> createNodeView(final int viewIndex,
            final AprioriNodeModel nodeModel) {
        return new AprioriNodeView(nodeModel);
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
        return new AprioriNodeDialog();
    }

}

