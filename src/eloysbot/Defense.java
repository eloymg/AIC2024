package eloysbot;

import java.util.HashMap;

import aic2024.user.*;
public class Defense extends Astronaut {
    public Defense(UnitController uc, Direction[] directions, Direction randomDir, int round, int bounceRadius, HashMap<Integer, String> map) {
        super(uc, directions, randomDir, round, bounceRadius,map);
    }

    public void run(Location hqLocation) {
        int closestEnemyAstronautDistance = 1000;
        AstronautInfo[] enemyAstronauts = uc.senseAstronauts(25, uc.getOpponent());
        AstronautInfo closestEnemyAstronaut = null;
        for (AstronautInfo a : enemyAstronauts) {
            int enemyAstronautDistance = uc.getLocation().distanceSquared(a.getLocation());
            if (closestEnemyAstronautDistance > enemyAstronautDistance) {
                closestEnemyAstronautDistance = enemyAstronautDistance;
                closestEnemyAstronaut = a;
            }
        }
        if (closestEnemyAstronaut == null) {
            if (uc.getLocation().distanceSquared(hqLocation)> 48){
                move(uc.getLocation().directionTo(hqLocation));
            }
            move(randomDir);
        } else {
            Direction closestEnemyAstronautDirection = uc.getLocation()
                    .directionTo(closestEnemyAstronaut.getLocation());
            if (uc.canPerformAction(ActionType.SABOTAGE, closestEnemyAstronautDirection, 0)) {
                uc.performAction(ActionType.SABOTAGE, closestEnemyAstronautDirection, 0);
            } else {
                move(closestEnemyAstronautDirection);
            }
        }
        defaultPostActions();
    }
}
