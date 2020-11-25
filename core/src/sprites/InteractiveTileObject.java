package sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

/**
 * A static object in the map that can be interacted with.
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    /**
     * Creates a new instantiated InteractiveTileobject.

     * @param world Physics world the teleporter should query
     * @param map Tiled map object will be placed in
     * @param bounds The bounds of where the object will interact with entities
     */
    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {

        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(bounds.getX() + bounds.getWidth() / 2,
                          bounds.getY() + bounds.getHeight() / 2);

        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(bounds.getWidth() / 2, bounds.getHeight() / 2);
        fdef.shape = shape;
        this.fixture = body.createFixture(fdef);

        shape.dispose();
    }
}
