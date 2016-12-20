package pl.edu.agh.ed.nodes.benchmark.files.frequentPatterns.closed;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;

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
public class FilesBenchmarkClosedFrequentPatternExtractorNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring Apriori node dialog. This is just a suggestion
	 * to demonstrate possible default dialog components.
	 */
	protected FilesBenchmarkClosedFrequentPatternExtractorNodeDialog() {
		super();
		addDialogComponent(new DialogComponentBoolean(
				FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS, "Is relative:"));
		addDialogComponent(new DialogComponentNumber(FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS,
				"Support:", 1));
		addDialogComponent(new DialogComponentNumber(
				FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS, "Relative support:", 0.01));
		addDialogComponent(new DialogComponentBoolean(
				FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS, "Read as ordered"));
		addDialogComponent(
				new DialogComponentFileChooser(FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.FILES_PATH_SETTINGS,
						"files.frequentPattern", JFileChooser.OPEN_DIALOG, true));
		addDialogComponent(new DialogComponentStringListSelection(
				FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS, "Algorithms",
				FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.ALGORITHMS.keySet(), true,
				FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.ALGORITHMS.size()));

	}
}
