package pl.edu.agh.ed.nodes.frequentPatterns;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.apriori.AprioriFrequentPatternsExtractor;
//import pl.edu.agh.ed.algorithm.apriori.AprioriFrequentPatternsExtractor;
//import pl.edu.agh.ed.algorithm.fpgrowth.FPGrowthFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.eclat.EclatFrequentPatternsExctractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.fin.FINFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.fpgrowth.FPGrowthFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.hmine.HMineFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.lcmfreq.LCMFreqFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.prepost.PrePostFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.prepostplus.PrePostPlusFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.relim.RelimFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

@SuppressWarnings("unchecked")
public class FrequentPatternExtractorNodeConstans {
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

	public static final Map<String, Class<? extends IFrequentPatternsExtractor<IItem>>> ALGORITHM;

	static {
		ChangeListener isRelativeListener = e -> {
			boolean isRelative = FrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.getBooleanValue();
			FrequentPatternExtractorNodeConstans.SUPPORT_SETTINGS.setEnabled(!isRelative);
			FrequentPatternExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.setEnabled(isRelative);
		};

		FrequentPatternExtractorNodeConstans.IS_RELATIVE_SETTINGS.addChangeListener(isRelativeListener);
		isRelativeListener.stateChanged(null);

		ALGORITHM = new HashMap<>();
		ALGORITHM.put("Apriori",
				(Class<? extends IFrequentPatternsExtractor<IItem>>) AprioriFrequentPatternsExtractor.class);
		ALGORITHM.put("FPGrowth",
				(Class<? extends IFrequentPatternsExtractor<IItem>>) FPGrowthFrequentPatternsExtractor.class);
		ALGORITHM.put("Eclat",
				(Class<? extends IFrequentPatternsExtractor<IItem>>) EclatFrequentPatternsExctractor.class);
		ALGORITHM.put("PrePost+",
				(Class<? extends IFrequentPatternsExtractor<IItem>>) PrePostPlusFrequentPatternsExtractor.class);
		ALGORITHM.put("PrePost",
				(Class<? extends IFrequentPatternsExtractor<IItem>>) PrePostFrequentPatternsExtractor.class);
		ALGORITHM.put("FIN", (Class<? extends IFrequentPatternsExtractor<IItem>>) FINFrequentPatternsExtractor.class);
		ALGORITHM.put("H-Mine",
				(Class<? extends IFrequentPatternsExtractor<IItem>>) HMineFrequentPatternsExtractor.class);
		ALGORITHM.put("LCMFreq",
				(Class<? extends IFrequentPatternsExtractor<IItem>>) LCMFreqFrequentPatternsExtractor.class);
		ALGORITHM.put("Relim",
				(Class<? extends IFrequentPatternsExtractor<IItem>>) RelimFrequentPatternsExtractor.class);
	}

	public static IFrequentPatternsExtractor<IItem> createExtractor() {
		try {
			return ALGORITHM.get(ALGORITHM_SETTINGS.getStringValue()).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}