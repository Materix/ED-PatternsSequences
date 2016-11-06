package pl.edu.agh.ed.model.associationRules;

import java.util.HashSet;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IAssociationRule {
	ITransactionSet getTransactionSet();
	
	Set<IItem> getAntecedent();
	
	Set<IItem> getConsequent();
	
	default long getSupport() {
		Set<IItem> items = new HashSet<>(getAntecedent());
		items.addAll(getConsequent());
		return getTransactionSet().stream()
			.filter(transaction -> transaction.contains(items))
			.count();
	}
	
	default long getAntecedentSupport() {
		Set<IItem> items = getAntecedent();
		return getTransactionSet().stream()
			.filter(transaction -> transaction.contains(items))
			.count();
	}
	
	default long getConsequentSupport() {
		Set<IItem> items = getAntecedent();
		return getTransactionSet().stream()
			.filter(transaction -> transaction.contains(items))
			.count();
	}
	
	default double getNormalizedSupport() {
		return getSupport() / getTransactionSet().size();
	}
	
	default double getConfidence() {
		return getSupport() / getAntecedentSupport();
	}
	
	default double getLift() {
		return getSupport() / (getAntecedentSupport() * getConsequentSupport());
	}
	
	default double getConviction() {
		return (1 - getConsequentSupport()) / (1 - getConfidence());
	}
}
