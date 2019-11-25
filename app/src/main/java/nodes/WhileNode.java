package nodes;

public class WhileNode extends Node {
	
	private BoolNode boolNode;
	private Node breakNode;

	public WhileNode(BlockType type) {
		super(type);
	}
	
	public WhileNode(BlockType type, BoolNode boolNode) {
		this(type);
		this.boolNode = boolNode;
	}
	
	public WhileNode(BlockType type, BoolNode boolNode, Node breakNode) {
		this(type, boolNode);
		this.breakNode = breakNode;
	}
	
	public BoolNode getBoolNode() {
		return boolNode;
	}
	
	public void setBreakNode(Node breakNode) {
		this.breakNode = breakNode;
	}
	
	public Node getBreakNode() {
		return breakNode;
	}
	
	public String toString() {
		return "While "+boolNode.toString();
	}

}
