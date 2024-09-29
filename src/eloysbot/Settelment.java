package eloysbot;

import aic2024.user.Direction;
import aic2024.user.UnitController;

public class Settelment extends Building {

    public Settelment(UnitController uc, Direction[] directions, Direction randomDir) {
        super(uc, directions, randomDir);
    }

    public void run() {
        int numAstronautsPerRound = 0;
        for (Direction dir : directions) {
            if (numAstronautsPerRound > 1) {
                break;
            }
            if (uc.canEnlistAstronaut(dir, 11, null) ){
                uc.enlistAstronaut(dir, 11, null);
                numAstronautsPerRound++;
            }
        }
    }
}
