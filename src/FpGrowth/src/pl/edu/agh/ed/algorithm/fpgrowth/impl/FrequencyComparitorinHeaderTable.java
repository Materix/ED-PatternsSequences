package pl.edu.agh.ed.algorithm.fpgrowth.impl;

import java.util.Comparator;

public class FrequencyComparitorinHeaderTable implements Comparator<FPtree> {
	public int compare(FPtree o1, FPtree o2) {
		if (o1.getCount() > o2.getCount()) {
			return 1;
		} else if (o1.getCount()  < o2.getCount())
			return -1;
		else
			return 0;
	}
}
