package eloysbot;

import aic2024.user.Direction;
import aic2024.user.UnitController;

public class GameObject {
    public UnitController uc;
    public Direction[] directions;
    public Direction randomDir;

    public GameObject(UnitController uc, Direction[] directions, Direction randomDir) {
        this.uc = uc;
        this.directions = directions;
        this.randomDir = randomDir;
    }

    public void run() {

    }
}
