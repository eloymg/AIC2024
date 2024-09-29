package eloysbot;

import java.util.HashMap;

import aic2024.user.*;

public class UnitPlayer {

    Direction[] directions = Direction.values();
    HashMap<Integer, String> map = new HashMap<Integer, String>();
    int defenseRatio = 100000;
    int defcon = 0;

    public void run(UnitController uc) {
        int dirIndex = (int) (uc.getRandomDouble() * 8.0);
        Direction randomDir = directions[dirIndex];
        int round = uc.getRound();
        HQ HQ = new HQ(uc, directions, randomDir);
        Settelment Settelment = new Settelment(uc, directions, randomDir);
        Astronaut Astronaut = new Astronaut(uc, directions, randomDir, round, 64, map);
        Settler Settler = new Settler(uc, directions, randomDir, round, 64, map);
        Killer Killer = new Killer(uc, directions, randomDir, round, 64, map);
        Grower Grower = new Grower(uc, directions, randomDir, round, 64, map);
        Defense Defense = new Defense(uc, directions, randomDir, round, 0, map);
        // [X-1, 0], [0, Y-1] i [X-1, Y-1]
        Cornerer CornererUL = new Cornerer(uc, directions, randomDir, round, 25, new Location(0, 0), map);
        Cornerer CornererDL = new Cornerer(uc, directions, randomDir, round, 25, new Location(uc.getMapWidth() - 1, 0),
                map);
        Cornerer CornererUR = new Cornerer(uc, directions, randomDir, round, 25,
                new Location(0, uc.getMapHeight() - 1), map);
        Cornerer CornererDR = new Cornerer(uc, directions, randomDir, round, 25,
                new Location(uc.getMapWidth() - 1, uc.getMapHeight() - 1), map);
        Location hqLocation = new Location();
        Cornerer[] cornerers = new Cornerer[] { CornererUL, CornererDL, CornererUR, CornererDR };

        while (true) {
            if (uc.isStructure() && uc.getType() == StructureType.SETTLEMENT) {
                Settelment.run();
            }
            if (uc.isStructure() && uc.getType() == StructureType.HQ) {
                hqLocation = uc.getLocation();
                if (uc.senseAstronauts(64, uc.getOpponent()).length > 1) {
                    HQ.numAstronautsPerRoundRatio = 5;
                    defenseRatio = 1;
                    defcon = 1;
                } else {
                    if (defcon > 0) {
                        HQ.numAstronautsPerRoundRatio = 1;
                        defcon -= 1;
                        defenseRatio = 10000;
                    }
                }
                HQ.run();
            } else if (!uc.isStructure()) {
                AstronautInfo info = uc.getAstronautInfo();
                int ID = info.getID();
                CarePackage c = info.getCarePackage();
                // uc.println(info.getConstructionTurns());
                // if (info.isBeingConstructed()){
                for (StructureInfo s : uc.senseStructures(2, uc.getTeam())) {
                    if (s.getType().equals(StructureType.SETTLEMENT)) {
                        map.put(ID, "Grower");
                    }
                }
                // }
                if (c != null) {
                    if (c.equals(CarePackage.SETTLEMENT)) {
                        map.put(ID, "Settler");
                    }
                }
                String astronautType = map.get(info.getID());
                if (astronautType == null) {
                    if (round % defenseRatio == 0) {
                        map.put(ID, "Defense");
                    }
                    if (round % 31 == 0) {
                        map.put(ID, "Killer");
                    } else {
                        int a = (int) (uc.getRandomDouble() * 2);
                        if (a == 1) {
                            map.put(ID, "Astronaut");
                        } else {
                            int b = (int) (uc.getRandomDouble() * 4);
                            if (b == 1) {
                                map.put(ID, "CornererUL");
                            } else if (b == 2) {
                                map.put(ID, "CornererDL");
                            } else if (b == 3) {
                                map.put(ID, "CornererUR");
                            } else if (b == 4) {
                                map.put(ID, "CornererDR");
                            }
                        }
                        // Astronaut.run();
                    }
                } else {
                    switch (astronautType) {
                        case "Defense":
                            Defense.run(hqLocation);
                            break;
                        case "Killer":
                            Killer.run();
                            break;
                        case "CornererUL":
                            cornerers[0].run();
                            break;
                        case "CornererDL":
                            cornerers[1].run();
                            break;
                        case "CornererUR":
                            cornerers[2].run();
                            break;
                        case "CornererDR":
                            cornerers[3].run();
                            break;
                        case "Settler":
                            Settler.run();
                            break;
                        case "Grower":
                            Grower.run();
                            break;
                        default:
                            Astronaut.run();
                            break;
                    }
                }

            }
            uc.yield(); // End of turn
        }
    }
}