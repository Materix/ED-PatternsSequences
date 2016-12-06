package pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.lcm;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.lcm.AlgoLCM;
import ca.pfv.spmf.algorithms.frequentpatterns.lcm.Dataset;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.AbstractSPMFClosedFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class LCMFrequentPatternsClosedExtractor<T extends IItem>
		extends AbstractSPMFClosedFileBasedFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport, int minSupport) throws IOException {
		AlgoLCM algo = new AlgoLCM();
		Dataset dataset = new Dataset(input);
		algo.runAlgorithm(minRelativeSupport, dataset, output);
	}
}
