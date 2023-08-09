package model.grammar.derivations;

import java.util.LinkedList;
import java.util.Random;

import model.grammar.AbstractGrammar;
import model.grammar.AbstractGrammar.Production;
import model.grammar.AbstractGrammar.Rule;
import model.grammar.AbstractGrammar.Symbol;
import model.grammar.StandardGrammar;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;

public class DerivationTree {
	private TreeNode _current, _root , _deepest;
	private int _nodeCount;
	private AbstractGrammar _grammar;
	
	public DerivationTree(AbstractGrammar grammar) {
		this._nodeCount = 0;
		this._grammar = grammar;
	}
	public DerivationTree(TreeNode copy) {
		_root = new TreeNode(copy);
		_current = _root;
	}
	protected void _addNode(TreeNode node) {
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
	}
	public boolean buildFromChromosome(Chromosome c) {
		_addNode(new TreeNode(_grammar.getInitial()));
		LinkedList<TreeNode>pending = new LinkedList<TreeNode>();
		pending.add(_current);
		int i = 0;
		int limit = 400;
		while(!pending.isEmpty()) {
			_current = pending.pollFirst();
			
			if(_current._data.getType().equals(AbstractGrammar.SymbolType.NTerminal)) {
				if(_grammar.getRule(_current._data).size()==1) {//no codon needed
					_expandAndPushNode(0,pending);
				}
				else{
					_expandAndPushNode(c.getCodon(i%c.getLength()),pending);
					i++;
				}
			}
			if(i>=limit) return false;
		}
		return true;
	}
	private void _expandAndPushNode(int codonValue, LinkedList<TreeNode> pending) {
		Rule r = _grammar.getRule(_current._data);
		Production ps = _grammar.getRule(_current._data).get(codonValue%r.size());
		for(Symbol s:ps) {
			TreeNode n = new TreeNode(s);
			_addNode(n);
		}
		
		pending.addAll(0, this._current._children);
		
	}
	public TreeNode getDeepest() {
		return this._deepest;
	}
	public int get_nodeCount() {
		return _nodeCount;
	}
	public TreeNode getRoot() {
		return this._root;
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
		c.init(new Random(3));
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
