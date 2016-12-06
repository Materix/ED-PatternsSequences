package pl.edu.agh.ed.nodes.frequentPatterns.closed;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import pl.edu.agh.ed.algorithm.IClosedFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.aprioriClose.AprioriCloseFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.charm.CharmFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.dCharm.DCharmFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.fpclose.FPCloseFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.lcm.LCMFrequentPatternsClosedExtractor;
import pl.edu.agh.ed.model.IItem;

@SuppressWarnings("unchecked")
public class ClosedFrequentPatternExtractorNodeConstans {
	private static final String CFGKEY_IS_RELATIVE = "IS_RELATIVE";

	private static final String CFGKEY_SUPPORT = "SUPPORT";

	private static final String CFGKEY_RELATIVE_SUPPORT = "RELATIVE_SUPPORT";

	private static final String CFGKEY_READ_AS_ORDERED = "READ_AS_ORDERED";

	private static final String CFGKEY_ALGORITHM = "AGLORITHM";

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

	public static final SettingsModelString ALGORITHM_SETTINGS = new SettingsModelString(CFGKEY_ALGORITHM, "Apriori");

	public static final Map<String, Class<? extends IClosedFrequentPatternsExtractor<IItem>>> ALGORITHM;

	static {
		ChangeListener isRelativeListener = e -> {
			boolean isRelative = ClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.getBooleanValue();
			ClosedFrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.setEnabled(!isRelative);
			ClosedFrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.setEnabled(isRelative);
		};

		ClosedFrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.addChangeListener(isRelativeListener);
		isRelativeListener.stateChanged(null);

		ALGORITHM = new HashMap<>();
		ALGORITHM.put("AprioriClose",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) AprioriCloseFrequentPatternsClosedExtractor.class);
		ALGORITHM.put("Charm",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) CharmFrequentPatternsClosedExtractor.class);
		ALGORITHM.put("dCharm",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) DCharmFrequentPatternsClosedExtractor.class);
		ALGORITHM.put("FPClose",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) FPCloseFrequentPatternsClosedExtractor.class);
		ALGORITHM.put("LCM",
				(Class<? extends IClosedFrequentPatternsExtractor<IItem>>) LCMFrequentPatternsClosedExtractor.class);
	}

	public static IClosedFrequentPatternsExtractor<IItem> createExtractor() {
		try {
			return ALGORITHM.get(ALGORITHM_SETTINGS.getStringValue()).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}