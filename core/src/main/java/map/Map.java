package map;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Stores information about the map and its graph representation
 */
public abstract class Map {
    // Height and Width of Map in Tiles
    public static int mapTileHeight;
    public static int mapTileWidth;

    // Height and Width of Map in Pixels 
    public static int mapPixelHeight;
    public static int mapPixelWidth;

    //Height and width of a Tile in Pixels
    public static int tilePixelHeight;
    public static int tilePixelWidth;

    // Graph representing the Map
    public static Graph graph;
    
    public static void create(TiledMap map) {
        MapProperties properties = map.getProperties();

        Map.mapTileHeight = properties.get("height", Integer.class);
        Map.mapTileWidth = properties.get("width", Integer.class);
        Map.tilePixelHeight = properties.get("tileheight", Integer.class);
        Map.tilePixelWidth = properties.get("tilewidth", Integer.class);
        Map.mapPixelHeight = Map.tilePixelHeight * Map.mapTileHeight;
        Map.mapPixelWidth = Map.tilePixelWidth * Map.mapTileWidth;

        Map.graph = new Graph(map);
    }
}
