package nodes;

public class FunctionNode extends Node {
	private Functions function;
	private Variable var;
	private double num;
	private boolean isNumber;
	
	public FunctionNode(BlockType type, Functions function) {
		super(type);
		this.function = function;
	}
	
	public FunctionNode(BlockType type, Functions function, Variable var) {
		this(type, function);
		this.var = var;
		isNumber = false;
	}
	
	public FunctionNode(BlockType type, Functions function, double num) {
		this(type, function);
		this.num = num;
		isNumber = true;
	}

	public Functions getFunction() {
		return function;
	}
	
	public boolean usesNumber() {
		return isNumber;
	}
	
	public Variable getVariable() {
		return var;
	}
	
	public double getNumber() {
		return num;
	}
	
	public String toString() {
		if(function == Functions.SHOOT || function == Functions.MOVE) {
			if(isNumber)
				return "Function ("+function+", "+num+")";
			return "Function ("+function+", "+var+")";
		}
		return "Function ("+function+")";
	}
}
