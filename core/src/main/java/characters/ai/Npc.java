package characters.ai;

import com.badlogic.gdx.physics.box2d.World;

public class Npc extends AiCharacter {
    public static int numberof_crew;

    /**
     * NPC object.
     *
     * @param world The game world
     * @param x the initial spawn position x
     * @param y the initial spawn position y
     */
    public Npc(World world, float x, float y) {
        super(world, x, y);
        numberof_crew++;
        super.b2body.setUserData("crew" + numberof_crew);
        super.b2body.getFixtureList().get(0).setSensor(true);
        destX = x;
        destY = y;
    }
}
