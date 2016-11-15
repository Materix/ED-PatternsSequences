package pl.edu.agh.ed.algorithm.frequent.patterns.spmf.fpgrowth;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.AbstractSPMFFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class FPGrowthFrequentPatternsExtractor<T extends IItem> extends AbstractSPMFFileBasedFrequentPatternsExtractor<T>
		implements IFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport) throws IOException {
		AlgoFPGrowth algoFPGrowth = new AlgoFPGrowth();
		algoFPGrowth.runAlgorithm(input, output, minRelativeSupport);
	}

}
