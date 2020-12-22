package map;

import java.util.Iterator;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;

/**
 * Path class represents a path between two nodes in a graph
 */
public class Path implements GraphPath<Node> {
    Array<Node> nodes;

    public Path() {
        nodes = new Array<Node>();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    @Override
    public int getCount() {
        return nodes.size;
    }

    @Override
    public Node get(int index) {
        return nodes.get(index);
    }

    @Override
    public void add(Node node) {
        nodes.add(node);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void reverse() {
        nodes.reverse();
    }

    public void log() {
        for (int i = 0; i < nodes.size; i++) {
            Node n = nodes.get(i);
            int targetY = (n.getIndex() / Map.mapTileWidth) * Map.tilePixelHeight;
            int targetX = (n.getIndex() % Map.mapTileWidth) * Map.tilePixelWidth;
            System.out.println(targetX + ", " + targetY);
        }
    }
    
}
