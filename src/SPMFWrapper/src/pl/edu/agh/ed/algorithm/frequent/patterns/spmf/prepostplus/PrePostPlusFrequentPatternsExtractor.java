package pl.edu.agh.ed.algorithm.frequent.patterns.spmf.prepostplus;

import java.io.IOException;

import ca.pfv.spmf.algorithms.frequentpatterns.fin_prepost.PrePost;
import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.algorithm.frequent.patterns.spmf.AbstractSPMFFileBasedFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;

public class PrePostPlusFrequentPatternsExtractor<T extends IItem> extends AbstractSPMFFileBasedFrequentPatternsExtractor<T>
		implements IFrequentPatternsExtractor<T> {

	@Override
	public void extract(String input, String output, double minRelativeSupport) throws IOException {
		PrePost prePost = new PrePost();
		prePost.setUsePrePostPlus(true);
		prePost.runAlgorithm(input, minRelativeSupport, output);
	}

}
