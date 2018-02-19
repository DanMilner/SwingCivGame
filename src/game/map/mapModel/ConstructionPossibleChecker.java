package game.map.mapModel;

import game.gameModel.Player;
import game.map.Constructable;
import game.map.Coordinates;
import game.map.resources.ResourceTypes;

public class ConstructionPossibleChecker {
        public static boolean isConstructionPossible(Tile[][] currentMap, Constructable buildingType, Coordinates coordinates, Player owner) {
            if (buildingType == Constructable.DOCK) {
                if (!checkForNearbyWater(currentMap, coordinates))
                    return false;
            }
            Tile candidateTile = currentMap[coordinates.x][coordinates.y];

            if (isTileInEnemyTerritory(candidateTile, owner))
                return false;

            if (doesTileAlreadyHaveABuilding(candidateTile))
                return false;

            return !doesTileHaveAResourceInUse(candidateTile);
        }

        private static boolean isTileInEnemyTerritory(Tile candidateTile, Player tileOwner) {
            if (candidateTile.hasOwner() && candidateTile.getOwner() != tileOwner) {
                System.out.println("Cannot build in another players territory");
                return true;
            }
            return false;
        }

        private static boolean doesTileAlreadyHaveABuilding(Tile candidateTile) {
            if (candidateTile.hasBuilding() && !candidateTile.getBuilding().getType().equals(Constructable.ROAD)) {
                System.out.println("Cannot build on top of another building");
                return true;
            }
            return false;
        }

        private static boolean doesTileHaveAResourceInUse(Tile candidateTile) {
            if (candidateTile.getResource().isInUse()) {
                System.out.println("Cannot build on top of a resource being harvested");
                return true;
            }
            return false;
        }

        private static boolean checkForNearbyWater(Tile[][] currentMap, Coordinates coordinates) {
            final int DOCK_SIZE = 1;

            for (int x = coordinates.x - DOCK_SIZE; x <= coordinates.x + DOCK_SIZE; x++) {
                for (int y = coordinates.y - DOCK_SIZE; y <= coordinates.y + DOCK_SIZE; y++) {
                    if (x <= currentMap.length && x >= 0 && y <= currentMap.length && y >= 0) {
                        if (currentMap[x][y].getResource().getResourceType() == ResourceTypes.WATER) {
                            return true;
                        }
                    }
                }
            }
            System.out.println("Cannot build dock. There is no water nearby");
            return false;
        }
}
