package map;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class Distance implements Heuristic<Node>{

    @Override
    public float estimate(Node node, Node endNode) {
        int startY = node.getIndex() / Map.mapTileWidth;
        int startX = node.getIndex() % Map.mapTileWidth;

        int endY = endNode.getIndex() / Map.mapTileWidth;
        int endX = endNode.getIndex() % Map.mapTileWidth;

        // distance in manhatten form
        return Math.abs(startX - endX) + Math.abs(startY - endY);
    }
    
}
