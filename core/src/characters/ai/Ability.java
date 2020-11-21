package characters.ai;

import characters.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class Ability {

    public boolean provked;
    public boolean disabled;
    public int randomIndex;
    public Player target;
    /**
     * Special Ability Enemy should have.
     *
     */
    public Ability() {

        provked = false;
        disabled = false;
        // give a random ability to enemy
        Random random = new Random();
        randomIndex = random.nextInt(4);
        //randomIndex = 1;

    }

    /**
     * Generate a random ability for enemy.
     */
    public void useAbility(Enemy enemy, Player player) {

        switch (randomIndex) {
            case 0:
                slowDownPlayer(player);
                break;
            case 1:
                speeding(enemy);
                break;
            case 2:
                attackPlayer(player);
                break;
            case 3:
                System.out.println("enemy not given ability");
                break;
        }

    }

    /**
     * provoke ability status
     * @param provke should be set in contact listener
     */
    public void provokeAbility(boolean provke) {
        provked = provke;
    }

    /**
     * Disable ability
     * @param disable true when arrested
     */
    public void disableAbility(boolean disable){
        disabled = disable;
    }


    public void setTarget(Player auber){
        this.target = auber;
    }


    /**
     * Slow down player's speed.
     */
    public void slowDownPlayer(Player auber) {

        float currentSpeed = auber.speed;
        auber.speed = currentSpeed * .5f * Gdx.graphics.getDeltaTime();
        Gdx.app.log("Auber:","Has been slow down");
    }

    /**
     * Increase speed for enemy.
     */
    public void speeding(Enemy enemy) {

        float currentSpeed = enemy.speed;
        enemy.speed = currentSpeed * 3f ;
        if (target != null){
            enemy.setDest(target.b2body.getPosition().x-400,target.b2body.getPosition().y);
        }

        Gdx.app.log("Enemy:","has speeding");
    }


    /**
     * Make damage to player.
     */
    public void attackPlayer(Player player) {

        float currentHp = player.health;
        player.health = currentHp - 1f * Gdx.graphics.getDeltaTime();
        Gdx.app.log("Auber:","Has been attacked");
    }

    /**
     * Become Invisible during contact.
     */
    public void invisible(Enemy enemy) {
        // Can't figure out how to hide the enemy as it is not a sprite
        // this is alternative option, transform enemy out of viewport like Teleport...
        enemy.b2body.setTransform(9999, 9999, 0);

    }

    /**
     * enemy will kill one NPC if they contact
     */
    public void sabotageNpc() {

    }


    public void removeAbility(Enemy enemy){
        enemy.speed = 1000f;
        if (target != null){
            target.speed = 60f;
        }

        Gdx.app.log("enemy:","ability removed");
    }



}
