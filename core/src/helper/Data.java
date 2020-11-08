package helper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Data config
 */
public class Data {

    public HashMap<String, ArrayList<Integer>> teleporter_Position = new HashMap<>(); // hardcoded positions for 6 teleporters

    public Data(){

        ArrayList<Integer> control_rooom = new ArrayList<>();
        control_rooom.add(1808);
        control_rooom.add(2099);
        teleporter_Position.put("control_room",control_rooom);

        ArrayList<Integer> mess = new ArrayList<>();
        mess.add(1133);
        mess.add(1011);
        teleporter_Position.put("mess",mess);

        ArrayList<Integer> reactor = new ArrayList<>();
        reactor.add(1488);
        reactor.add(115);
        teleporter_Position.put("reactor",reactor);

        ArrayList<Integer> bathroom = new ArrayList<>();
        bathroom.add(1805);
        bathroom.add(1171);
        teleporter_Position.put("bathroom",bathroom);

        ArrayList<Integer> hangar = new ArrayList<>();
        hangar.add(493);
        hangar.add(115);
        teleporter_Position.put("hangar",hangar);

        ArrayList<Integer> infirmary = new ArrayList<>();
        infirmary.add(430);
        infirmary.add(2130);
        teleporter_Position.put("infirmary",infirmary);

    }

    public HashMap<String,ArrayList<Integer>> getTeleporter_Position(){
        return teleporter_Position;
    }
}
