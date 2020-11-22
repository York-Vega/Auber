package characters.ai;

import characters.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.sql.Time;
import java.util.Random;

public class Ability {

    public boolean disabled;
    public int randomIndex;
    public Player target;
    public float useTime = 30;
    public float cooldownTime = 30;
    public boolean ready;
    public boolean inUse;

    /**
     * Special Ability Enemy should have.
     *
     */
    public Ability() {

        inUse = false;
        ready = true;
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
    public void provokeAbility() {
        if (ready && !disabled) {
            useTime = 30f;
            inUse = true;
            ready = false;
        }
    }

    public void update(float delta, Enemy enemy) {
        if (inUse) {
            if (useTime >= delta) {
                System.out.println("Use Time: " + useTime);
                useTime -= delta;
            } else {
                removeAbility(enemy);
                inUse = false;
                cooldownTime = 30;
                System.out.println("Starting Cooldown");
            }
        } else if (!ready) {
            if (cooldownTime >= delta) {
                System.out.println("cooldown time: " + cooldownTime);
                cooldownTime -= delta;
            } else {
                ready = true;
                System.out.println("Ability Ready");
            }
        }
    }

    /**
     * Disable ability
     * @param disable true when arrested
     */
    public void setDisable(boolean disable) {
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
        auber.speed = currentSpeed * .5f ;
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
