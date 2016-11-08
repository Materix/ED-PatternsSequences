package pl.edu.agh.ed.algorithm.fpgrowth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
				.stream()
				.map(items -> new FrequentPattern(transactionSet, items))
				.collect(Collectors.toSet()));
	}

	private Map<IItem, Long> calculateSupportForSingleItem(ITransactionSet transactionSet) {
		return transactionSet.stream().map(ITransaction::getItems).flatMap(Set::stream)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	private Set<Set<IItem>> fpgrowth(FPTree tree, Set<IItem> prefix, long prefixSupport, 
			Map<IItem, Long> mapSupport, int minSupport) {
		Set<Set<IItem>> result = new HashSet<>();
		if (tree.getRoot().getChildren().size() == 1) {
			Set<FPTreeNode> singlePath = new HashSet<>();
			FPTreeNode currentNode = tree.getRoot().getChildren().iterator().next();
			while (currentNode.getChildren().size() == 1) {
				singlePath.add(currentNode);
				currentNode = currentNode.getChildren().iterator().next();
			}
			if (currentNode.getChildren().size() == 0) {
				return generateAllCombinationsForSinglePath(singlePath, prefix);
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
				result.add(new HashSet<>(beta));
				
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
				result.addAll(fpgrowth(treeBeta, beta, betaSupport, mapSupportBeta, minSupport));
			}
		}
		return result;
	}

	private Set<Set<IItem>> generateAllCombinationsForSinglePath(Set<FPTreeNode> singlePath, Set<IItem> prefix) {
		Set<Set<IItem>> powerset = new HashSet<>();
		powerset.add(new HashSet<>());

	    for (FPTreeNode node : singlePath) {
	    	Set<Set<IItem>> temp = new HashSet<>();

	        for (Set<IItem> innerSet : powerset) {
	            innerSet = new HashSet<>(innerSet);
	            innerSet.add(node.getItem());
	            temp.add(innerSet);
	        }
	        powerset.addAll(temp);
	    }
	    
	    return powerset.stream()
	    		.filter(set -> set.size() > 0)
	    		.map(set -> {set.addAll(prefix); return set; })
	    		.collect(Collectors.toSet());
	}
}
