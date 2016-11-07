package pl.edu.agh.ed.model.associationRules;

import java.util.Set;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class AssociationRule implements IAssociationRule {
	
	private final ITransactionSet transactionSet;
	
	private final Set<IItem> antecedent;
	
	private final Set<IItem> consequent;

	public AssociationRule(ITransactionSet transactionSet, Set<IItem> antecedent, Set<IItem> consequent) {
		this.transactionSet = transactionSet;
		this.antecedent = antecedent;
		this.consequent = consequent;
	}

	@Override
	public ITransactionSet getTransactionSet() {
		return transactionSet;
	}

	@Override
	public Set<IItem> getAntecedent() {
		return antecedent;
	}

	@Override
	public Set<IItem> getConsequent() {
		return consequent;
	}

}
