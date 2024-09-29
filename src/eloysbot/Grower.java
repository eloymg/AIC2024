package eloysbot;

import java.util.HashMap;

import aic2024.user.*;

public class Grower extends Astronaut {
    public Grower(UnitController uc, Direction[] directions, Direction randomDir, int round, int bounceRadius,  HashMap<Integer, String> map) {
        super(uc, directions, randomDir, round, bounceRadius, map);
    }

    public void run() {
        retrieveGrowPackages();
        moveToClosestGrowerPackage();
        defaultPostActions();
    }

      public boolean retrieveGrowPackages() {
        for (Direction dir : directions) {
            Location adjLocation = uc.getLocation().add(dir);
            if (!uc.canSenseLocation(adjLocation))
                continue;
            CarePackage cp = uc.senseCarePackage(adjLocation);
            if (cp != null && ((cp.equals(CarePackage.PLANTS)) || (cp.equals(CarePackage.OXYGEN_TANK)))) {
                if (uc.canPerformAction(ActionType.RETRIEVE, dir, 0)) {
                    uc.performAction(ActionType.RETRIEVE, dir, 0);
                    return true;
                }
            }
        }
        return false;
    }
    public void moveToClosestGrowerPackage(){
        int dirIndex = (int) (uc.getRandomDouble() * 8.0);
        Direction randomDir = directions[dirIndex];
        CarePackageInfo closestPackage = getClosestGrowPackage();
        if (closestPackage == null) {
            if (uc.getLocation().distanceSquared(getMapCenter()) < bounceRadius) {
                move(randomDir);
                return;
            }
            move(uc.getLocation().directionTo(getMapCenter()));
        } else {
            Direction closestPackageDirection = uc.getLocation().directionTo(closestPackage.getLocation());
            move(closestPackageDirection);
        }
    }
    
    public CarePackageInfo getClosestGrowPackage() {
        int closestPackageDistance = 1000;
        CarePackageInfo[] sensePackages = uc.senseCarePackages(25);
        CarePackageInfo closestPackage = null;
        for (CarePackageInfo c : sensePackages) {
            int PackageDistance = uc.getLocation().distanceSquared(c.getLocation());
            if ((closestPackageDistance > PackageDistance) && ((c.getCarePackageType().equals(CarePackage.PLANTS)) || (c.getCarePackageType().equals(CarePackage.OXYGEN_TANK)))) {
                closestPackageDistance = PackageDistance;
                closestPackage = c;
            }
        }
        return closestPackage;
    }
}
