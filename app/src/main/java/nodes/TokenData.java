package nodes;

import nodes.Parser.Token;

public class TokenData {
	private Token type;
	private String value;
	
	public TokenData(Token type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public Token getType() { return type; }
	public String getValue() { return value; }
	public void setValue(String value) { this.value = value; }
	public String toString() {
		if(value.equals(""))
			return type.toString();
		return "("+type+","+value+")"; 
	}
}
