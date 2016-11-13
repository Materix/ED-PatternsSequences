package pl.edu.agh.ed.model.associationRules;

import java.util.Set;
import java.util.stream.Stream;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IAssociationRuleSet<T extends IItem> {
	ITransactionSet<T> getTransactionSet();
	
	Set<IAssociationRule<T>> getAssociationRules();
	
	default Stream<IAssociationRule<T>> stream() {
		return getAssociationRules().stream();
	}
	
	default int size() {
		return getAssociationRules().size();
	}
}
