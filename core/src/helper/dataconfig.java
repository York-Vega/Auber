package helper;

/// !!! this is one is for test puepose during dev, not useable !!!

import com.badlogic.gdx.physics.bullet.softbody.btSoftBody;

import java.util.ArrayList;

public class dataconfig { // store the position of position where the atlas region put in the game

    // control room

    ArrayList<ArrayList> controlFloor = new ArrayList<>();
    ArrayList<ArrayList> o2Tank = new ArrayList<>();
    ArrayList<ArrayList> controlPanel = new ArrayList<>();
    ArrayList<ArrayList> bottoms = new ArrayList<>();
    ArrayList<ArrayList> jail = new ArrayList<>();
    ArrayList<ArrayList> teleportControlRoom = new ArrayList<>();

    // control room -> infrimary room

    // corridor from control to inframry
    ArrayList<ArrayList> corridor_floor_c_i = new ArrayList<>(); 

    // infrimary

    ArrayList<ArrayList> infrimary_floor = new ArrayList<>();

    public dataconfig() {
        set_control_floor();
        setO_tank();
        setControl_panel();
        setbottoms();
        setJail();
        setTeleport_controlRoom();
        setCorridor_floor_c_i();
        setInfrimary_floor();

    }

    public void set_control_floor() {

        for (int x = 32; x <= 736; x += 32) { // render the floors
            for (int y = 32; y <= 608; y += 32) {
                ArrayList position = new ArrayList();
                position.add("middle");
                position.add(x);
                position.add(y);
                controlFloor.add(position);
            }
        }
    }

    public void setO_tank() {

        for (int x = 0; x <= 128; x += 32) {
            ArrayList position = new ArrayList();
            position.add("top");
            position.add(x);
            position.add(96);
            o2Tank.add(position);
        }

        ArrayList position_tlc = new ArrayList(); // topright corner, maybe replace it with a computer something to be
                                                  // abel to be sabotaged

        position_tlc.add("toprightinner");
        position_tlc.add(160);
        position_tlc.add(96);

        o2Tank.add(position_tlc);

        for (int y = 0; y <= 64; y += 32) {
            ArrayList position = new ArrayList();
            position.add("right");
            position.add(160);
            position.add(y);
            o2Tank.add(position);
        }
    }

    public void setControl_panel() {
        for (int x = 192; x <= 416; x += 32) {
            ArrayList position = new ArrayList();
            position.add("computer");
            position.add(x);
            position.add(574);
            controlPanel.add(position);
        }
    }

    public void setbottoms() {
        for (int x = 32; x <= 736; x += 32) { // bottom
            ArrayList position = new ArrayList();
            position.add("black");
            position.add(x);
            position.add(0);
            bottoms.add(position);
        }
        for (int y = 32; y <= 576; y += 32) { // left
            ArrayList position = new ArrayList();
            position.add("black");
            position.add(0);
            position.add(y);
            bottoms.add(position);
        }
        ArrayList position0 = new ArrayList(); // left_inner_bottom
        position0.add("black");
        position0.add(0);
        position0.add(0);
        bottoms.add(position0);

        ArrayList position1 = new ArrayList(); // left_inner_top
        position1.add("black");
        position1.add(0);
        position1.add(608);
        bottoms.add(position1);

        for (int x = 32; x <= 736; x += 32) { // top
            ArrayList position = new ArrayList();
            position.add("black");
            position.add(x);
            position.add(608);
            bottoms.add(position);
        }

        for (int y = 32; y <= 576; y += 32) { // right
            ArrayList position = new ArrayList();
            position.add("black");
            position.add(768);
            position.add(y);
            bottoms.add(position);
        }

        ArrayList position2 = new ArrayList(); // right_inner_top
        position2.add("black");
        position2.add(768);
        position2.add(608);
        bottoms.add(position2);

        ArrayList position3 = new ArrayList(); // right_inner_bottom
        position3.add("black");
        position3.add(768);
        position3.add(0);
        bottoms.add(position3);

    }

