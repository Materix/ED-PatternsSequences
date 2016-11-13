package pl.edu.agh.ed.model.associationRules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public interface IAssociationRule<T extends IItem> {
	ITransactionSet<T> getTransactionSet();
	
	List<T> getAntecedent();
	
	List<T> getConsequent();
	
	default long getSupport() {
		Set<T> items = new HashSet<>(getAntecedent());
		items.addAll(getConsequent());
		return getTransactionSet().stream()
			.filter(transaction -> transaction.contains(items))
			.count();
	}
	
	default long getAntecedentSupport() {
		List<T> items = getAntecedent();
		return getTransactionSet().stream()
			.filter(transaction -> transaction.contains(items))
			.count();
	}
	
	default long getConsequentSupport() {
		List<T> items = getAntecedent();
		return getTransactionSet().stream()
			.filter(transaction -> transaction.contains(items))
			.count();
	}
	
	default double getRelativeSupport() {
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
