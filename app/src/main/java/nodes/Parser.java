package nodes;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Takes in an ArrayList of Strings and checks
 * to make sure syntax is good, as well as creating
 * a script, which is a graph of nodes 
 * @author MattSBellis
 *
 */
public class Parser {

	public enum Token {
		NOTHING, WHILE, BOOLEAN_STATEMENT, IF, ELSE, FUNCTION, 
		END_IF, END_WHILE, END_ELSE, LEFT_PAREN, RIGHT_PAREN, SPACE, VARIABLE,
		NUMBER, ERROR, OPERATOR;
	}
	
	public enum ErrorMessage {
		GOOD, ERROR,  DOESNTTAKEVALUE, BOOLEANORDER, WHILEORDER, 
		IFORDER, ELSEORDER, FUNCTIONNEEDVALUE, MISSILENEEDNUMBER, INFINITELOOP;
	}

	private ArrayList<String> list;
	private int pIndex;
	private Map<Integer, Integer> indexMap;
	private int recDepth;
	private int whileIndex;
	private WhileNode whileNode;
	
	private class ErrorData {
		private ErrorMessage error;
		private int index;
		
		public ErrorData(ErrorMessage error, int index) {
			this.error = error;
			this.index = index;
		}
		public ErrorMessage getError() { return error; }
		public int getIndex() { return index; }
	}

	public Parser(ArrayList<String> list) {
		this.list = list;
		pIndex = -1;
		recDepth = -1;
		whileIndex = -1;
		indexMap = new TreeMap<Integer, Integer>();
	}
	
	/**
	 * Given an array of strings
	 * this parses and checks for
	 * syntax errors and returns
	 * the head node of the script
	 * @return Node headNode
	 */
	public Node createTree() {
		List<TokenData> tokenList = tokenize(list);
		if(checkSyntax(tokenList) == ErrorMessage.GOOD) {
			tokenList = markTokens(tokenList);
			return createNode(tokenList, 0);
		}
		return null;
	}
	
	public ErrorMessage checkForSyntaxErrors() {
		return checkSyntax(tokenize(list));
	}
	
