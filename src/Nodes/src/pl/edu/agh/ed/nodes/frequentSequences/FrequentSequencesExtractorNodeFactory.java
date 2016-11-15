package pl.edu.agh.ed.nodes.frequentSequences;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Apriori" Node.
 * 
 *
 * @author 
 */
public class FrequentSequencesExtractorNodeFactory 
        extends NodeFactory<FrequentSequencesExtractorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FrequentSequencesExtractorNodeModel createNodeModel() {
        return new FrequentSequencesExtractorNodeModel();
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
    public NodeView<FrequentSequencesExtractorNodeModel> createNodeView(final int viewIndex,
            final FrequentSequencesExtractorNodeModel nodeModel) {
        return new FrequentSequencesExtractorNodeView(nodeModel);
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
        return new FrequentSequencesExtractorNodeDialog();
    }

}

