package pl.edu.agh.ed.algorithm.frequent.patterns.spmf.hmine;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.hmine.AlgoHMine;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.AbstractSPMFFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class HMineFrequentPatternsExtractor<T extends IItem> extends AbstractSPMFFileBasedFrequentPatternsExtractor<T>
		implements IFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport) throws IOException {
		AlgoHMine algoHMine = new AlgoHMine();
		algoHMine.runAlgorithm(input, output, minRelativeSupport);
	}

}
