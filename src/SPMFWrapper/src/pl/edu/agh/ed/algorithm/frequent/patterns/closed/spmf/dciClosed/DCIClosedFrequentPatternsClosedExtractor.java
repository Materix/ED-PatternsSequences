package pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.dciClosed;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.dci_closed.AlgoDCI_Closed;
import pl.edu.agh.ed.algorithm.frequent.patterns.closed.spmf.AbstractSPMFClosedFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class DCIClosedFrequentPatternsClosedExtractor<T extends IItem>
		extends AbstractSPMFClosedFileBasedFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport, int minSupport) throws IOException {
		AlgoDCI_Closed algoFPGrowth = new AlgoDCI_Closed();
		algoFPGrowth.runAlgorithm(input, output, minSupport);
	}

}
