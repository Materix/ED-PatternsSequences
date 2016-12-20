package pl.edu.agh.ed.nodes.benchmark.files.frequentPatterns.closed;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

import pl.edu.agh.ed.algorithm.IClosedFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.aprioriClose.AprioriCloseFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.charm.CharmFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.dCharm.DCharmFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.fpclose.FPCloseFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.lcm.LCMFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.model.IItem;

@SuppressWarnings("unchecked")
public class FilesBenchmarkClosedFrequentPatternExtractorNodeConstans {
	private static final String CFGKEY_IS_RELATIVE = "IS_RELATIVE";

	private static final String CFGKEY_SUPPORT = "SUPPORT";

	private static final String CFGKEY_RELATIVE_SUPPORT = "RELATIVE_SUPPORT";

	private static final String CFGKEY_READ_AS_ORDERED = "READ_AS_ORDERED";

	private static final String CFGKEY_ALGORITHMS = "ALGORITHMS";

	private static final String CFGKEY_FILES_PATH = "FILES_PATH";

	private static final boolean DEFAULT_IS_RELATIVE = false;

	private static final int DEFAULT_SUPPORT = 10;

	private static final double DEFAULT_RELATIVE_SUPPORT = 0.1;

	private static final boolean DEFAULT_ORDERED = false;

	public static final SettingsModelBoolean IS_RELATIVE_SETTINGS = new SettingsModelBoolean(CFGKEY_IS_RELATIVE,
			DEFAULT_IS_RELATIVE);

	public static final SettingsModelIntegerBounded SUPPORT_SETTINGS = new SettingsModelIntegerBounded(CFGKEY_SUPPORT,
			DEFAULT_SUPPORT, 0, Integer.MAX_VALUE);

	public static final SettingsModelDoubleBounded RELATIVE_SUPPORT_SETTINGS = new SettingsModelDoubleBounded(
			CFGKEY_RELATIVE_SUPPORT, DEFAULT_RELATIVE_SUPPORT, 0, 1);

	public static final SettingsModelBoolean READ_AS_ORDERED_SETTINGS = new SettingsModelBoolean(CFGKEY_READ_AS_ORDERED,
			DEFAULT_ORDERED);

	public static final SettingsModelStringArray ALGORITHMS_SETTINGS;

	public static final SettingsModelString FILES_PATH_SETTINGS = new SettingsModelString(CFGKEY_FILES_PATH, "");

	public static final Map<String, Class<? extends IFrequentPatternsExtractor<IItem>>> ALGORITHMS;

	static {
		ChangeListener isRelativeListener = e -> {
			boolean isRelative = FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS
					.getBooleanValue();
			FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.setEnabled(!isRelative);
			FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.setEnabled(isRelative);
		};

		FilesBenchmarkClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS
				.addChangeListener(isRelativeListener);
		isRelativeListener.stateChanged(null);
		ALGORITHMS = new HashMap<>();
		ALGORITHMS.put("AprioriClose",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) AprioriCloseFrequentPatternsClosedExtractor.class);
		ALGORITHMS.put("Charm",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) CharmFrequentPatternsClosedExtractor.class);
		ALGORITHMS.put("dCharm",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) DCharmFrequentPatternsClosedExtractor.class);
		ALGORITHMS.put("FPClose",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) FPCloseFrequentPatternsClosedExtractor.class);
		ALGORITHMS.put("LCM",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) LCMFrequentPatternsClosedExtractor.class);

		ALGORITHMS_SETTINGS = new SettingsModelStringArray(CFGKEY_ALGORITHMS,
				ALGORITHMS.keySet().toArray(new String[0]));

	}

	public static IFrequentPatternsExtractor<IItem> createExtractor(String name) {
		try {
			return ALGORITHMS.get(name).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}