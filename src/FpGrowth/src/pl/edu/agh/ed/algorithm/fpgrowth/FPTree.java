package pl.edu.agh.ed.algorithm.fpgrowth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;

import pl.edu.agh.ed.model.IItem;

public class FPTree<T extends IItem> {
	private final Map<T, FPTreeNode<T>> frequentItemNodeHeaders;
	
	private final Map<T, FPTreeNode<T>> lastItemNodes;
	
	private FPTreeNode<T> root;
	
	public FPTree() {
		frequentItemNodeHeaders = new HashMap<>();
		lastItemNodes = new HashMap<>();
		root = new FPTreeNode<T>();
	}
	
	// TODO check this
	public FPTree(List<List<FPTreeNode<T>>> prefixPaths, Map<T, Long> mapSupportBeta, long minSupport) {
		this();
		for (List<FPTreeNode<T>> prefixPath : prefixPaths) {
			FPTreeNode<T> currentNode = root;
			long pathSupport = mapSupportBeta.get(prefixPath.get(0).getItem()); 
			for (int i = prefixPath.size() - 1; i >= 1; i--) {
				FPTreeNode<T> pathItem = prefixPath.get(i);
				if (mapSupportBeta.get(pathItem) >= minSupport) {
					Optional<FPTreeNode<T>> child = currentNode.getChild(pathItem.getItem());
					if(child.isPresent()) { 
						child.get().incrementCount(pathSupport);
						currentNode = child.get();
					} else { 
						FPTreeNode<T> newNode = new FPTreeNode<>(pathItem.getItem(), currentNode, pathSupport);
						currentNode.addChild(newNode);
						currentNode = newNode;
						fixNodeLinks(pathItem.getItem(), newNode);	
					}
				}
			}
		}
	}

	public void addTransaction(SortedSet<T> frequentItems) {
		FPTreeNode<T> currentNode = root;
		for (T item : frequentItems){
			Optional<FPTreeNode<T>> child = currentNode.getChild(item);
			if(child.isPresent()) { 
				child.get().incrementCount();
				currentNode = child.get();
			} else { 
				FPTreeNode<T> newNode = new FPTreeNode<T>(item, currentNode);
				currentNode.addChild(newNode); 
				currentNode = newNode;
				fixNodeLinks(item, newNode);	
			}
		}
	}
	
	private void fixNodeLinks(T item, FPTreeNode<T> newNode) {
		FPTreeNode<T> lastNode = lastItemNodes.get(item);
		if(lastNode != null) {
			lastNode.setNodeLink(newNode);
		}
		lastItemNodes.put(item, newNode);
		frequentItemNodeHeaders.putIfAbsent(item, newNode);
	}

	public FPTreeNode<T> getRoot() {
		return root;
	}

	public Map<T, FPTreeNode<T>> getHeaders() {
		return frequentItemNodeHeaders;
	}
}
