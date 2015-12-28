package com.jteap.wz.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;



/**
 * 树节点
 * */
public class TreeNode {
	
	private Serializable id;
	private String text;
	private String sortNo;
	private Serializable parentId;
	private boolean expanded;						//扩展
	private boolean leaf;							//是否叶子节点
	
	private Collection<TreeNode> children=new ArrayList<TreeNode>();//儿子节点
	
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public Serializable getId() {
		return id;
	}
	public void setId(Serializable id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSortNo() {
		return sortNo;
	}
	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}
	public Serializable getParentId() {
		return parentId;
	}
	public void setParentId(Serializable parentId) {
		this.parentId = parentId;
	}
	public void appendChild(TreeNode node){
		this.getChildren().add(node);
	}
	
	public Collection<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(Collection<TreeNode> children) {
		this.children = children;
	}
}
