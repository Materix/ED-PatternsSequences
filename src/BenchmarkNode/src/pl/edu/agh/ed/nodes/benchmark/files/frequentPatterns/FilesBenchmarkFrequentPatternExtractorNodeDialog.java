package pl.edu.agh.ed.nodes.benchmark.files.frequentPatterns;

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
public class FilesBenchmarkFrequentPatternExtractorNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring Apriori node dialog. This is just a suggestion
	 * to demonstrate possible default dialog components.
	 */
	protected FilesBenchmarkFrequentPatternExtractorNodeDialog() {
		super();
		addDialogComponent(new DialogComponentBoolean(
				FilesBenchmarkFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS, "Is relative:"));
		addDialogComponent(new DialogComponentNumber(FilesBenchmarkFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS,
				"Support:", 1));
		addDialogComponent(new DialogComponentNumber(
				FilesBenchmarkFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS, "Relative support:", 0.01));
		addDialogComponent(new DialogComponentBoolean(
				FilesBenchmarkFrequentPatternExtractorNodeConstans.READ_AS_ORDERED_SETTINGS, "Read as ordered"));
		addDialogComponent(
				new DialogComponentFileChooser(FilesBenchmarkFrequentPatternExtractorNodeConstans.FILES_PATH_SETTINGS,
						"files.frequentPattern", JFileChooser.OPEN_DIALOG, true));
		addDialogComponent(new DialogComponentStringListSelection(
				FilesBenchmarkFrequentPatternExtractorNodeConstans.ALGORITHMS_SETTINGS, "Algorithms",
				FilesBenchmarkFrequentPatternExtractorNodeConstans.ALGORITHMS.keySet(), true,
				FilesBenchmarkFrequentPatternExtractorNodeConstans.ALGORITHMS.size()));

	}
}