	private Node createNode(List<TokenData> tokens, int index) {
		pIndex = index;
		if(pIndex > tokens.size() - 1)
			return null;
		Node newNode = null;
		Node nextNode = null;
		TokenData token = tokens.get(pIndex);
		Token type = (Token) token.getType();
		switch(type) {
		case FUNCTION:
			if(token.getValue().equals("MOVE") || token.getValue().equals("SHOOT")) {
				TokenData nextToken = tokens.get(++pIndex);
				if(nextToken.getType() == Token.VARIABLE) {
					Node.Functions f = stringToFunction(token.getValue());
					Node.Variable v = stringToVariable(nextToken.getValue());
					newNode = new FunctionNode(Node.BlockType.FUNCTION, f, v);
					nextNode = createNode(tokens, ++pIndex);
					newNode.setNextNode(nextNode);
				}
				if(nextToken.getType() == Token.NUMBER) {
					newNode = new FunctionNode(Node.BlockType.FUNCTION, stringToFunction(token.getValue()), Double.parseDouble(nextToken.getValue()));
					nextNode = createNode(tokens, ++pIndex);
					newNode.setNextNode(nextNode);
				}
				break;
			} else if (token.getValue().equals("MISSILE")) {
				TokenData nextToken = tokens.get(++pIndex);
				if(nextToken.getType() == Token.NUMBER) {
					newNode = new FunctionNode(Node.BlockType.FUNCTION, stringToFunction(token.getValue()), Double.parseDouble(nextToken.getValue()));
					nextNode = createNode(tokens, ++pIndex);
					newNode.setNextNode(nextNode);
				}
				break;
			}
			newNode = new FunctionNode(Node.BlockType.FUNCTION, stringToFunction(token.getValue()));
			nextNode = createNode(tokens, ++pIndex);
			newNode.setNextNode(nextNode);
			break;
		case IF:
			String recValue = token.getValue();
			recDepth = Integer.parseInt(recValue);
			++pIndex;
			TokenData nextToken = tokens.get(++pIndex);
			Node.Variable v = stringToVariable(nextToken.getValue());
			nextToken = tokens.get(++pIndex);
			Node.Operators o = stringToOperator(nextToken.getValue());
			nextToken = tokens.get(++pIndex);
			BoolNode boolNode = null;
			if(nextToken.getType() == Token.NUMBER) {
				double n = Double.parseDouble(nextToken.getValue());
				boolNode = new BoolNode(Node.BlockType.BOOL_STATEMENT, v, o, n);
			} else if (nextToken.getType() == Token.VARIABLE) {
				Node.Variable ov = stringToVariable(nextToken.getValue());
				boolNode = new BoolNode(Node.BlockType.BOOL_STATEMENT, v, o, ov);
			}
			int indexElse = getMatching(tokens, pIndex, recValue, Token.ELSE);
			indexMap.put(recDepth, getMatching(tokens, indexElse, recValue, Token.END_ELSE)+1);
			nextToken = tokens.get(++pIndex);
			IfNode ifNode = new IfNode(Node.BlockType.IF_THEN_ELSE, boolNode);
			nextNode = createNode(tokens, ++pIndex);
			ifNode.setNextNode(nextNode);
			recDepth = Integer.parseInt(recValue);
			Node elseNode = createNode(tokens, indexElse);
			ifNode.setElseNode(elseNode);
			newNode = ifNode;
			break;
		case WHILE:
			recValue = token.getValue();
			++pIndex;
			nextToken = tokens.get(++pIndex);
			v = stringToVariable(nextToken.getValue());
			nextToken = tokens.get(++pIndex);
			o = stringToOperator(nextToken.getValue());
			nextToken = tokens.get(++pIndex);
			boolNode = null;
			if(nextToken.getType() == Token.NUMBER) {
				double n = Double.parseDouble(nextToken.getValue());
				boolNode = new BoolNode(Node.BlockType.BOOL_STATEMENT, v, o, n);
			} else if (nextToken.getType() == Token.VARIABLE) {
				Node.Variable ov = stringToVariable(nextToken.getValue());
				boolNode = new BoolNode(Node.BlockType.BOOL_STATEMENT, v, o, ov);
			}
			whileIndex = getMatching(tokens, pIndex, recValue, Token.END_WHILE);
			WhileNode whileNode = new WhileNode(Node.BlockType.WHILE, boolNode);
			this.whileNode = whileNode;
			nextToken = tokens.get(++pIndex);
			nextNode = createNode(tokens, ++pIndex);
			whileNode.setNextNode(nextNode);
			newNode = whileNode;
			Node breakNode = createNode(tokens, whileIndex+1);
			whileNode.setBreakNode(breakNode);
			break;
		case NOTHING:
			newNode = new NothingNode(Node.BlockType.NOTHING); 
			nextNode = createNode(tokens, ++pIndex);
			newNode.setNextNode(nextNode);
			break;
		case END_IF:
			recValue = token.getValue();
			recDepth = Integer.parseInt(recValue);
			newNode = createNode(tokens, indexMap.get(recDepth));
			break;
		case END_ELSE:
			newNode = createNode(tokens, ++pIndex);
			break;
		case END_WHILE:
			newNode = this.whileNode;
			break;
		default:
			newNode = createNode(tokens, ++pIndex);
			break;
		}
		return newNode;
	}

	public int getListLength() {
		return list.size();
	}
	
	private int getMatching(List<TokenData> tokens, int startIndex, String number, Token match) {
		TokenData token;
		Token type;
		int index = startIndex;
		while(index < tokens.size() - 1) {
			token = tokens.get(++index);
			type = (Token) token.getType();
			if(type == match && number.equals(token.getValue())) 
				return index;
		}
		return -1;
	}

