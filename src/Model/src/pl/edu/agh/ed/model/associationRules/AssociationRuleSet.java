package pl.edu.agh.ed.model.associationRules;

import java.util.Collections;
import java.util.Set;

import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class AssociationRuleSet implements IAssociationRuleSet {
	private final ITransactionSet transactionSet;
	
	private final Set<IAssociationRule> associationRules;
	
	public AssociationRuleSet(ITransactionSet transtionSet, Set<IAssociationRule> associationRules) {
		this.transactionSet = transtionSet;
		this.associationRules = associationRules;
	}

	@Override
	public ITransactionSet getTransactionSet() {
		return transactionSet;
	}

	@Override
	public Set<IAssociationRule> getAssociationRules() {
		return Collections.unmodifiableSet(associationRules);
	}
}
