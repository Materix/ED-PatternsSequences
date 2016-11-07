package pl.edu.agh.ed.model.associationRules;

import java.util.Set;
import java.util.stream.Stream;

import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IAssociationRuleSet {
	ITransactionSet getTransactionSet();
	
	Set<IAssociationRule> getAssociationRules();
	
	default Stream<IAssociationRule> stream() {
		return getAssociationRules().stream();
	}
	
	default int size() {
		return getAssociationRules().size();
	}
}
