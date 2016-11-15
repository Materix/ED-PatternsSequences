package pl.edu.agh.ed.nodes.frequentPatterns;

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
public class FrequentPatternExtractorNodeDialog extends DefaultNodeSettingsPane {

	/**
     * New pane for configuring Apriori node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected FrequentPatternExtractorNodeDialog() {
        super();        
        addDialogComponent(new DialogComponentBoolean(FrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS, 
        		"Is relative:"));
        addDialogComponent(new DialogComponentNumber(FrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS, 
        		"Support:", 1));
        addDialogComponent(new DialogComponentNumber(FrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS, 
        		"Relative support:", 0.01));
        addDialogComponent(new DialogComponentBoolean(FrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS, 
        		"Read as ordered"));
        addDialogComponent(new DialogComponentStringSelection(FrequentPatternExtractorNodeConstans.ALGORITHM_SETTINGS, 
        		"Algorithm", FrequentPatternExtractorNodeConstans.ALGORITHM.keySet()));
        
    }
}

