package nodes;

public class IfNode extends Node {

    private Node elseNode;
    private BoolNode boolNode;

    public IfNode(BlockType type) {
        super(type);
    }

    public IfNode(BlockType type, BoolNode boolNode) {
        this(type);
        this.boolNode = boolNode;
    }

    public IfNode(BlockType type, BoolNode boolNode, Node elseNode) {
        this(type);
        this.boolNode = boolNode;
        this.elseNode = elseNode;
    }

    public void setElseNode(Node elseNode) {
        this.elseNode = elseNode;
    }

    public Node getElseNode() {
        return elseNode;
    }

    public BoolNode getBoolNode() {
        return boolNode;
    }

    public String toString() {
        return "If " + boolNode.toString();
    }

}
