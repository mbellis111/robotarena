package nodes;

import game.Robot;

public class Script {
    private Node headNode;
    private Node currentNode;
    private Robot owner;
    private int tokenLength;
    private boolean functionCalled;
    private int loopedNoCall;

    public Script(Node headNode, Robot owner) {
        this.headNode = headNode;
        this.owner = owner;
        this.currentNode = headNode;
        tokenLength = -1;
        functionCalled = false;
        loopedNoCall = 0;
    }

    public Node getHeadNode() {
        return headNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public Robot getOwner() {
        return owner;
    }

    public void setOwner(Robot owner) {
        this.owner = owner;
    }

    public int getTokensInScript() {
        return tokenLength;
    }

    public void setTokensInScript(int tokenLength) {
        this.tokenLength = tokenLength;
    }

    public boolean getIsFunctionCalled() {
        return functionCalled;
    }

    public void setFunctionCalled(boolean functionCalled) {
        this.functionCalled = functionCalled;
    }

    public int getLoopedNoCall() {
        return loopedNoCall;
    }

    public void setLoopedNoCall(int loopedNoCall) {
        this.loopedNoCall = loopedNoCall;
    }
}
