package pl.edu.agh.ed.algorithm.fpgrowth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;

import pl.edu.agh.ed.model.IItem;

public class FPTree {
	private final Map<IItem, FPTreeNode> frequentItemNodeHeaders;
	
	private final Map<IItem, FPTreeNode> lastItemNodes;
	
	private FPTreeNode root;
	
	public FPTree() {
		frequentItemNodeHeaders = new HashMap<>();
		lastItemNodes = new HashMap<>();
		root = new FPTreeNode();
	}
	
	// TODO check this
	public FPTree(List<List<FPTreeNode>> prefixPaths, Map<IItem, Long> mapSupportBeta, long minSupport) {
		this();
		for (List<FPTreeNode> prefixPath : prefixPaths) {
			FPTreeNode currentNode = root;
			long pathSupport = mapSupportBeta.get(prefixPath.get(0).getItem()); 
			for (int i = prefixPath.size() - 1; i >= 1; i--) {
				FPTreeNode pathItem = prefixPath.get(i);
				if (mapSupportBeta.get(pathItem) >= minSupport) {
					Optional<FPTreeNode> child = currentNode.getChild(pathItem.getItem());
					if(child.isPresent()) { 
						child.get().incrementCount(pathSupport);
						currentNode = child.get();
					} else { 
						FPTreeNode newNode = new FPTreeNode(pathItem.getItem(), currentNode, pathSupport);
						currentNode.addChild(newNode);
						currentNode = newNode;
						fixNodeLinks(pathItem.getItem(), newNode);	
					}
				}
			}
		}
	}

	public void addTransaction(SortedSet<IItem> frequentItems) {
		FPTreeNode currentNode = root;
		for (IItem item : frequentItems){
			Optional<FPTreeNode> child = currentNode.getChild(item);
			if(child.isPresent()) { 
				child.get().incrementCount();
				currentNode = child.get();
			} else { 
				FPTreeNode newNode = new FPTreeNode(item, currentNode);
				currentNode.addChild(newNode); 
				currentNode = newNode;
				fixNodeLinks(item, newNode);	
			}
		}
	}
	
	private void fixNodeLinks(IItem item, FPTreeNode newNode) {
		FPTreeNode lastNode = lastItemNodes.get(item);
		if(lastNode != null) {
			lastNode.setNodeLink(newNode);
		}
		lastItemNodes.put(item, newNode);
		frequentItemNodeHeaders.putIfAbsent(item, newNode);
	}

	public FPTreeNode getRoot() {
		return root;
	}

	public Map<IItem, FPTreeNode> getHeaders() {
		return frequentItemNodeHeaders;
	}
}
