package eloysbot;

import aic2024.user.Direction;
import aic2024.user.UnitController;

public class Building  extends GameObject{
    public Building(UnitController uc, Direction[] directions, Direction randomDir) {
        super(uc, directions, randomDir);
     }
    public int indexDirection(Direction d) {
        int i = 0;
        for (Direction dir : directions) {
            if (dir == d) {
                return i;
            }
            i++;
        }
        return 0;
    }

}
