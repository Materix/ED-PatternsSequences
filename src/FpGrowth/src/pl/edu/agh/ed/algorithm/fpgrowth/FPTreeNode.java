package pl.edu.agh.ed.algorithm.fpgrowth;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;

public class FPTreeNode {
	private final IItem item;
	
	private final FPTreeNode parent;
	
	private final Set<FPTreeNode> children;
	
	private long count;
	
	private FPTreeNode nodeLink;
	
	public FPTreeNode() {
		this(null, null);
	}
	
	public FPTreeNode(IItem item, FPTreeNode parent) {
		this(item, parent, 1);
	}

	public FPTreeNode(IItem item, FPTreeNode parent, long count) {
		children = new HashSet<>();
		this.item = item;
		this.parent = parent;
	}

	public boolean isRoot() {
		return item == null;
	}
	
	public Optional<FPTreeNode> getChild(IItem item) {
		return getChildren().stream().filter(child -> child.item.equals(item)).findAny();
	}

	public void incrementCount() {
		incrementCount(1);
	}

	public void incrementCount(long count) {
		this.count += count;
	}
	
	public void addChild(FPTreeNode child) {
		getChildren().add(child);
	}

	public FPTreeNode getNodeLink() {
		return nodeLink;
	}

	public void setNodeLink(FPTreeNode nodeLink) {
		this.nodeLink = nodeLink;
	}

	public Set<FPTreeNode> getChildren() {
		return children;
	}

	public FPTreeNode getParent() {
		return parent;
	}

	public long getCount() {
		return count;
	}

	public IItem getItem() {
		return item;
	}
}
