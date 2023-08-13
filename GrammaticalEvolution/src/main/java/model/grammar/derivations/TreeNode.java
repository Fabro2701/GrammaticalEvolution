package model.grammar.derivations;

import java.util.ArrayList;
import java.util.Iterator;

import model.grammar.AbstractGrammar.Symbol;
import model.grammar.AbstractGrammar.SymbolType;


public class TreeNode {
	protected ArrayList<TreeNode> _children;
	protected int _depth;
	protected TreeNode _parent;
	protected Symbol _data;
	protected int expansion;
	
	public TreeNode(Symbol data) {
		this._data = data;
		this._depth = 0;
		_children = new ArrayList<TreeNode>();

	}
	public TreeNode(TreeNode copy) {
		this._data = copy._data;
		this._depth = copy._depth;
		this._parent = copy._parent;
		this._children = new ArrayList<TreeNode>();
		for(TreeNode tn:copy._children) {
			this._children.add(new TreeNode(tn));
		}

	}
	public void setParent(TreeNode parent) {
		this._parent = parent;
	}
	public TreeNode getParent() {
		return this._parent;
	}
	public Symbol getData() {
		return this._data;
	}
	public void setData(Symbol data) {
		this._data = data;
	}
	public void clearChildren() {
		this._children.clear();
	}
	public ArrayList<TreeNode> get_children() {
		return _children;
	}
	public void addChild(TreeNode child) {
		child._depth = this._depth+1;
		child.setParent(this);
		_children.add(child);
	}
	public int getNumberOfChildren() {
		return this._children.size();
	}
	public int get_depth() {
		return _depth;
	}
	public int getExpansion() {
		return expansion;
	}
	public void setExpansion(int expansion) {
		this.expansion = expansion;
	}
	@Override
	public String toString() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(this._data.toString());
        buffer.append('\n');
        for (Iterator<TreeNode> it = _children.iterator(); it.hasNext();) {
            TreeNode next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
	public String getFlatString() {
		if(this._children.size()==0)return this._data.getName();
		StringBuilder sb = new StringBuilder();
		for(TreeNode n:this._children) {
			sb.append(n.getFlatString());
		}
		return sb.toString();
	}
}