	/**
	 * Counts levels of recursion
	 * for if and while tokens
	 * @param tokens
	 * @return
	 */
	private List<TokenData> markTokens(List<TokenData> tokens) {
		int index = -1;
		int length = tokens.size();
		int currentIf = 0, currentWhile = 0;
		TokenData token;
		Token type;
		while(index < length -1) {
			token = tokens.get(++index);
			type = (Token) token.getType();
			switch(type) {
			case IF:
				token.setValue(""+(++currentIf));
				break;
			case END_IF:
				token.setValue(""+currentIf);
				break;
			case ELSE:
				token.setValue(""+currentIf);
				break;
			case END_ELSE:
				token.setValue(""+(currentIf--));
				break;
			case WHILE:
				token.setValue(""+(++currentWhile));
				break;
			case END_WHILE:
				token.setValue(""+(currentWhile--));
				break;
			default:
				break;
			}
		}
		return tokens;
	}

	/*
	 * Check for just loop that doesn't execute
	 */
	public ErrorMessage checkSyntax(List<TokenData> tokens) {
		int index = -1;
		int length = tokens.size();
		boolean nonNestedFunction = false; // ensure there is 1 function that isn't in an if/while
		TokenData token;
		ErrorData edata = new ErrorData(ErrorMessage.ERROR, -1);
		Token type;
		
		// check to make sure the only token isn't nothing
		if(length == 1 && tokens.get(index+1).getType() == Token.NOTHING)
			return ErrorMessage.INFINITELOOP;
		
		while(index < length -1) {
			token = tokens.get(++index);
			type = (Token) token.getType();
			switch(type) {
			case IF:
				edata = checkIf(tokens, index);
				index = edata.getIndex();
				break;
			case FUNCTION:
				nonNestedFunction = true;
				edata = checkFunction(tokens, index);
				index = edata.getIndex();
				break;
			case WHILE:
				edata = checkWhile(tokens, index);
				index = edata.getIndex();
				break;
			case NOTHING:
				break;
			default:
				return ErrorMessage.ERROR;
			}
			if(edata.getIndex() <= 0)
				return edata.getError();
		}
		if(nonNestedFunction == false)
			return ErrorMessage.INFINITELOOP;
		return ErrorMessage.GOOD;
	}

	private ErrorData checkFunction(List<TokenData> tokens, int index) {
		TokenData token = tokens.get(index);
		TokenData nextToken;
		if(index+1 <= tokens.size() - 1)
			nextToken = tokens.get(index + 1);
		else
			nextToken = null;
		String value = token.getValue();
		boolean moveOrShoot = value.equals("MOVE") || value.equals("SHOOT");
		if(nextToken != null) {
			Token typeB = (Token) nextToken.getType();
			boolean variableOrNumber = typeB == Token.VARIABLE || typeB == Token.NUMBER;
			if(moveOrShoot) {
				if(variableOrNumber)
					return new ErrorData(ErrorMessage.GOOD, index + 1);
				return new ErrorData(ErrorMessage.FUNCTIONNEEDVALUE, -1);
			} else {
				if(value.equals("MISSILE")) {
					if(typeB == Token.NUMBER)
						return new ErrorData(ErrorMessage.GOOD, index + 1);
					return new ErrorData(ErrorMessage.MISSILENEEDNUMBER, -1);
				}
				if(variableOrNumber)
					return new ErrorData(ErrorMessage.DOESNTTAKEVALUE, -1);
				return new ErrorData(ErrorMessage.GOOD, index);
			}
		} else {
			if(moveOrShoot || value.equals("MISSILE"))
				return new ErrorData(ErrorMessage.FUNCTIONNEEDVALUE, -1);
			return new ErrorData(ErrorMessage.GOOD, index + 1);
		}
	}
	
