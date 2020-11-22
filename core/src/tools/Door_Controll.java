package tools;

import java.util.ArrayList;
import java.util.Random;

import screen.Gameplay;
import sprites.Door;
import sprites.Systems;

public abstract class Door_Controll {
    static private int MAX_DOORS_TO_LOCK = 3;
    static private float MAX_TIME = 30;
    static private float time = MAX_TIME;
    static private boolean isSabotaged;
    static private ArrayList<Door> doors;

    public static void updateDoors(ArrayList<Systems> systems, float delta) {
        if (!isSabotaged) {
            if(isDoorsSabotaged(systems)) {
                if (time == MAX_TIME) {
                    sabotage();
                } else {
                    time = MAX_TIME;
                }                
            }
        } else if (time >= delta) {
            //System.out.println("Locked for: " + time + "s");
            time -= delta;            
        } else {
            unsabotage();
        }
    }

    private static boolean isDoorsSabotaged(ArrayList<Systems> systems) {
        for (Systems sys : systems){
            if (sys.sys_name.equals("doors") ){
                if (sys.is_sabotaged()){                    
                    isSabotaged = true;
                    return true;
                }
                return  false;
            }
        }
        return false;
    }

    private static void sabotage() {
        doors = new ArrayList<Door>();
        for (int i=0; i < MAX_DOORS_TO_LOCK; i++) {
            Random r = new Random();
            int index = r.nextInt(Gameplay.doors.size());
            index = 7;
            Door door = Gameplay.doors.get(index);
            if (!door.isLocked()) {
                door.lock();
                doors.add(door);
            }
        }
    }

    private static void unsabotage() {
        for (Door door: doors) {
            door.unlock();
        }
    }

     
}
