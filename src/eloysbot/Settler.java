package eloysbot;

import java.util.HashMap;

import aic2024.user.*;

public class Settler extends Astronaut {
    public Settler(UnitController uc, Direction[] directions, Direction randomDir, int round, int bounceRadius,
            HashMap<Integer, String> map) {
        super(uc, directions, randomDir, round, bounceRadius, map);
    }
    public void run() {
        moveToLocation(getMapCenter());
        StructureInfo[] s = uc.senseStructures(25,uc.getTeam());
        for (StructureInfo structure : s){
            if (structure.getType().equals(StructureType.SETTLEMENT)){
                if (uc.canPerformAction(ActionType.TRANSFER_OXYGEN, uc.getLocation().directionTo(structure.getLocation()), 0)){
                    uc.performAction(ActionType.TRANSFER_OXYGEN, uc.getLocation().directionTo(structure.getLocation()), 0);
                }else{
                    moveToLocation(structure.getLocation());
                }
            }
        }
        if (uc.senseTileType(uc.getLocation()).equals(TileType.HOT_ZONE)){
            buildSettlement(randomDir);
        }
        defaultPostActions();
    }

}
