package pl.edu.agh.ed.algorithm.fpgrowth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import pl.edu.agh.ed.algorithm.IFrequentPatternsExtractor;
import pl.edu.agh.ed.model.IItem;
import pl.edu.agh.ed.model.patterns.FrequentPattern;
import pl.edu.agh.ed.model.patterns.FrequentPatternSet;
import pl.edu.agh.ed.model.patterns.IFrequentPatternSet;
import pl.edu.agh.ed.model.transactions.ITransaction;
import pl.edu.agh.ed.model.transactions.ITransactionSet;

public class FPGrowthFrequentPatternsExtractor<T extends IItem> implements IFrequentPatternsExtractor<T> {

	@Override
	public IFrequentPatternSet<T> extract(ITransactionSet<T> transactionSet, int minSupport) {
		Map<T, Long> itemSupport = calculateSupportForSingleItem(transactionSet);

		FPTree<T> tree = new FPTree<T>();
		for (ITransaction<T> transaction : transactionSet.getTransactions()) {
			SortedSet<T> frequentItems = transaction.getItems().stream()
					.filter(item -> itemSupport.get(item) >= minSupport)
					.collect(Collector.of(() -> new TreeSet<>(Comparator.comparingLong(itemSupport::get).reversed()),
							Set::add, (left, right) -> {
								left.addAll(right);
								return left;
							}));
			tree.addTransaction(frequentItems);
		}
		
		return new FrequentPatternSet<>(transactionSet, fpgrowth(tree, new ArrayList<>(), 0, itemSupport, minSupport)
				.entrySet()
				.stream()
				.map(entry -> new FrequentPattern<>(transactionSet, entry.getKey(), entry.getValue()))
				.collect(Collectors.toSet()));
	}

	private Map<T, Long> calculateSupportForSingleItem(ITransactionSet<T> transactionSet) {
		return transactionSet.stream().map(ITransaction::getItems).flatMap(List::stream)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	private Map<List<T>, Long> fpgrowth(FPTree<T> tree, List<T> prefix, long prefixSupport, 
			Map<T, Long> mapSupport, long minSupport) {
		Map<List<T>, Long> result = new HashMap<>();
		if (tree.getRoot().getChildren().size() == 1) {
			List<FPTreeNode<T>> singlePath = new ArrayList<>();
			FPTreeNode<T> currentNode = tree.getRoot().getChildren().iterator().next();
			while (currentNode.getChildren().size() == 1) {
				singlePath.add(currentNode);
				currentNode = currentNode.getChildren().iterator().next();
			}
			if (currentNode.getChildren().size() == 0) {
				return generateAllCombinationsForSinglePath(singlePath, prefix, prefixSupport, minSupport, mapSupport);
			}						
		} else {
			SortedMap<T, FPTreeNode<T>> headerMap = new TreeMap<>(Comparator.comparingLong(mapSupport::get).reversed());
			headerMap.putAll(tree.getHeaders());
			for (Entry<T, FPTreeNode<T>> entry : headerMap.entrySet()) {
				T item = entry.getKey();
				long itemSupport = mapSupport.get(item);
				List<T> beta = new ArrayList<>(prefix);
				beta.add(item);
				long betaSupport = (prefixSupport < itemSupport) ? prefixSupport : itemSupport;
				if (betaSupport >= minSupport) {
					result.put(new ArrayList<>(beta), betaSupport);
				}
				
				List<List<FPTreeNode<T>>> prefixPaths = new ArrayList<>();
				FPTreeNode<T> path = tree.getHeaders().get(item);
				
				Map<T, Long> mapSupportBeta = new HashMap<>();
				
				while (path != null) {
					if (!path.getParent().isRoot()) {
						List<FPTreeNode<T>> prefixPath = new ArrayList<>();
						prefixPath.add(path);
						long pathCount = path.getCount();
						mapSupportBeta.put(path.getItem(), pathCount);

						FPTreeNode<T> parent = path.getParent();
						T parentItem = parent.getItem();
						while (!parent.isRoot()) {
							prefixPath.add(parent);
							if (mapSupportBeta.get(parentItem) == null) {
								mapSupportBeta.put(parentItem, pathCount);
							} else {
								mapSupportBeta.put(parentItem, mapSupportBeta.get(parentItem) + pathCount);
							}
							parent = parent.getParent();
						}
						prefixPaths.add(prefixPath);
					}
					path = path.getNodeLink();
				}
				
				FPTree<T> treeBeta = new FPTree<T>(prefixPaths, mapSupportBeta, minSupport);
				result.putAll(fpgrowth(treeBeta, beta, betaSupport, mapSupportBeta, minSupport));
			}
		}
		return result;
	}

	private Map<List<T>, Long> generateAllCombinationsForSinglePath(List<FPTreeNode<T>> singlePath, List<T> prefix, 
			long prefixSupport, long minSupport, Map<T, Long> mapSupport) {
		List<List<FPTreeNode<T>>> powerset = new ArrayList<>();
				
		for (int i = 1; i < 2 << singlePath.size(); i++) {
			List<FPTreeNode<T>> temp = new ArrayList<>(); 
			char[] chars = Integer.toBinaryString(i).toCharArray();
			for (int pos = 0; pos < chars.length; pos++) {
				if (chars[pos] == '1') {
					temp.add(singlePath.get(pos));
				}
			}
		}
	    
	    return powerset.stream()
	    		.filter(list -> list.size() > 0)
	    		.collect(Collectors.toMap(list -> {
	    			List<T> newSet = new ArrayList<>(prefix);
	    			newSet.addAll(list.stream().map(FPTreeNode<T>::getItem).collect(Collectors.toList()));
	    			return newSet;
	    		}, set -> {
	    			long setSupport = set.stream().map(FPTreeNode<T>::getItem).mapToLong(mapSupport::get).min().orElse(0);
	    			return (prefixSupport < setSupport) ? prefixSupport : setSupport;
	    		}))
	    		.entrySet()
	    		.stream()
	    		.filter(entry -> entry.getValue() >= minSupport)
	    		.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
}
