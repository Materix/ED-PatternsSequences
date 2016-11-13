package pl.edu.agh.ed.algorithm.fpgrowth.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.transactions.ITransaction;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class FPGrowth {

	private int threshold;
	private Vector<FPtree> headerTable;
	private FPtree fptree;
	private Map<Vector<IItem>, Integer> frequentPatterns;

	public FPGrowth(ITransactionSet<IItem> transactionSet, int threshold) {
		this.threshold = threshold;
		fptree(transactionSet);
		fpgrowth(fptree, threshold, headerTable);
	}

	private FPtree conditionalFptreeConstructor(Map<Vector<IItem>, Integer> conditionalPatternBase,
			Map<IItem, Integer> conditionalItemsMaptoFrequencies, int threshold,
			Vector<FPtree> conditionalHeaderTable) {
		// FPTree constructing
		// the null node!
		FPtree conditionalFptree = new FPtree(null);
		conditionalFptree.setRoot(true);
		// remember our transactions here has oredering and non-frequent items
		// for condition items
		for (Vector<IItem> pattern : conditionalPatternBase.keySet()) {
			// adding to tree
			// removing non-frequents and making a vector instead of string
			Vector<IItem> patternVector = new Vector<IItem>();
			for (IItem item: pattern) {
				if (conditionalItemsMaptoFrequencies.get(item) >= threshold) {
					patternVector.addElement(item);
				}
			}
			// the insert method
			insert(patternVector, conditionalPatternBase.get(pattern), conditionalFptree, conditionalHeaderTable);
			// end of insert method
		}
		return conditionalFptree;
	}

	private void fptree(ITransactionSet<IItem> transactionSet) {
		// preprocessing fields
		Map<IItem, Integer> itemsMaptoFrequencies = new HashMap<>();
		List<IItem> sortedItemsbyFrequencies = new LinkedList<>();
		Vector<IItem> itemstoRemove = new Vector<>();
		preProcessing(itemsMaptoFrequencies, transactionSet, sortedItemsbyFrequencies, itemstoRemove);
		construct_fpTree(itemsMaptoFrequencies, transactionSet, sortedItemsbyFrequencies, itemstoRemove);

	}

	private void preProcessing(Map<IItem, Integer> itemsMaptoFrequencies, ITransactionSet<IItem> transactionSet,
			List<IItem> sortedItemsbyFrequencies, Vector<IItem> itemstoRemove) {
		itemsMaptoFrequencies.putAll(transactionSet.stream().map(ITransaction::getItems).flatMap(List::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.reducing(0, e -> 1, Integer::sum))));
		sortedItemsbyFrequencies.addAll(itemsMaptoFrequencies.keySet());
		sortedItemsbyFrequencies.sort(Comparator.comparingInt(itemsMaptoFrequencies::get));
		sortedItemsbyFrequencies.removeIf(item -> itemsMaptoFrequencies.get(item) < threshold);
	}

	private void construct_fpTree(Map<IItem, Integer> itemsMaptoFrequencies, ITransactionSet<IItem> transactionSet,
			List<IItem> sortedItemsbyFrequencies, Vector<IItem> itemstoRemove) {
		// HeaderTable Creation
		// first elements use just as pointers
		headerTable = new Vector<FPtree>();
		for (IItem itemsforTable : sortedItemsbyFrequencies) {
			headerTable.add(new FPtree(itemsforTable));
		}
		// FPTree constructing
		// the null node!
		fptree = new FPtree(null);
		fptree.setRoot(true);
		// ordering frequent items transaction
		
		for (ITransaction<IItem> transaction: transactionSet.getTransactions()) {
			Vector<IItem> transactionSortedbyFrequencies = new Vector<>(transaction.getItems());
			for (IItem item : transaction.getItems()) {
				if (itemstoRemove.contains(item)) {
					continue;
				}
				int index = 0;
				for (IItem vectorItem : transactionSortedbyFrequencies) {
					// some lines of condition is for alphabetically check in
					// equals situatioans
					if (itemsMaptoFrequencies.get(vectorItem) < itemsMaptoFrequencies.get(item)
							|| (itemsMaptoFrequencies.get(vectorItem) == itemsMaptoFrequencies.get(item))) {
						transactionSortedbyFrequencies.add(index, item);
						break;
					}
					index++;
				}
				if (!transactionSortedbyFrequencies.contains(item)) {
					transactionSortedbyFrequencies.add(item);
				}
			}
			insert(transactionSortedbyFrequencies, fptree, headerTable);
			transactionSortedbyFrequencies.clear();
		}
		for (FPtree item : headerTable) {
			int count = 0;
			FPtree itemtemp = item;
			while (itemtemp.getNext() != null) {
				itemtemp = itemtemp.getNext();
				count += itemtemp.getCount();
			}
			item.setCount(count);
		}
		Collections.sort(headerTable, new FrequencyComparitorinHeaderTable());
	}

	private void insert(Vector<IItem> transactionSortedbyFrequencies, FPtree fptree, Vector<FPtree> headerTable) {
		if (transactionSortedbyFrequencies.isEmpty()) {
			return;
		}
		IItem itemtoAddtotree = transactionSortedbyFrequencies.firstElement();
		FPtree newNode = null;
		boolean ifisdone = false;
		for (FPtree child : fptree.getChildren()) {
			if (child.getItem().equals(itemtoAddtotree)) {
				newNode = child;
				child.incrementCount();
				ifisdone = true;
				break;
			}
		}
		if (!ifisdone) {
			newNode = new FPtree(itemtoAddtotree);
			newNode.setCount(1);
			newNode.setParent(fptree);
			fptree.getChildren().add(newNode);
			for (FPtree headerPointer : headerTable) {
				if (headerPointer.getItem().equals(itemtoAddtotree)) {
					while (headerPointer.getNext() != null) {
						headerPointer = headerPointer.getNext();
					}
					headerPointer.setNext(newNode);
				}
			}
		}
		transactionSortedbyFrequencies.remove(0);
		insert(transactionSortedbyFrequencies, newNode, headerTable);
	}

	private void fpgrowth(FPtree fptree, int threshold, Vector<FPtree> headerTable) {
		frequentPatterns = new HashMap<>();
		FPgrowth(fptree, new Vector<>(), threshold, headerTable, frequentPatterns);
	}

	private void FPgrowth(FPtree fptree, Vector<IItem> base, int threshold, Vector<FPtree> headerTable,
			Map<Vector<IItem>, Integer> frequentPatterns) {
		for (FPtree iteminTree : headerTable) {
			Vector<IItem> currentPattern = new Vector<>(base);
			currentPattern.add(iteminTree.getItem());
			int supportofCurrentPattern = 0;
			Map<Vector<IItem>, Integer> conditionalPatternBase = new HashMap<>();
			while (iteminTree.getNext() != null) {
				iteminTree = iteminTree.getNext();
				supportofCurrentPattern += iteminTree.getCount();
				Vector<IItem> conditionalPattern = new Vector<>();
				FPtree conditionalItem = iteminTree.getParent();

				while (!conditionalItem.isRoot()) {
					conditionalPattern.add(0, conditionalItem.getItem());
					conditionalItem = conditionalItem.getParent();
				}
				if (!conditionalPattern.isEmpty()) {
					conditionalPatternBase.put(conditionalPattern, iteminTree.getCount());
				}
			}
			frequentPatterns.put(currentPattern, supportofCurrentPattern);
			// counting frequencies of single items in conditional pattern-base
			Map<IItem, Integer> conditionalItemsMaptoFrequencies = new HashMap<>();
			for (Vector<IItem> conditionalPattern : conditionalPatternBase.keySet()) {
				for (IItem item: conditionalPattern) {
					if (conditionalItemsMaptoFrequencies.containsKey(item)) {
						conditionalItemsMaptoFrequencies.put(item, 
							conditionalItemsMaptoFrequencies.get(item)
							+ conditionalPatternBase.get(conditionalPattern));
					} else {
						conditionalItemsMaptoFrequencies.put(item, conditionalPatternBase.get(conditionalPattern));
					}
				}
			}
			// conditional fptree
			// HeaderTable Creation
			// first elements are being used just as pointers
			// non conditional frequents also will be removed
			Vector<FPtree> conditional_headerTable = new Vector<FPtree>();
			for (IItem itemsforTable : conditionalItemsMaptoFrequencies.keySet()) {
				int count = conditionalItemsMaptoFrequencies.get(itemsforTable);
				if (count < threshold) {
					continue;
				}
				FPtree f = new FPtree(itemsforTable);
				f.setCount(count);
				conditional_headerTable.add(f);
			}
			FPtree conditionalFptree = conditionalFptreeConstructor(conditionalPatternBase,
					conditionalItemsMaptoFrequencies, threshold, conditional_headerTable);
			// headertable reverse ordering
			Collections.sort(conditional_headerTable, new FrequencyComparitorinHeaderTable());
			//
			if (!conditionalFptree.getChildren().isEmpty()) {
				FPgrowth(conditionalFptree, currentPattern, threshold, conditional_headerTable, frequentPatterns);
			}
		}
	}

	private void insert(Vector<IItem> patternVector, int countOfPattern, FPtree conditionalFptree,
			Vector<FPtree> conditionalHeaderTable) {
		if (patternVector.isEmpty()) {
			return;
		}
		IItem itemtoAddtotree = patternVector.firstElement();
		FPtree newNode = null;
		boolean ifisdone = false;
		for (FPtree child : conditionalFptree.getChildren()) {
			if (child.getItem().equals(itemtoAddtotree)) {
				newNode = child;
				child.incrementCount(countOfPattern);
				ifisdone = true;
				break;
			}
		}
		if (!ifisdone) {
			for (FPtree headerPointer : conditionalHeaderTable) {
				// this if also gurantees removing og non frequets
				if (headerPointer.getItem().equals(itemtoAddtotree)) {
					newNode = new FPtree(itemtoAddtotree);
					newNode.setCount(countOfPattern);
					newNode.setParent(conditionalFptree);
					conditionalFptree.getChildren().add(newNode);
					while (headerPointer.getNext() != null) {
						headerPointer = headerPointer.getNext();
					}
					headerPointer.setNext(newNode);
				}
			}
		}
		patternVector.remove(0);
		insert(patternVector, countOfPattern, newNode, conditionalHeaderTable);
	}

	public Map<Vector<IItem>, Integer> getFrequentPatterns() {
		return frequentPatterns;
	}
}
