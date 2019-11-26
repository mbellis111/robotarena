package nodes;

public class BoolNode extends Node {

    private Variable var, otherVariable;
    private double num;
    private Operators operator;
    private boolean hasNumber;

    public BoolNode(BlockType type) {
        super(type);
    }

    public BoolNode(BlockType type, Variable var, Operators operator, double num) {
        this(type);
        this.var = var;
        this.operator = operator;
        this.num = num;
        this.otherVariable = null;
        this.hasNumber = true;
    }

    public BoolNode(BlockType type, Variable var, Operators operator, Variable otherVariable) {
        this(type);
        this.var = var;
        this.operator = operator;
        this.num = -1;
        this.otherVariable = otherVariable;
        this.hasNumber = false;
    }

    public Variable getVariable() {
        return var;
    }

    public Operators getOperator() {
        return operator;
    }

    public double getNumber() {
        return num;
    }

    public String toString() {
        if (hasNumber) {
            return "Boolean ( " + var + " " + operator + " " + num + " )";
        }
        return "Boolean ( " + var + " " + operator + " " + otherVariable + " )";
    }

    public boolean hasNumber() {
        return hasNumber;
    }

    public Variable getOtherVariable() {
        return otherVariable;
    }

}