	private ErrorData checkWhile(List<TokenData> tokens, int index) {
		TokenData token = tokens.get(++index);
		if(token.getType() != Token.LEFT_PAREN)
			return new ErrorData(ErrorMessage.ERROR, -1);
		token = tokens.get(++index);
		if(token.getType() != Token.VARIABLE)
			return new ErrorData(ErrorMessage.BOOLEANORDER, -1);
		token = tokens.get(++index);
		if(token.getType() != Token.OPERATOR)
			return new ErrorData(ErrorMessage.BOOLEANORDER, -1);
		token = tokens.get(++index);

		if(!(token.getType() == Token.NUMBER || token.getType() == Token.VARIABLE))
			return new ErrorData(ErrorMessage.BOOLEANORDER, -1);
		token = tokens.get(++index);
		if(token.getType() != Token.RIGHT_PAREN)
			return new ErrorData(ErrorMessage.BOOLEANORDER, -1);
		int indexBefore = index;
		
		// check to make sure it isn't just a while nothing
		if(index + 1 > tokens.size()-1)
			return new ErrorData(ErrorMessage.WHILEORDER, -1);
		token = tokens.get(index+1);
		if(token.getType() == Token.NOTHING)
			return new ErrorData(ErrorMessage.WHILEORDER, -1);
		
		while(true) {
			ErrorData edata;
			if(index + 1 > tokens.size()-1)
				return new ErrorData(ErrorMessage.WHILEORDER, -1);
			token = tokens.get(++index);
			if(token == null)
				return new ErrorData(ErrorMessage.WHILEORDER, -1);
			if(token.getType() == Token.FUNCTION) {
				edata = checkFunction(tokens, index);
				int tempIndex = edata.getIndex();
				if(tempIndex == -1)
					return edata;
				index = tempIndex;
			} else if(token.getType() == Token.END_WHILE) {
				break;
			} else if(token.getType() == Token.IF) {
				edata = checkIf(tokens, index);
				int tempIndex = edata.getIndex();
				if(tempIndex == -1)
					return edata;
				index = tempIndex;
			} else if(token.getType() == Token.WHILE) {
				edata = checkWhile(tokens, index);
				int tempIndex = edata.getIndex();
				if(tempIndex == -1)
					return edata;
				index = tempIndex;
			} else if(token.getType() != Token.NOTHING)
				return new ErrorData(ErrorMessage.WHILEORDER, -1);
		}
		if(indexBefore == index-1)
			return new ErrorData(ErrorMessage.WHILEORDER, -1);
		return new ErrorData(ErrorMessage.GOOD, index);
	}

