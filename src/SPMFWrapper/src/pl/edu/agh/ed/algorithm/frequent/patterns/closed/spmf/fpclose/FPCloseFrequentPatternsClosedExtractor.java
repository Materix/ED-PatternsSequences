package pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.fpclose;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPClose;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.AbstractSPMFClosedFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class FPCloseFrequentPatternsClosedExtractor<T extends IItem>
		extends AbstractSPMFClosedFileBasedFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport, int minSupport) throws IOException {
		AlgoFPClose algoFPGrowth = new AlgoFPClose();
		algoFPGrowth.runAlgorithm(input, output, minRelativeSupport);
	}

}
