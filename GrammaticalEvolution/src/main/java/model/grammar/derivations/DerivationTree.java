package model.grammar.derivations;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.grammar.AbstractGrammar;
import model.grammar.AbstractGrammar.Production;
import model.grammar.AbstractGrammar.Rule;
import model.grammar.AbstractGrammar.Symbol;
import model.grammar.AbstractGrammar.SymbolType;
import model.grammar.StandardGrammar;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;

public class DerivationTree {
	private TreeNode _current, _root , _deepest;
	private int _nodeCount, ntNodeCount;
	private AbstractGrammar _grammar;
	
	public DerivationTree(AbstractGrammar grammar) {
		this._nodeCount = 0;
		this.ntNodeCount = 0;
		this._grammar = grammar;
	}
	public DerivationTree(TreeNode copy) {
		_root = new TreeNode(copy);
		_current = _root;
	}
	public void addNode(TreeNode node) {
		if(_root == null) {
			_root = node;
			_deepest = _root;
			_current = _root;
		}
		else {
			_current.addChild(node);
			if(_current._depth>=_deepest._depth) _deepest = node;
		}
		_nodeCount++;
		if(node.getData().getType()==SymbolType.NTerminal && _grammar.getRule(node.getData()).size()!=1)ntNodeCount++;
	}
	public boolean buildFromChromosome(Chromosome c) {
		addNode(new TreeNode(_grammar.getInitial()));
		LinkedList<TreeNode>pending = new LinkedList<TreeNode>();
		pending.add(_current);
		int i = 0;
		int limit = 400;
		while(!pending.isEmpty()) {
			_current = pending.pollFirst();
			
			if(_current._data.getType().equals(AbstractGrammar.SymbolType.NTerminal)) {
				if(_grammar.getRule(_current._data).size()==1) {//no codon needed
					expandAndPushNode(0,pending);
				}
				else{
					expandAndPushNode(c.getCodon(i%c.getLength()),pending);
					i++;
				}
			}
			if(i>=limit) return false;
		}
		return true;
	}
	public void expandAndPushNode(int codonValue, LinkedList<TreeNode> pending) {
		expandAndPushNode(codonValue, pending, 0);
	}
	public void expandAndPushNode(int codonValue, LinkedList<TreeNode> pending, int pos) {
		Rule r = _grammar.getRule(_current._data);
		Production ps = _grammar.getRule(_current._data).get(codonValue%r.size());
		for(Symbol s:ps) {
			TreeNode n = new TreeNode(s);
			addNode(n);
		}
		pending.addAll(pos, this._current._children.stream().filter(c->c._data.getType()==AbstractGrammar.SymbolType.NTerminal).collect(Collectors.toList()));
	}
	public Chromosome buildChromosome(Chromosome crom) {
		LinkedList<TreeNode>pending = new LinkedList<TreeNode>();
		pending.add(this._root);
		TreeNode c = null;
		
		int i=0;
		while(!pending.isEmpty()) {
			c=pending.poll();
			if(_grammar.getRule(c._data).size()==1) {
				//no codon used
			}
			else {
				crom.setIntToCodon(i, c.getExpansion());
				i++;
			}
			pending.addAll(0,c.get_children().stream().filter(ch->ch._data.getType()==AbstractGrammar.SymbolType.NTerminal).collect(Collectors.toList()));
		}
		crom.setUsedCodons(i);
		return crom;
	}
	/**
	 * preorder
	 */
	public List<TreeNode>flat(){
		List<TreeNode> l = new ArrayList<>(this.ntNodeCount);
		ArrayDeque<TreeNode>q = new ArrayDeque<TreeNode>();
		
		q.add(_root);
		TreeNode tmp;
		while(!q.isEmpty()) {
			tmp = q.poll();
			List<TreeNode>tmpl = tmp.get_children().stream().filter(n->n.getData().getType()==SymbolType.NTerminal).collect(Collectors.toList());
			for(int i=tmpl.size()-1;i>=0;i--)q.addFirst(tmpl.get(i));
			if(_grammar.getRule(tmp.getData()).size()!=1) l.add(tmp);
		}
		return l;
	}
	public TreeNode getDeepest() {
		return this._deepest;
	}
	public int get_nodeCount() {
		return _nodeCount;
	}
	public int getNTNodeCount() {
		return this.ntNodeCount;
	}
	public TreeNode getRoot() {
		return this._root;
	}
	public TreeNode get_current() {
		return _current;
	}
	public void setCurrent(TreeNode current) {
		this._current=current;
	}
	@Override
	public String toString() {
		return this._root.toString();
	}
	public static void main(String args[]) {
		StandardGrammar g = new StandardGrammar();
		g.parseBNF("resources/grammar/default.bnf");
		DerivationTree t = new DerivationTree(g);
		Chromosome c = new Chromosome(50);
		c.init(new Random(1));
		System.out.println(c);
		Genotype geno = new Genotype(c);
		Phenotype pheno = new Phenotype();
		Individual ind = new Individual(geno,pheno,g);
		System.out.println(pheno.getPlainSymbols());
		boolean b = t.buildFromChromosome(c);
		if(b) {
			System.out.println(t.toString());
			System.out.println(t._deepest._data.toString()+" "+t._deepest._depth);
			System.out.println(t._nodeCount);
		}
		else {
			System.out.println("bad");
		}
		/*BiasedGrammar g = new BiasedGrammar();
		g.parseBNF("defaultBias");
		DerivationTree t = new DerivationTree(g);
		
		
		for(int i=0;i<10;i++) {
			Chromosome c = new Chromosome(50);
			//c.setArrayIntToCodon(1,0,0,0,0,0,0,0);
			
			boolean b = t.buildFromChromosome(c);
			if(b) {
				System.out.println(t.toString());
				System.out.println(t._deepest._data.toString()+" "+t._deepest._depth);
				System.out.println(t._nodeCount);
				System.out.println(g.getDeepestPropagated(t));
			}
			else {
				System.out.println("bad");
			}
		}*/
		
	}
}
