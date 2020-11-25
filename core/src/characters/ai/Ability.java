package characters.ai;

import characters.Player;
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

    }

    /**
     * Generate a random ability for enemy.

     * @param enemy The enemy who should use their ability
     * @param player The player to target
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
          default:
              break;
        }

    }

    /**
     * provoke ability status.
     */
    public void provokeAbility() {
        if (ready && !disabled) {
            useTime = 30f;
            inUse = true;
            ready = false;
        }
    }

    /**
     * cool down timer.

     * @param delta delta time
     * @param enemy Enemy
     */
    public void update(float delta, Enemy enemy) {
        if (inUse) {
            if (useTime >= delta) {
                useTime -= delta;
            } else {
                removeAbility(enemy);
                inUse = false;
                cooldownTime = 30;
            }
        } else if (!ready) {
            if (cooldownTime >= delta) {
                cooldownTime -= delta;
            } else {
                ready = true;
            }
        }
    }

    /**
     * Disable ability.
     *
     * @param disable true when arrested
     */
    public void setDisable(boolean disable) {
        disabled = disable;
    }

    /**
     * set the target.
     *
     * @param auber Player
     */
    public void setTarget(Player auber) {
        this.target = auber;
    }


    /**
     * Slow down player's speed.

     * @param auber The player character to slow
     */
    public void slowDownPlayer(Player auber) {

        float currentSpeed = auber.speed;
        auber.speed = currentSpeed * .5f;

    }

    /**
     * Increase speed for enemy.

     * @param enemy the enemy who sould use the speed ability
     */
    public void speeding(Enemy enemy) {

        float currentSpeed = enemy.speed;
        enemy.speed = currentSpeed * 3f;
        if (target != null) {
            enemy.setDest(target.b2body.getPosition().x - 400, target.b2body.getPosition().y);
        }

    }


    /**
     * Make damage to player.

     * @param player Player to attack
     */
    public void attackPlayer(Player player) {

        float currentHp = player.health;
        player.health = currentHp - 10f;

    }

    /**
     * remove the ability effect.
     *
     * @param enemy Enemy
     */
    public void removeAbility(Enemy enemy) {
        enemy.speed = 1000f;
        if (target != null) {
            target.speed = 60f;
        }
    }


}
