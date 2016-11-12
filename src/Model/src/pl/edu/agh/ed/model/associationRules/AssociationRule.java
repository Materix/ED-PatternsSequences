package pl.edu.agh.ed.model.associationRules;

import java.util.Collections;
import java.util.List;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class AssociationRule implements IAssociationRule {
	
	private final ITransactionSet transactionSet;
	
	private final List<IItem> antecedent;
	
	private final List<IItem> consequent;

	public AssociationRule(ITransactionSet transactionSet, List<IItem> antecedent, List<IItem> consequent) {
		this.transactionSet = transactionSet;
		this.antecedent = antecedent;
		this.consequent = consequent;
	}

	@Override
	public ITransactionSet getTransactionSet() {
		return transactionSet;
	}

	@Override
	public List<IItem> getAntecedent() {
		return Collections.unmodifiableList(antecedent);
	}

	@Override
	public List<IItem> getConsequent() {
		return Collections.unmodifiableList(consequent);
	}

}
