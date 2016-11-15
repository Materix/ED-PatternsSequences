package pl.edu.agh.ed.algorithm.frequent.patterns.spmf.relim;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.relim.AlgoRelim;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.AbstractSPMFFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class RelimFrequentPatternsExtractor<T extends IItem> extends AbstractSPMFFileBasedFrequentPatternsExtractor<T>
		implements IFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport) throws IOException {
		AlgoRelim algoRelim = new AlgoRelim();
		algoRelim.runAlgorithm(minRelativeSupport, input, output);
	}

}
