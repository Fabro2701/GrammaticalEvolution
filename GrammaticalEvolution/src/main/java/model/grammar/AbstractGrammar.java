package model.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


import model.Util.Pair;
import model.individual.Chromosome;



public abstract class AbstractGrammar {
	protected Symbol initial;
	HashMap<Symbol,Rule>_rulesProductions;

	public AbstractGrammar() {
		_rulesProductions = new LinkedHashMap<Symbol,Rule>();

	}
	public abstract LinkedList<Symbol> parse(Chromosome c);
	public abstract void parseBNF(String filename);
	public static enum SymbolType{NTerminal,Terminal}
	public class Symbol {
		String name;
		SymbolType type;
		public Symbol(String name, SymbolType type) {
			this.type = type;
			this.name = name;
		}
		public SymbolType getType() {return type;}
		@Override
		public String toString() {
			return type==SymbolType.NTerminal?"<"+name+">":name;
		}
		public String getName() {
			return name;
		}
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		public boolean equals(String s) {
			return this.name.equals(s);
		}
		public boolean equals(Symbol s) {
			return this.name.equals(s.name)&&this.type==s.type;
		}
		@Override
		public boolean equals(Object s) {
			return equals((Symbol)s);
		}
		
	}
	public class Production extends ArrayList<Symbol>{
		protected int _minimumDepth;
		int _minimumExp;
		boolean _recursive;
		public Production() {
			super();
		}
		public int get_minimumDepth() {
			return _minimumDepth;
		}
		public void set_minimumDepth(int _minimumDepth) {
			this._minimumDepth = _minimumDepth;
		}
		public int get_minimumExp() {
			return _minimumExp;
		}
		public void set_minimumExp(int _minimumExp) {
			this._minimumExp = _minimumExp;
		}
		public boolean is_recursive() {
			return _recursive;
		}
		public void set_recursive(boolean _recursive) {
			this._recursive = _recursive;
		}
		public Production(Symbol... terms) {
			this();
			for (int i = 0; i < terms.length; i++) {
				this.add(terms[i]);
			}
		}
		
	
		public boolean equals(Production p2) {
			if(this.size()!=p2.size())return false;
			for(int i=0;i<this.size();i++) {
				if(!this.get(i).equals(p2.get(i)))return false;
			}
			return true;
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Symbol t : this) {
				if(t.type==AbstractGrammar.SymbolType.Terminal)sb.append("\'"+t+"\'");
				else sb.append(t);
				sb.append(' ');

			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(String.format("(%b, %d, %d)", this._recursive, this._minimumDepth, this._minimumExp));
			return sb.toString();
		}
		
		
	}
	public class Rule extends ArrayList<Production>{
		private Symbol _symbol;
		boolean _recursive;
		int _minimumDepth;
		int _minimumExp;
		public Rule() {
			super();
		}
		public Symbol get_symbol() {
			return _symbol;
		}
		public void set_symbol(Symbol _symbol) {
			this._symbol = _symbol;
		}
		public boolean is_recursive() {
			return _recursive;
		}
		public void set_recursive(boolean _recursive) {
			this._recursive = _recursive;
		}
		public int get_minimumDepth() {
			return _minimumDepth;
		}
		public void set_minimumDepth(int _minimumDepth) {
			this._minimumDepth = _minimumDepth;
		}
		
		
		
	}
	public Symbol getInitial() {
		return initial;
	}
	public Rule getRule(Symbol s) {
		return this._rulesProductions.get(s);
	}
	public void setInitial(Symbol initial) {
		if(initial.getType()==SymbolType.Terminal)throw new IllegalArgumentException("The initial symbol has to be non-Terminal");
		this.initial = initial;
	}
	private static int inf = 99999;
	public void calculateAttributes() {
		List<Symbol>visitedRules = new ArrayList<Symbol>();
		
		//clear
		for(Symbol s:this._rulesProductions.keySet()) {
			this._rulesProductions.get(s).set_minimumDepth(inf);
			this._rulesProductions.get(s)._minimumExp = inf;
		}
		
		//rules mindepth
		calculateRuleMinDepth();
		calculateProductionsMinDepth();
		
		
		//rules minExp
		
		this.calculateRuleMinExpansion();
		
		
		this.calculateProductionsMinExpansion();
		
		
		for(Symbol s:this._rulesProductions.keySet()) {
			visitedRules.clear();
			//System.out.println("-----------------------------");
			//System.out.println("Calling "+s+" rule isRecursive");
			this._rulesProductions.get(s)._recursive = this._isRecursive(this._rulesProductions.get(s),visitedRules);
		}
		
	}
	private void calculateRuleMinDepth() {
		boolean change=true;
		while(change) {
			change=false;
			for(var entry:this._rulesProductions.entrySet()) {
				Symbol s = entry.getKey();
				Rule rule = entry.getValue();
				int tmp = inf;
				for(Production prod:rule) {
					int tmp2=0;
					for(Symbol s2:prod) {
						 tmp2 = Math.max(auxMinDepth(s2), tmp2);
					}
					tmp = Math.min(auxInf(tmp2+1), tmp);
				}
				if(tmp!=rule._minimumDepth){
					rule._minimumDepth = tmp;
					change = true;
				}
			}
		}
	}
	private int auxMinDepth(Symbol s) {
		if(s.type == SymbolType.NTerminal) {
			int tmp =  _rulesProductions.get(s)._minimumDepth;
			return auxInf(tmp);
		}
		return 1;
	}
	private int auxInf(int n) {
		return n>=inf?inf:n;
	}
	private void calculateProductionsMinDepth() {
		for(var entry:this._rulesProductions.entrySet()) {
			for(Production p:entry.getValue()) {
				p._minimumDepth = 0;
				for(Symbol s:p) {
					if(s.type == SymbolType.NTerminal) {
						p._minimumDepth = Math.max(p._minimumDepth, this._rulesProductions.get(s)._minimumDepth);
					}
					else {
						p._minimumDepth = Math.max(p._minimumDepth, 1);
					}
				}
			}
		}
	}
	private void calculateRuleMinExpansion() {
		boolean change=true;
		while(change) {
			change=false;
			for(var entry:this._rulesProductions.entrySet()) {
				Symbol s = entry.getKey();
				Rule rule = entry.getValue();
				int tmp = inf;
				for(Production prod:rule) {
					int tmp2=0;
					for(Symbol s2:prod) {
						 tmp2 += auxMinExp(s2);
					}
					tmp = Math.min(auxInf(tmp2), tmp);
				}
				if(tmp!=rule._minimumExp){
					rule._minimumExp = tmp;
					change = true;
				}
			}
		}
	}
	private int auxMinExp(Symbol s) {
		if(s.type == SymbolType.NTerminal) {
			int tmp =  _rulesProductions.get(s)._minimumExp;
			tmp++;
			//if(_rulesProductions.get(s).size()>1)tmp++;
			return auxInf(tmp);
		}
		return 0;
	}
	private void calculateProductionsMinExpansion() {
		for(var entry:this._rulesProductions.entrySet()) {
			for(Production p:entry.getValue()) {
				p._minimumExp = 0;
				for(Symbol s:p) {
					if(s.type == SymbolType.NTerminal) {
						p._minimumExp += this._rulesProductions.get(s)._minimumExp +1;
					}
				}
			}
		}
		
	}
	private boolean _isRecursive(Rule query, List<Symbol> visitedRules) {
		//System.out.println("Entering "+query._symbol+" method");
		Rule r=null;
		if(visitedRules.size()==0)r = query;
		else r = this._rulesProductions.get(visitedRules.get(visitedRules.size()-1));
		if(visitedRules.contains(query._symbol)) {
			query._recursive=true;
			//System.out.println("visitedRules contains "+query._symbol);
			return true;
		}
		boolean b=false;
		for(Production p:r) {
			for(Symbol s:p) {
				if(s.type == SymbolType.NTerminal) {
					Rule query2 = this._rulesProductions.get(s);
					if(!visitedRules.contains(query2._symbol)) {
						visitedRules.add(s);
						//System.out.println(s+" added to visited rules");
						//System.out.print("visited: ");
						//for(Symbol ss:visitedRules)System.out.print(ss+ " ");
						//System.out.println();
						if(_isRecursive(query,visitedRules)) {
							p._recursive=true;
							b = true;
							//System.out.println(p+" is recursive");
							visitedRules.remove(visitedRules.size()-1);
							break;
						}
						else {
							visitedRules.remove(visitedRules.size()-1);
							//System.out.println(p+" is not recursive");
						}
					}
					
					
				}
			}
		}

		//System.out.println("Quitting "+query._symbol+" method");
		return b;
	}
	
	public static void main(String args[]) {
		AbstractGrammar g = new StandardGrammar();
		g.parseBNF("resources/grammar/default.bnf");
		System.out.println(g);

		g.calculateAttributes();
		
		for(Symbol k:g._rulesProductions.keySet()) {
			Rule r = g._rulesProductions.get(k);
			System.out.printf("rule %s : (%b %d %d)\n",r._symbol,r._recursive,r._minimumDepth,r._minimumExp);
			for(Production p:r) {
				System.out.printf("		prod %s : (%b %d %d)\n",p,p._recursive,p._minimumDepth,p._minimumExp);
			}
		}
		
	}
	
}
