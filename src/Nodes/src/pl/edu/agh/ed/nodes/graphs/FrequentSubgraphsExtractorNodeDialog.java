package pl.edu.agh.ed.nodes.graphs;

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
public class FrequentSubgraphsExtractorNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring Apriori node dialog. This is just a suggestion
	 * to demonstrate possible default dialog components.
	 */
	protected FrequentSubgraphsExtractorNodeDialog() {
		super();
		addDialogComponent(new DialogComponentBoolean(FrequentSubgraphsExtractorNodeConstans.IS_RELATIVE_SETTINGS,
				"Is relative:"));
		addDialogComponent(
				new DialogComponentNumber(FrequentSubgraphsExtractorNodeConstans.SUPPORT_SETTINGS, "Support:", 1));
		addDialogComponent(new DialogComponentNumber(FrequentSubgraphsExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS,
				"Relative support:", 0.01));
		addDialogComponent(new DialogComponentStringSelection(FrequentSubgraphsExtractorNodeConstans.ALGORITHM_SETTINGS,
				"Algorithm", FrequentSubgraphsExtractorNodeConstans.ALGORITHM.keySet()));

	}
}
