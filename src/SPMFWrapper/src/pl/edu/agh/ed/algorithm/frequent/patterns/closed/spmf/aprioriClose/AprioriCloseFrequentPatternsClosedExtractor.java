package pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.aprioriClose;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori_close.AlgoAprioriClose;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.AbstractSPMFClosedFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class AprioriCloseFrequentPatternsClosedExtractor<T extends IItem>
		extends AbstractSPMFClosedFileBasedFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport, int minSupport) throws IOException {
		AlgoAprioriClose algo = new AlgoAprioriClose();
		algo.runAlgorithm(minRelativeSupport, input, output);
	}
}