	private ErrorData checkIf(List<TokenData> tokens, int index) {
		TokenData token = tokens.get(++index);
		if(token.getType() != Token.LEFT_PAREN)
			return new ErrorData(ErrorMessage.ERROR, -1);
		token = tokens.get(++index);
		if(token.getType() != Token.VARIABLE)
			return new ErrorData(ErrorMessage.BOOLEANORDER, -1);
		token = tokens.get(++index);
		if(token.getType() != Token.OPERATOR)
			return new ErrorData(ErrorMessage.BOOLEANORDER, -1);
		token = tokens.get(++index);

		if(!(token.getType() == Token.NUMBER || token.getType() == Token.VARIABLE))
			return new ErrorData(ErrorMessage.BOOLEANORDER, -1);
		token = tokens.get(++index);
		if(token.getType() != Token.RIGHT_PAREN)
			return new ErrorData(ErrorMessage.ERROR, -1);
		int indexBefore = index;
		while(true) {
			ErrorData edata;
			if(index + 1 > tokens.size()-1)
				return new ErrorData(ErrorMessage.IFORDER, -1);
			token = tokens.get(++index);
			if(token == null)
				return new ErrorData(ErrorMessage.IFORDER, -1);
			if(token.getType() == Token.FUNCTION) {
				edata = checkFunction(tokens, index);
				int tempIndex = edata.getIndex();
				if(tempIndex == -1)
					return edata;
				index = tempIndex;
			} else if(token.getType() == Token.END_IF) {
				break;
			} else if(token.getType() == Token.IF) {
				edata = checkIf(tokens, index);
				int tempIndex = edata.getIndex();
				if(tempIndex == -1)
					return edata;
				index = tempIndex;
			} else if(token.getType() == Token.WHILE) {
				edata = checkWhile(tokens, index);
				int tempIndex = edata.getIndex();
				if(tempIndex == -1)
					return edata;
				index = tempIndex;
			} else if(token.getType() != Token.NOTHING)
				return new ErrorData(ErrorMessage.IFORDER, -1);
		}
		if(indexBefore == index-1)
			return new ErrorData(ErrorMessage.IFORDER, -1);
		token = tokens.get(++index);
		if(token.getType() != Token.ELSE)
			return new ErrorData(ErrorMessage.ELSEORDER, -1);
		indexBefore = index;
		while(true) {
			ErrorData edata;
			if(index + 1 > tokens.size()-1)
				return new ErrorData(ErrorMessage.ELSEORDER, -1);
			token = tokens.get(++index);
			if(token == null)
				return new ErrorData(ErrorMessage.ELSEORDER, -1);
			if(token.getType() == Token.FUNCTION) {
				edata = checkFunction(tokens, index);
				int tempIndex = edata.getIndex();
				if(tempIndex == -1)
					return edata;
				index = tempIndex;
			} else if(token.getType() == Token.END_ELSE) {
				break;
			} else if(token.getType() == Token.IF) {
				edata = checkIf(tokens, index);
				int tempIndex = edata.getIndex();
				if(tempIndex == -1)
					return edata;
				index = tempIndex;
			} else if(token.getType() == Token.WHILE) {
				edata = checkWhile(tokens, index);
				int tempIndex = edata.getIndex();
				if(tempIndex == -1)
					return edata;
				index = tempIndex;
			} else if(token.getType() != Token.NOTHING)
				return new ErrorData(ErrorMessage.ELSEORDER, -1);
		}
		if(indexBefore == index-1)
			return new ErrorData(ErrorMessage.ELSEORDER, -1);
		return new ErrorData(ErrorMessage.GOOD, index);
	}

	private List<TokenData> tokenize(ArrayList<String> strings) {
		List<TokenData> tokens = new LinkedList<TokenData>();
		for(String s: strings) {
			List<TokenData> temp = stringToTokens(s);
			for(TokenData t:temp)
				tokens.add(t);
		}
		return tokens;
	}

	public String errorToString(ErrorMessage error) {
		if(error == ErrorMessage.GOOD) {
			return "No Syntax Errors!";
		} else if (error == ErrorMessage.BOOLEANORDER) {
			return "BOOLEAN statement syntax:\nVARIABLE OPERATOR VARIABLE/NUMBER";
		} else if (error == ErrorMessage.IFORDER) {
			return "IF statement syntax:\nIF BOOLEANSTATEMENT\nFUNCTION/IF/WHILE/NOTHING\nENDIF"; 
		} else if (error == ErrorMessage.ELSEORDER) {
			return "ELSE statement syntax:\nELSE\nFUNCTION/IF/WHILE/NOTHING\nENDELSE";
		} else if (error == ErrorMessage.WHILEORDER) {
			return "WHILE statement syntax:\nWHILE BOOLEANSTATEMENT\nFUNCTION/IF\nENDWHILE";
		} else if(error == ErrorMessage.DOESNTTAKEVALUE) {
			return "DETECT, RELOAD and SHIELD do not take a value";
		} else if(error == ErrorMessage.FUNCTIONNEEDVALUE) {
			return "MOVE and SHOOT need a VARIABLE/NUMBER, MISSILE needs NUMBER";
		} else if(error == ErrorMessage.ERROR) {
			return "Syntax Error!";
		} else if(error == ErrorMessage.INFINITELOOP) {
			return "Potential Infinite Loop!";
		} else if(error == ErrorMessage.MISSILENEEDNUMBER) {
			return "MISSILE only takes a NUMBER";
		}
		return "Error Message Not Found!";
	}