    public void setJail() {

        for (int y = 480; y <= 576; y += 32) {
            ArrayList position = new ArrayList();// left
            position.add("left_orange");
            position.add(608);
            position.add(y);
            jail.add(position);
        }

        ArrayList position0 = new ArrayList();// left bottom corner
        position0.add("leftbottom_inner_orange");
        position0.add(608);
        position0.add(448);
        jail.add(position0);

        for (int x = 640; x <= 736; x += 32) { // bottom
            ArrayList position = new ArrayList();
            position.add("bottom_orange");
            position.add(x);
            position.add(448);
            jail.add(position);
        }

    }

    public void setTeleport_controlRoom() {

        for (int x = 544; x <= 672; x += 32) { // bottom
            ArrayList position = new ArrayList();
            position.add("square");
            position.add(x);
            position.add(96);
            teleportControlRoom.add(position);
        }

        ArrayList position0 = new ArrayList(); // teleport
        position0.add("teleport");
        position0.add(608);
        position0.add(160);
        teleportControlRoom.add(position0);

        for (int x = 544; x <= 672; x += 32) { // top

            ArrayList position = new ArrayList();
            position.add("square");
            position.add(x);
            position.add(224);
            teleportControlRoom.add(position);
        }

        ArrayList position1 = new ArrayList(); // top left outer
        position1.add("top_left_outer");
        position1.add(576);
        position1.add(224);
        teleportControlRoom.add(position1);

        ArrayList position2 = new ArrayList(); // top right outer
        position2.add("top_right_outer");
        position2.add(640);
        position2.add(224);
        teleportControlRoom.add(position2);

        ArrayList position3 = new ArrayList();
        position3.add("middle");
        position3.add(608);
        position3.add(224);
        teleportControlRoom.add(position3);

        for (int y = 128; y <= 192; y += 32) { // left
            ArrayList position = new ArrayList();
            position.add("square");
            position.add(544);
            position.add(y);
            teleportControlRoom.add(position);
        }

        for (int y = 128; y <= 192; y += 32) { // right
            ArrayList position = new ArrayList();
            position.add("square");
            position.add(672);
            position.add(y);
            teleportControlRoom.add(position);
        }

    }

    public void setCorridor_floor_c_i() {

        for (int x = -512; x <= -32; x += 32) { // bottom black
            ArrayList position = new ArrayList();
            position.add("black");
            position.add(x);
            position.add(192);
            corridor_floor_c_i.add(position);
        }

        for (int x = -512; x <= -32; x += 32) { // top black
            ArrayList position = new ArrayList();
            position.add("black");
            position.add(x);
            position.add(320);
            corridor_floor_c_i.add(position);
        }

        for (int x = -512; x <= 0; x += 32) { // floor
            for (int y = 224; y <= 288; y += 32) {
                ArrayList position = new ArrayList();
                position.add("middle");
                position.add(x);
                position.add(y);
                corridor_floor_c_i.add(position);
            }
        }

    }

    public void setInfrimary_floor() {
        for (int x = -1024; x <= -544; x += 32) {
            for (int y = 32; y <= 480; y += 32) {
                ArrayList position = new ArrayList();
                position.add("middle");
                position.add(x);
                position.add(y);
                infrimary_floor.add(position);
            }
        }
    }

    public ArrayList<ArrayList> getO_tank() {
        return o2Tank;
    }

    public ArrayList<ArrayList> getControl_panel() {
        return controlPanel;
    }

    public ArrayList<ArrayList> getbottoms() {
        return bottoms;
    }

    public ArrayList<ArrayList> getJail() {
        return jail;
    }

    public ArrayList<ArrayList> getTeleport_control_room() {
        return teleportControlRoom;
    }

    public ArrayList<ArrayList> getControl_floor() {
        return controlFloor;
    }

    public ArrayList<ArrayList> getCorridor_floor_c_i() {
        return corridor_floor_c_i;
    }

    public ArrayList<ArrayList> getInfrimary_floor() {
        return infrimary_floor;
    }
}
