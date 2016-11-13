package pl.edu.agh.ed.model.associationRules;

import java.util.Collections;
import java.util.List;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class AssociationRule<T extends IItem> implements IAssociationRule<T> {
	
	private final ITransactionSet<T> transactionSet;
	
	private final List<T> antecedent;
	
	private final List<T> consequent;

	public AssociationRule(ITransactionSet<T> transactionSet, List<T> antecedent, List<T> consequent) {
		this.transactionSet = transactionSet;
		this.antecedent = antecedent;
		this.consequent = consequent;
	}

	@Override
	public ITransactionSet<T> getTransactionSet() {
		return transactionSet;
	}

	@Override
	public List<T> getAntecedent() {
		return Collections.unmodifiableList(antecedent);
	}

	@Override
	public List<T> getConsequent() {
		return Collections.unmodifiableList(consequent);
	}

}
