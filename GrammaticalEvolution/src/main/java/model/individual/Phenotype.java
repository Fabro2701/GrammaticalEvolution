package model.individual;

import java.util.Iterator;
import java.util.LinkedList;

import model.grammar.AbstractGrammar.Symbol;
import model.grammar.Parser;

public class Phenotype {
	boolean valid;
	//Strategy strategy;
	//Evaluator evaluator;
	String visualization;
	LinkedList<Symbol> symbols; 
	public Phenotype() {
		valid=false;
		//strategy = new Strategy();
	}
	public Phenotype(Phenotype copy) {
		valid = copy.valid;
		//strategy = new Strategy(copy.strategy);
		visualization = copy.visualization;
		this.symbols = copy.symbols;
	}
	public boolean isValid() {
		return valid;
	}

//	public Strategy getStrategy() {
//		return strategy;
//	}
	public void init(LinkedList<Symbol> symbols) {
		if(symbols==null) {
			valid = false;
			return;
		}
		else {
			this.symbols = symbols;
//			Parser parser = new Parser();
//			evaluator = new Evaluator(parser.parse(this.getVisualCode()));
//			strategy.setEvaluator(evaluator);
			valid = true;
		}
	}
	public String getPlainSymbols() {
		StringBuilder s = new StringBuilder();
		if(symbols==null) {
			int a=0;
		}
		Iterator<Symbol>it = symbols.iterator();
		while(it.hasNext()) {
			Symbol current = it.next();
			s.append(current);
		}
		return s.toString();
	}
	private void setVisualization() {
		StringBuilder s = new StringBuilder();
		
		Iterator<Symbol>it = symbols.iterator();
		StringBuilder tabs = new StringBuilder();
		while(it.hasNext()) {
			Symbol current = it.next();
			
			if(current.equals(";")) {
				s.append(current);
				s.append('\n');
			}
			else if(current.equals("{")) {
				s.append(current);
				s.append('\n');
				tabs.append("    ");
			}
			else if(current.equals("}")) {
				
				tabs.deleteCharAt(tabs.length()-1);
				tabs.deleteCharAt(tabs.length()-1);
				tabs.deleteCharAt(tabs.length()-1);
				tabs.deleteCharAt(tabs.length()-1);
				s.append(tabs);
				s.append(current);
				s.append('\n');
			}
			else if(current.equals("endif")) {
				s.append(tabs);
				s.append(current);
				s.append('\n');
			}
			else if(current.equals("if")||current.equals("else")){
				s.append(tabs);
				s.append(current);
			}
			else {
				s.append(current);
			}
		}
		visualization = s.toString();
	}
	public String getVisualCode() {
		if(visualization==null) {
			setVisualization();
		}
		return visualization;
	}
}
