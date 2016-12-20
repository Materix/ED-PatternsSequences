package pl.edu.agh.ed.nodes.graphs;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import pl.edu.agh.ed.algorithm.IFrequentSubgraphExtractor;
import pl.edu.agh.ed.algorithm.frequent.graphs.parsemis.gspan.GSpanFrequentSubgraphExtractor;

public class FrequentSubgraphsExtractorNodeConstans {
	private static final String CFGKEY_IS_RELATIVE = "IS_RELATIVE";

	private static final String CFGKEY_SUPPORT = "SUPPORT";

	private static final String CFGKEY_RELATIVE_SUPPORT = "RELATIVE_SUPPORT";

	private static final String CFGKEY_ALGORITHM = "AGLORITHM";

	private static final boolean DEFAULT_IS_RELATIVE = false;

	private static final int DEFAULT_SUPPORT = 10;

	private static final double DEFAULT_RELATIVE_SUPPORT = 0.1;

	public static final SettingsModelBoolean IS_RELATIVE_SETTINGS = new SettingsModelBoolean(CFGKEY_IS_RELATIVE,
			DEFAULT_IS_RELATIVE);

	public static final SettingsModelIntegerBounded SUPPORT_SETTINGS = new SettingsModelIntegerBounded(CFGKEY_SUPPORT,
			DEFAULT_SUPPORT, 0, Integer.MAX_VALUE);

	public static final SettingsModelDoubleBounded RELATIVE_SUPPORT_SETTINGS = new SettingsModelDoubleBounded(
			CFGKEY_RELATIVE_SUPPORT, DEFAULT_RELATIVE_SUPPORT, 0, 1);

	public static final SettingsModelString ALGORITHM_SETTINGS = new SettingsModelString(CFGKEY_ALGORITHM, "gSpan");

	public static final Map<String, Class<? extends IFrequentSubgraphExtractor>> ALGORITHM;

	static {
		ChangeListener isRelativeListener = e -> {
			boolean isRelative = FrequentSubgraphsExtractorNodeConstans.IS_RELATIVE_SETTINGS.getBooleanValue();
			FrequentSubgraphsExtractorNodeConstans.SUPPORT_SETTINGS.setEnabled(!isRelative);
			FrequentSubgraphsExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.setEnabled(isRelative);
		};

		FrequentSubgraphsExtractorNodeConstans.IS_RELATIVE_SETTINGS.addChangeListener(isRelativeListener);
		isRelativeListener.stateChanged(null);

		ALGORITHM = new HashMap<>();
		ALGORITHM.put("gSpan", GSpanFrequentSubgraphExtractor.class);
	}

	public static IFrequentSubgraphExtractor createExtractor() {
		try {
			return ALGORITHM.get(ALGORITHM_SETTINGS.getStringValue()).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}