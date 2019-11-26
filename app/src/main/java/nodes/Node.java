package nodes;

public abstract class Node {
    private BlockType type;
    private Node nextNode = null;

    public enum BlockType {
        IF_THEN_ELSE, WHILE, FUNCTION, BOOL_STATEMENT, NOTHING
    }

    public enum Variable {
        HEALTH, AMMO, SHIELDS, X, Y, NEAREST_ENEMY, LOWEST_HP_ENEMY, HIGHEST_HP_ENEMY,
        UP, DOWN, LEFT, RIGHT, UP_RIGHT, UP_LEFT, DOWN_LEFT, DOWN_RIGHT,
        ROBOTS_DETECTED, ARENA_WIDTH, ARENA_HEIGHT, MISSILES
    }

    public enum Functions {
        SHOOT, MOVE, RELOAD, SHIELD, DETECT, MISSILE
    }

    public enum Operators {
        EQUALS, LESS_THAN, GREATER_THAN, GREATER_EQUAL_THAN, LESS_EQUAL_THAN, AND, OR, NOT
    }

    public Node(BlockType type) {
        this.type = type;
    }

    public BlockType getNodeType() {
        return type;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public Node getNextNode() {
        return nextNode;
    }
}
