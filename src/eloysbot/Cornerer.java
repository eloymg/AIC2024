package eloysbot;

import java.util.HashMap;

import aic2024.user.*;

public class Cornerer extends Astronaut {
    public Location corner;

    public Cornerer(UnitController uc, Direction[] directions, Direction randomDir, int round, int bounceRadius,
            Location corner,  HashMap<Integer, String> map) {
        super(uc, directions, randomDir, round, bounceRadius,map);
        this.corner = corner;
    }

    public void run() {
        retrievePackages();
        if (uc.getLocation().equals(corner)) {
            map.put(uc.getID(),"Astronaut");
        } else {
            moveToClosestPackageBouncingToCenter(corner);
        }
        defaultPostActions();
    }
}
