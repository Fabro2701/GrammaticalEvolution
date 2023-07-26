package model.grammar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import model.grammar.bnf.BNFParser;
import model.individual.Chromosome;

public class StandardGrammar extends AbstractGrammar{

	@Override
	public LinkedList<Symbol> parse(Chromosome c) {
		Symbol t = this.getInitial();
		List<Production> ps;
		LinkedList<Symbol> q = new LinkedList<Symbol>();
		LinkedList<Symbol> terminals = new LinkedList<Symbol>();
		
		int limit=100;
		int i=0;
		int calls=0;
		while(true) {
			ps = this.getRule(t);
			int m = ps.size();
			//int r = Util.toInt(codons.get(i).bits.get(0, Util.log2(m)));
			
			int r = c.getCodon(i) % m;
			//c.setModToCodon(i, r);
			q.addAll(0, ps.get(r));
			
			calls++;
			while(!q.isEmpty() && q.getFirst().getType()==SymbolType.Terminal) {
				terminals.add(q.pop());
			}
			
			if(q.isEmpty())break;
			
			t = q.pop();
			i++;
			i %= c.getLength();
			if(calls>=limit)return null;
		}
		c.setUsedCodons(calls);

		
		return terminals;
	}

	@Override
	public void parseBNF(String filename) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			String aux = reader.readLine();
			while(aux!=null) {
				sb.append(aux);
				aux = reader.readLine();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String string = sb.toString();
		
		BNFParser parser = new BNFParser();
		JSONObject o = parser.parse(string);
		
		JSONArray rules = o.getJSONArray("rules");
		for(int i=0;i<rules.length();i++) {
			JSONObject rule = rules.getJSONObject(i);
			
			String name = rule.getJSONObject("name").getString("id");
			Symbol nameS = new Symbol(name,SymbolType.NTerminal);
			
			if(i==0)this.setInitial(nameS);
			
			JSONArray productions = rule.getJSONArray("productions");
			List<Production> ps = new ArrayList<Production>();
			for(int j=0;j<productions.length();j++) {
				JSONArray p = productions.getJSONArray(j);
				Production production = new Production();
				for(int k=0; k<p.length();k++) {
					JSONObject s = p.getJSONObject(k);
					if(s.getString("type").equals("Terminal")) {
						Symbol inS = new Symbol(s.getString("id"),SymbolType.Terminal);
						production.add(inS);
					}
					else {
						Symbol inS = new Symbol(s.getString("id"),SymbolType.NTerminal);
						production.add(inS);
					}
				}
				ps.add(production);
			}
			Rule r = new Rule();
			r.set_symbol(nameS);
			r.addAll(ps);
			addRule(nameS,r);
		}		
	}
	public void addRule(Symbol s, Rule r) {
		_rulesProductions.put(s, r);
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Symbol nt : _rulesProductions.keySet()) {
			sb.append(nt + " -> ");
			for (Production p : _rulesProductions.get(nt)) {
				sb.append(p);
				sb.append(" | ");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
			sb.append('\n');
		}
		return sb.toString();
	}
}
