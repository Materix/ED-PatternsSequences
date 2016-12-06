package pl.edu.agh.ed.nodes.frequentPatterns.closed;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
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
public class ClosedFrequentPatternExtractorNodeDialog extends DefaultNodeSettingsPane {

	/**
     * New pane for configuring Apriori node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected ClosedFrequentPatternExtractorNodeDialog() {
        super();        
        addDialogComponent(new DialogComponentBoolean(ClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS, 
        		"Is relative:"));
        addDialogComponent(new DialogComponentNumber(ClosedFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS, 
        		"Support:", 1));
        addDialogComponent(new DialogComponentNumber(ClosedFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS, 
        		"Relative support:", 0.01));
        addDialogComponent(new DialogComponentBoolean(ClosedFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS, 
        		"Read as ordered"));
        addDialogComponent(new DialogComponentStringSelection(ClosedFrequentPatternExtractorNodeConstans.ALGORITHM_SETTINGS, 
        		"Algorithm", ClosedFrequentPatternExtractorNodeConstans.ALGORITHM.keySet()));
        
    }
}

