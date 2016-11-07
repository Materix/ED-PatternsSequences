package pl.edu.agh.ed.algorithm;

import pl.edu.agh.ed.model.associationRules.IAssociationRuleSet;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;

public interface IAssociationRulesExtractor {
	IAssociationRuleSet extract(IFrequentPatternSet frequentPatternSet);
}
