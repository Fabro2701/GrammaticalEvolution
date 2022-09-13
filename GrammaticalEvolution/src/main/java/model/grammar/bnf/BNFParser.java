package model.grammar.bnf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import model.grammar.StandardGrammar;



public class BNFParser {
	protected String _string;
	protected BNFTokenizer _tokenizer;
	protected JSONObject _lookahead;
	public BNFParser() {
		_string = new String();
		_tokenizer = new BNFTokenizer();
	}
	public JSONObject parse(String string){
		_string = string;
		_tokenizer.init(string);
		
		
		this._lookahead = this._tokenizer.getNextToken();
		return this.Program();
	}
	protected JSONObject Program() {
		return new JSONObject().put("type", "Grammar").put("rules", this.StatementList());
	}
	protected JSONArray StatementList() {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null) {
			arr.put(this.Statement());
		}
		return arr;
	}
	protected JSONObject Statement() {
		return this.RuleStatement();
	}
	/**
	 * RuleStatement ::= <NTSymbol>  '->'  <ProductionList> '.'
	 * @return
	 */
	protected JSONObject RuleStatement() {
		JSONObject key = this.NTSymbol();
		_eat("->");
		JSONArray productions = this.ProductionList();
		_eat(".");
		return new JSONObject().put("name", key)
							   .put("productions", productions);
	}
	/**
	 * ProductionList ::= <Production> '|' <Production> '|' <Production>
	 * @return
	 */
	protected JSONArray ProductionList() {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null && !this._lookahead.getString("type").equals(".")) {
			arr.put(this.Production());
			if(this._lookahead.getString("type").equals("|")) _eat("|");
		}
		return arr;
	}
	protected JSONArray Production() {
		JSONArray arr = new JSONArray();
		
		while(this._lookahead != null && !(this._lookahead.getString("type").equals("|")||this._lookahead.getString("type").equals("."))) {
			arr.put(this.Symbol());			
		}
		return arr;
	}
	protected JSONObject Symbol() {
		if(this._lookahead.getString("type").equals("NTSYMBOL")) {
			return this.NTSymbol();
		}
		else {
			return this.TSymbol();
		}
	}
	protected JSONObject TSymbol() {
		String literal = _eat("TSYMBOL").getString("value");
		if(literal.charAt(0)=='\'') {
			return new JSONObject().put("type", "Terminal").put("id", literal.substring(1, literal.length()-1));
		}
		return new JSONObject().put("type", "Terminal").put("id", literal);
	}
	protected JSONObject NTSymbol() {
		String literal = _eat("NTSYMBOL").getString("value");
		return new JSONObject().put("type", "NonTerminal").put("id", literal.substring(1, literal.length()-1));
	}
	protected JSONObject _eat(String type) {
		JSONObject token=_lookahead;
		if(this._lookahead==null) {
			System.err.println("unex end of input");
			return null;
		}
		if(!this._lookahead.getString("type").equals(type)) {
			System.err.println("unexpected "+this._lookahead.getString("type")+" expected "+type);
			return null;
		}
		this._lookahead=_tokenizer.getNextToken();
		return token;
	}
	public static void main(String args[]) {
		String e1 ="<S> -> <IF>.\n"
				+ "<IF> -> if <COND> { <TRADE> } else { <TRADE> } | if <COND> { <TRADE> }.\n"
				+ "<COND> -> ( <VALUE> <OP> <OBS> )| ( <COND><LOGOP><COND> ).\n"
				+ "<VALUE> -> <N> | <REALCONST>.\n"
				+ "<OBS> -> ma <MA> [ <DAY> ] | <PRICE> [ <DAY> ].\n"
				+ "<PRICE> -> open|high|low|close.\n"
				+ "<MA> -> 25|50|200.\n"
				+ "<DAY> -> 0|1|2|3|4|5|6|7|8|9.\n"
				+ "<OP> -> <|>|<=|>=.\n"
				+ "<AR> -> +|*|-.\n"
				+ "<N> -> 0|1|2|3|4|5|6|7|8|9.\n"
				+ "<REALCONST> -> 0. <N>.\n"
				+ "<LOGOP> -> '||' | &&.\n"
				+ "<TRADE> -> return BUY ;|return SELL ;|return NOTHING ;.";
		String e2 = "<A>->'a'|<B>."
				  + "<B>->'b'|'a'.";
		
//		StringBuilder sb = new StringBuilder();
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/loads/grammars/default.bnf")));
//			String aux = reader.readLine();
//			while(aux!=null) {
//				sb.append(aux);
//				aux = reader.readLine();
//			}
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//String e3 = sb.toString();
		BNFParser parser = new BNFParser();
		System.out.println(parser.parse(e1).toString(4));
		
		
		
		Pattern p = Pattern.compile("^[^|]+");
		Matcher m = p.matcher("le| t9"); 
		System.out.println(m.find());
		System.out.println(m.end());
	}
}
