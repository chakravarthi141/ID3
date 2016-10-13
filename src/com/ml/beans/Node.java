package com.ml.beans;

import java.util.ArrayList;
import java.util.List;

/**
* This class defines the tree data structure 
*/
public class Node {

	private Node parent = null;
	private List<Node> children = new ArrayList<>();
	private String data = null;
	
	public Node(String data) {
		this.data = data;
	}
	
	public Node(String data, Node parent) {
		this.parent = parent;
		this.data = data;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
		parent.children.add(this);
	}
	
	public List<Node> getChildren() {
		return children;
	}
	
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	
	public void addChild(Node child) {
		this.children.add(child);
//		child.setParent(this); 
	}
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
}
