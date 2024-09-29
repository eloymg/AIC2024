package eloysbot;

import java.util.HashMap;

import aic2024.user.*;

public class Astronaut extends GameObject {
    public int round;
    public int bounceRadius;
    public HashMap<Integer, String> map;

    public Astronaut(UnitController uc, Direction[] directions, Direction randomDir, int round, int bounceRadius,
            HashMap<Integer, String> map) {
        super(uc, directions, randomDir);
        this.round = round;
        this.map = map;
        this.bounceRadius = bounceRadius;
    }

    public void run() {
        retrievePackages();
        if (round > 50) {
            moveToClosestPackageBouncingToCenter(getMapCenter());
        } else {
            moveToLocation(getMapCenter());
        }
        defaultPostActions();
    }

    public void scanHQ() {

    }

    public void defaultPostActions() {
        int buildTrigger = 10;
        if (round > 50) {
            buildTrigger = 2;
        } else if (round > 30) {
            buildTrigger = 5;
        } else if (round > 10) {
            buildTrigger = 8;
        }
        if (uc.getAstronautInfo().getOxygen() <= buildTrigger) {
            buildDome(randomDir);
            //buildSettlement(randomDir);
            buildHJ(randomDir);
            terraform(randomDir);
        }
    }

    public void moveToClosestPackageBouncingTo(Location l, Location bouncingLocation) {
        int dirIndex = (int) (uc.getRandomDouble() * 8.0);
        Direction randomDir = directions[dirIndex];
        CarePackageInfo closestPackage = getClosestPackage();
        if (closestPackage == null) {
            if (uc.getLocation().distanceSquared(l) < bounceRadius) {
                move(randomDir);
                return;
            }
            move(uc.getLocation().directionTo(l));
        } else {
            Direction closestPackageDirection = uc.getLocation().directionTo(closestPackage.getLocation());
            move(closestPackageDirection);
        }
    }

    public void moveToClosestPackageBouncingToCenter(Location l) {
        moveToClosestPackageBouncingTo(l, getMapCenter());
    }

    public void move(Direction dir) {
        for (int i = 0; i < 8; ++i) {
            if (uc.canPerformAction(ActionType.JUMP, dir, 0)) {
                uc.performAction(ActionType.JUMP, dir, 0);
            }
            if (uc.canPerformAction(ActionType.MOVE, dir, 0)) {
                uc.performAction(ActionType.MOVE, dir, 0);
            }
            if ((int) (uc.getRandomDouble() * 2) == 1){
                dir = dir.rotateRight();
            }else{
                dir = dir.rotateLeft();
            }

        }
    }

    public void moveToLocation(Location loc) {
        Direction dir = uc.getLocation().directionTo(loc);
        move(dir);
    }

    public void action(Direction dir, ActionType action) {
        for (int i = 0; i < 8; ++i) {
            if (uc.canPerformAction(action, dir, 0)) {
                uc.performAction(action, dir, 0);
            }
            dir = dir.rotateRight();
        }
    }

    public void terraform(Direction dir) {
        action(dir, ActionType.TERRAFORM);
    }

    public void buildDome(Direction dir) {
        action(dir, ActionType.BUILD_DOME);
    }

    public void buildHJ(Direction dir) {
        action(dir, ActionType.BUILD_HYPERJUMP);
    }

    public void buildSettlement(Direction dir) {
        action(dir, ActionType.BUILD_SETTLEMENT);
    }

    public boolean retrievePackages() {
        for (Direction dir : directions) {
            Location adjLocation = uc.getLocation().add(dir);
            if (!uc.canSenseLocation(adjLocation))
                continue;
            CarePackage cp = uc.senseCarePackage(adjLocation);
            if (cp != null && !((cp.equals(CarePackage.RADIO)))) {
                if (uc.canPerformAction(ActionType.RETRIEVE, dir, 0)) {
                    uc.performAction(ActionType.RETRIEVE, dir, 0);
                    return true;
                }
            }
        }
        return false;
    }

    public CarePackageInfo getClosestPackage() {
        int closestPackageDistance = 1000;
        CarePackageInfo[] sensePackages = uc.senseCarePackages(25);
        CarePackageInfo closestPackage = null;
        for (CarePackageInfo c : sensePackages) {
            int PackageDistance = uc.getLocation().distanceSquared(c.getLocation());
            if ((closestPackageDistance > PackageDistance) && !((c.getCarePackageType().equals(CarePackage.RADIO)))) {
                closestPackageDistance = PackageDistance;
                closestPackage = c;
            }
        }
        return closestPackage;
    }

    public Location getMapCenter() {
        int h = uc.getMapHeight();
        int w = uc.getMapWidth();
        return new Location(h / 2, w / 2);
    }
}
