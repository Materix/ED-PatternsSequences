package pl.edu.agh.ed.algorithm.frequent.patterns.spmf.prepost;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.fin_prepost.PrePost;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.AbstractSPMFFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class PrePostFrequentPatternsExtractor<T extends IItem> extends AbstractSPMFFileBasedFrequentPatternsExtractor<T>
		implements IFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport) throws IOException {
		PrePost prePost = new PrePost();
		prePost.setUsePrePostPlus(false);
		prePost.runAlgorithm(input, minRelativeSupport, output);
	}

}
