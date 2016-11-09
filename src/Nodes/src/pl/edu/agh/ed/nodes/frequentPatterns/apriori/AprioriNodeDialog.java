package pl.edu.agh.ed.nodes.frequentPatterns.apriori;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
/**
 * <code>NodeDialog</code> for the "Apriori" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class AprioriNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring Apriori node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected AprioriNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentNumber(AprioriNodeModel.SUPPORT_SETTINGS, "Support:", 1, 5));
                    
    }
}

