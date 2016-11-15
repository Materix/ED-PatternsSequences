package pl.edu.agh.ed.algorithm.frequent.patterns.spmf.fin;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.fin_prepost.FIN;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.AbstractSPMFFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class FINFrequentPatternsExtractor<T extends IItem> extends AbstractSPMFFileBasedFrequentPatternsExtractor<T>
		implements IFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport) throws IOException {
		FIN fin = new FIN();
		fin.runAlgorithm(input, minRelativeSupport, output);
	}

}
