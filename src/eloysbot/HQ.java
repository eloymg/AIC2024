package eloysbot;

import aic2024.user.*;

public class HQ extends Building {
    public int numAstronautsPerRoundRatio;
    public HQ(UnitController uc, Direction[] directions, Direction randomDir) {
        super(uc, directions, randomDir);
        this.numAstronautsPerRoundRatio = 1;
    }

    public void run() {
        int numAstronautsPerRound = 0;
        int round = uc.getRound();
        if (round<20) {
            for (Direction dir : directions) {
                if (uc.canEnlistAstronaut(dir, 12, null) ){
                    uc.enlistAstronaut(dir,12, null);
                }
            }
            return;
        }
        for (Direction dir : directions) {
            if (numAstronautsPerRound > numAstronautsPerRoundRatio) {
                break;
            }
            StructureInfo s = uc.senseStructure(uc.getLocation());
            int[] carePackages = s.getCarePackages();
            CarePackage carePackage = null;
            int a = (int) (uc.getRandomDouble() * 3);
            if ((carePackages[0] > 0)&& (a==1)) {
                carePackage = CarePackage.SETTLEMENT;
            }
            if ((carePackages[1] > 0)&& (a==2)) {
                carePackage = CarePackage.DOME;
            }
            if ((carePackages[2] > 0)&& (a==3)) {
                carePackage = CarePackage.HYPERJUMP;
            }
            int oxigen = (int) ((uc.getRandomDouble() * 60)+15);
            if (uc.canEnlistAstronaut(dir, oxigen, carePackage) && uc.senseAstronauts(64, uc.getTeam()).length < 7) {
                uc.enlistAstronaut(dir, oxigen, carePackage);
                numAstronautsPerRound++;
            }
        }
        int closestPackageDistance = 1000;
        CarePackageInfo[] sensePackages = uc.senseCarePackages(48);
        CarePackageInfo closestPackage = null;
        for (CarePackageInfo c : sensePackages) {
            int PackageDistance = uc.getLocation().distanceSquared(c.getLocation());
            if (closestPackageDistance > PackageDistance) {
                closestPackageDistance = PackageDistance;
                closestPackage = c;
            }
        }
        if (closestPackage != null) {
            uc.performAction(ActionType.BROADCAST, randomDir,
                    indexDirection(uc.getLocation().directionTo(closestPackage.getLocation())));
        }

    }

}
