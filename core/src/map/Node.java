package map;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

/**
 * Node Class represents a tile in the map.
 */
public class Node {
    private static int nextIndex = 0; 

    private Array<Connection<Node>> edges;
    private int index;

    /**
     * Represents node for pathfinding.
     */
    public Node() {
        this.edges = new Array<Connection<Node>>();
        this.index = nextIndex;
        Node.nextIndex++;
    }

    public int getIndex() {
        return this.index;
    }

    public Array<Connection<Node>> getConnections() { 
        return this.edges;
    }

    public void createEdge(Node toNode, float cost) {
        edges.add(new Edge(cost, this, toNode));
    }

}
