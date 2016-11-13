package pl.edu.agh.ed.algorithm;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.associationRules.IAssociationRuleSet;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;

public interface IAssociationRulesExtractor<T extends IItem> {
	IAssociationRuleSet<T> extract(IFrequentPatternSet<T> frequentPatternSet);
}
