package pl.edu.agh.ed.algorithm.frequent.patterns.spmf.lcmfreq;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.lcm.AlgoLCM;
import ca.pfv.spmf.algorithms.frequentpatterns.lcm.Dataset;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.AbstractSPMFFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class LCMFreqFrequentPatternsExtractor<T extends IItem> extends AbstractSPMFFileBasedFrequentPatternsExtractor<T>
		implements IFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport) throws IOException {
		AlgoLCM algoLCM = new AlgoLCM();
		algoLCM.runAlgorithm(minRelativeSupport, new Dataset(input), output);
		
	}

}
