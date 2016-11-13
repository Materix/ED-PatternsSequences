package pl.edu.agh.ed.algorithm.fpgrowth.impl;

import java.util.Vector;

import pl.edu.agh.ed.model.IItem;

public class FPtree {
	private boolean root;
	private IItem item;
	private Vector<FPtree> children;
	private FPtree parent;
	private FPtree next;
	private int count;
	
	public FPtree(IItem item) {
		this.item = item;
		next = null;
		children = new Vector<FPtree>();
		root = false;
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public IItem getItem() {
		return item;
	}

	public void setItem(IItem item) {
		this.item = item;
	}

	public Vector<FPtree> getChildren() {
		return children;
	}

	public void setChildren(Vector<FPtree> children) {
		this.children = children;
	}

	public FPtree getParent() {
		return parent;
	}

	public void setParent(FPtree parent) {
		this.parent = parent;
	}

	public FPtree getNext() {
		return next;
	}

	public void setNext(FPtree next) {
		this.next = next;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void incrementCount() {
		incrementCount(1);
	}

	public void incrementCount(int count) {
		this.count += count;
	}
}