	private Node.Functions stringToFunction(String text) {
		if (text != null) 
			for (Node.Functions f : Node.Functions.values()) 
				if (text.equalsIgnoreCase(f.toString())) 
					return f;
		return null;
	}

	private Node.Operators stringToOperator(String text) {
		if (text != null) 
			for (Node.Operators o : Node.Operators.values()) 
				if (text.equalsIgnoreCase(o.toString())) 
					return o;
		return null;
	}

	private Node.Variable stringToVariable(String text) {
		if (text != null) 
			for (Node.Variable v : Node.Variable.values()) 
				if (text.equalsIgnoreCase(v.toString())) 
					return v;
		return null;
	}

	private TokenData tokenType(String s) {
		if(s.equals("IF"))
			return new TokenData(Token.IF, "");
		else if(s.equals("WHILE"))
			return new TokenData(Token.WHILE, "");
		else if(s.equals("ELSE"))
			return new TokenData(Token.ELSE, "");
		else if(s.equals("END_IF"))
			return new TokenData(Token.END_IF, "");
		else if(s.equals("END_ELSE"))
			return new TokenData(Token.END_ELSE, "");
		else if(s.equals("END_WHILE"))
			return new TokenData(Token.END_WHILE, "");
		else if(s.equals("("))
			return new TokenData(Token.LEFT_PAREN, "");
		else if(s.equals(")"))
			return new TokenData(Token.RIGHT_PAREN, "");
		else if(s.equals("MOVE") || s.equals("SHOOT") || s.equals("RELOAD") || s.equals("DETECT") || s.equals("SHIELD") || s.equals("MISSILE"))
			return new TokenData(Token.FUNCTION, s);
		else if(s.equals("EQUALS") || s.equals("LESS_THAN") || s.equals("GREATER_THAN")
				|| s.equals("LESS_EQUAL_THAN") || s.equals("GREATER_EQUAL_THAN"))
			return new TokenData(Token.OPERATOR, s);
		else if(s.equals("HEALTH") || s.equals("AMMO") || s.equals("NEAREST_ENEMY")
				|| s.equals("LOWEST_HP_ENEMY") || s.equals("UP") || s.equals("DOWN") || s.equals("LEFT") 
				|| s.equals("RIGHT") || s.equals("SHIELDS") || s.equals("HIGHEST_HP_ENEMY")
				|| s.equals("DOWN_RIGHT") || s.equals("DOWN_LEFT") || s.equals("UP_LEFT") || s.equals("UP_RIGHT")
				|| s.equals("ROBOTS_DETECTED") || s.equals("X") || s.equals("Y") 
				|| s.equals("ARENA_WIDTH") || s.equals("ARENA_HEIGHT") || s.equals("MISSILES"))
			return new TokenData(Token.VARIABLE, s);
		else if(s.equals("NOTHING"))
			return new TokenData(Token.NOTHING, "");
		else if(isInt(s))
			return new TokenData(Token.NUMBER, s);
		return new TokenData(Token.ERROR, "");
	}
	private List<TokenData> stringToTokens(String text) {
		String [] splitText = text.split("\\s+");
		LinkedList<TokenData> tokens = new LinkedList<TokenData>();
		for(String s:splitText)
			tokens.add(tokenType(s));
		return tokens;
	}

	private boolean isInt(String s) {
		try{
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public void printTree(Node node) {
		if(node != null) {
			System.out.println(node);
			printTree(node.getNextNode());
			if(node instanceof IfNode)
				printTree(((IfNode) node).getElseNode());
			if(node instanceof WhileNode)
				printTree(((WhileNode) node).getBreakNode());
		}
	}
}
