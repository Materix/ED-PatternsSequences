package pl.edu.agh.ed.model.associationRules;

import java.util.Collections;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class AssociationRuleSet<T extends IItem> implements IAssociationRuleSet<T> {
	private final ITransactionSet<T> transactionSet;
	
	private final Set<IAssociationRule<T>> associationRules;
	
	public AssociationRuleSet(ITransactionSet<T> transtionSet, Set<IAssociationRule<T>> associationRules) {
		this.transactionSet = transtionSet;
		this.associationRules = associationRules;
	}

	@Override
	public ITransactionSet<T> getTransactionSet() {
		return transactionSet;
	}

	@Override
	public Set<IAssociationRule<T>> getAssociationRules() {
		return Collections.unmodifiableSet(associationRules);
	}
}
