package pl.edu.agh.ed.utils.runners.kosarak;

import java.util.function.Supplier;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.frequent.patterns.IFrequentPatternSet;

public class SupplierFrequentPatternSet implements Supplier<Integer> {

	private final IFrequentPatternSet<IItem> frequentPatterns;

	public SupplierFrequentPatternSet(IFrequentPatternSet<IItem> frequentPatterns) {
		this.frequentPatterns = frequentPatterns;
	}

	@Override
	public Integer get() {
		return getFrequentPatterns().size();
	}

	public IFrequentPatternSet<IItem> getFrequentPatterns() {
		return frequentPatterns;
	}

}