package pl.edu.agh.ed.nodes.benchmark.frequentSequences;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

import pl.edu.agh.ed.algorithm.IFrequentSequencesExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.cmspade.CMSPADEFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.cmspam.CMSPAMFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.gsp.GSPFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.lapin.LAPINFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.prefixSpan.PrefixSpanFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.spade.SPADEFrequentSequenceExtractor;
import pl.edu.agh.ed.algorithm.frequent.sequences.spmf.spam.SPAMFrequentSequenceExtractor;

public class BenchmarkFrequentSequencesExtractorNodeConstans {
	private static final String CFGKEY_IS_RELATIVE = "IS_RELATIVE";

	private static final String CFGKEY_SUPPORT = "SUPPORT";

	private static final String CFGKEY_RELATIVE_SUPPORT = "RELATIVE_SUPPORT";

	private static final String CFGKEY_READ_AS_ORDERED = "READ_AS_ORDERED";

	private static final String CFGKEY_ALGORITHMS = "ALGORITHMS";

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

	public static final Map<String, Class<? extends IFrequentSequencesExtractor>> ALGORITHMS;

	static {
		ChangeListener isRelativeListener = e -> {
			boolean isRelative = BenchmarkFrequentSequencesExtractorNodeConstans.IS_RELATIVE_SETTINGS.getBooleanValue();
			BenchmarkFrequentSequencesExtractorNodeConstans.SUPPORT_SETTINGS.setEnabled(!isRelative);
			BenchmarkFrequentSequencesExtractorNodeConstans.RELATIVE_SUPPORT_SETTINGS.setEnabled(isRelative);
		};

		BenchmarkFrequentSequencesExtractorNodeConstans.IS_RELATIVE_SETTINGS.addChangeListener(isRelativeListener);
		isRelativeListener.stateChanged(null);

		ALGORITHMS = new HashMap<>();
		ALGORITHMS.put("GSP", GSPFrequentSequenceExtractor.class);
		ALGORITHMS.put("Lapin", LAPINFrequentSequenceExtractor.class);
		ALGORITHMS.put("PrefixSpan", PrefixSpanFrequentSequenceExtractor.class);
		ALGORITHMS.put("SPAM", SPAMFrequentSequenceExtractor.class);
		ALGORITHMS.put("SPADE", SPADEFrequentSequenceExtractor.class);
		ALGORITHMS.put("CM-SPADE", CMSPAMFrequentSequenceExtractor.class);
		ALGORITHMS.put("CM-SPAM", CMSPADEFrequentSequenceExtractor.class);

		ALGORITHMS_SETTINGS = new SettingsModelStringArray(CFGKEY_ALGORITHMS,
				ALGORITHMS.keySet().toArray(new String[0]));

	}

	public static IFrequentSequencesExtractor createExtractor(String name) {
		try {
			return ALGORITHMS.get(name).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}