package pl.edu.agh.ed.nodes.frequentSequences.closed;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class ClosedFrequentSequencesExtractorNodeFactory 
        extends NodeFactory<ClosedFrequentSequencesExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ClosedFrequentSequencesExtractorNodeModel createNodeModel() {
        return new ClosedFrequentSequencesExtractorNodeModel();
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
    public NodeView<ClosedFrequentSequencesExtractorNodeModel> createNodeView(final int viewIndex,
            final ClosedFrequentSequencesExtractorNodeModel nodeModel) {
        return new ClosedFrequentSequencesExtractorNodeView(nodeModel);
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
        return new ClosedFrequentSequencesExtractorNodeDialog();
    }

}

