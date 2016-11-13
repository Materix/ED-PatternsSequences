package pl.edu.agh.ed.algorithm.fpgrowth;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import pl.edu.agh.ed.model.IItem;

public class FPTreeNode<T extends IItem> {
	private final T item;
	
	private final FPTreeNode<T> parent;
	
	private final Set<FPTreeNode<T>> children;
	
	private long count;
	
	private FPTreeNode<T> nodeLink;
	
	public FPTreeNode() {
		this(null, null);
	}
	
	public FPTreeNode(T item, FPTreeNode<T> parent) {
		this(item, parent, 1);
	}

	public FPTreeNode(T item, FPTreeNode<T> parent, long count) {
		children = new HashSet<>();
		this.item = item;
		this.parent = parent;
	}

	public boolean isRoot() {
		return item == null;
	}
	
	public Optional<FPTreeNode<T>> getChild(T item) {
		return getChildren().stream().filter(child -> child.item.equals(item)).findAny();
	}

	public void incrementCount() {
		incrementCount(1);
	}

	public void incrementCount(long count) {
		this.count += count;
	}
	
	public void addChild(FPTreeNode<T> child) {
		getChildren().add(child);
	}

	public FPTreeNode<T> getNodeLink() {
		return nodeLink;
	}

	public void setNodeLink(FPTreeNode<T> nodeLink) {
		this.nodeLink = nodeLink;
	}

	public Set<FPTreeNode<T>> getChildren() {
		return children;
	}

	public FPTreeNode<T> getParent() {
		return parent;
	}

	public long getCount() {
		return count;
	}

	public T getItem() {
		return item;
	}
}
