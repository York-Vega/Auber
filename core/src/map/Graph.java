package map;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

public class Graph implements IndexedGraph<Node> {
    Array<Node> nodes;


    public Graph (TiledMap map) {
        TiledMapTileLayer tiles = (TiledMapTileLayer)map.getLayers().get(0);
        int height = Map.mapTileHeight;
        int width = Map.mapTileWidth;

        this.nodes = new Array<Node>();

        //initialises all nodes
        for (int y = 0; y < height; y++){
            for(int x = 0; x < width; x++) {
                this.nodes.add(new Node());
            }
        }

        for (int y = 0; y < height; y++){
            for(int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell current = tiles.getCell(x, y);
                TiledMapTileLayer.Cell left = tiles.getCell(x-1, y);
                TiledMapTileLayer.Cell right = tiles.getCell(x+1, y);
                TiledMapTileLayer.Cell up = tiles.getCell(x, y+1);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y-1);

                Node currentNode = nodes.get(width * y + x);

                if (current != null) {
                    if (y != 0 && down != null) {
                        Node downNode = nodes.get(width * (y-1) + x);
                        currentNode.createEdge(downNode, 1);
                    }
                    if (x != 0 && left != null) {
                        Node leftNode = nodes.get(width * y + x-1);
                        currentNode.createEdge(leftNode, 1);
                    }
                    if (y != height-1 && up != null) {
                        Node upNode = nodes.get(width * (y+1) + x);
                        currentNode.createEdge(upNode, 1);
                    }
                    if (x != width-1 && right != null) {
                        Node rightNode = nodes.get(width * y + x+1);
                        currentNode.createEdge(rightNode, 1);
                    }
                }
            }
        }
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.getConnections();
    }

    @Override
    public int getIndex(Node node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }
}
