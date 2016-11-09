package pl.edu.agh.ed.algorithm.fpgrowth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalLong;
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

public class FPGrowthFrequentPatternsExtractor implements IFrequentPatternsExtractor {

	@Override
	public IFrequentPatternSet extract(ITransactionSet transactionSet, int minSupport) {
		Map<IItem, Long> itemSupport = calculateSupportForSingleItem(transactionSet);

		FPTree tree = new FPTree();
		for (ITransaction transaction : transactionSet.getTransactions()) {
			SortedSet<IItem> frequentItems = transaction.getItems().stream()
					.filter(item -> itemSupport.get(item) >= minSupport)
					.collect(Collector.of(() -> new TreeSet<>(Comparator.comparingLong(itemSupport::get).reversed()),
							Set::add, (left, right) -> {
								left.addAll(right);
								return left;
							}));
			tree.addTransaction(frequentItems);
		}
		
		return new FrequentPatternSet(transactionSet, fpgrowth(tree, new HashSet<>(), 0, itemSupport, minSupport)
				.entrySet()
				.stream()
				.map(entry -> new FrequentPattern(transactionSet, entry.getKey(), entry.getValue()))
				.collect(Collectors.toSet()));
	}

	private Map<IItem, Long> calculateSupportForSingleItem(ITransactionSet transactionSet) {
		return transactionSet.stream().map(ITransaction::getItems).flatMap(List::stream)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	private Map<Set<IItem>, Long> fpgrowth(FPTree tree, Set<IItem> prefix, long prefixSupport, 
			Map<IItem, Long> mapSupport, long minSupport) {
		Map<Set<IItem>, Long> result = new HashMap<>();
		if (tree.getRoot().getChildren().size() == 1) {
			Set<FPTreeNode> singlePath = new HashSet<>();
			FPTreeNode currentNode = tree.getRoot().getChildren().iterator().next();
			while (currentNode.getChildren().size() == 1) {
				singlePath.add(currentNode);
				currentNode = currentNode.getChildren().iterator().next();
			}
			if (currentNode.getChildren().size() == 0) {
				return generateAllCombinationsForSinglePath(singlePath, prefix, prefixSupport, minSupport, mapSupport);
			}						
		} else {
			SortedMap<IItem, FPTreeNode> headerMap = new TreeMap<>(Comparator.comparingLong(mapSupport::get).reversed());
			headerMap.putAll(tree.getHeaders());
			for (Entry<IItem, FPTreeNode> entry : headerMap.entrySet()) {
				IItem item = entry.getKey();
				long itemSupport = mapSupport.get(item);
				Set<IItem> beta = new HashSet<>(prefix);
				beta.add(item);
				long betaSupport = (prefixSupport < itemSupport) ? prefixSupport : itemSupport;
				if (betaSupport >= minSupport) {
					result.put(new HashSet<>(beta), betaSupport);
				}
				
				Set<List<FPTreeNode>> prefixPaths = new HashSet<>();
				FPTreeNode path = tree.getHeaders().get(item);
				
				Map<IItem, Long> mapSupportBeta = new HashMap<>();
				
				while (path != null) {
					if (!path.getParent().isRoot()) {
						List<FPTreeNode> prefixPath = new ArrayList<>();
						prefixPath.add(path);
						long pathCount = path.getCount();
						mapSupportBeta.put(path.getItem(), pathCount);

						FPTreeNode parent = path.getParent();
						IItem parentItem = parent.getItem();
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
				
				FPTree treeBeta = new FPTree(prefixPaths, mapSupportBeta, minSupport);
				result.putAll(fpgrowth(treeBeta, beta, betaSupport, mapSupportBeta, minSupport));
			}
		}
		return result;
	}

	private Map<Set<IItem>, Long> generateAllCombinationsForSinglePath(Set<FPTreeNode> singlePath, Set<IItem> prefix, 
			long prefixSupport, long minSupport, Map<IItem, Long> mapSupport) {
		Set<Set<FPTreeNode>> powerset = new HashSet<>();
		powerset.add(new HashSet<>());

	    for (FPTreeNode node : singlePath) {
	    	Set<Set<FPTreeNode>> temp = new HashSet<>();

	        for (Set<FPTreeNode> innerSet : powerset) {
	            innerSet = new HashSet<>(innerSet);
	            innerSet.add(node);
	            temp.add(innerSet);
	        }
	        powerset.addAll(temp);
	    }
	    
	    return powerset.stream()
	    		.filter(set -> set.size() > 0)
	    		.collect(Collectors.toMap(set -> {
	    			Set<IItem> newSet = new HashSet<>(prefix);
	    			newSet.addAll(set.stream().map(FPTreeNode::getItem).collect(Collectors.toList()));
	    			return newSet;
	    		}, set -> {
	    			long setSupport = set.stream().map(FPTreeNode::getItem).mapToLong(mapSupport::get).min().orElse(0);
	    			return (prefixSupport < setSupport) ? prefixSupport : setSupport;
	    		}))
	    		.entrySet()
	    		.stream()
	    		.filter(entry -> entry.getValue() >= minSupport)
	    		.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
}
