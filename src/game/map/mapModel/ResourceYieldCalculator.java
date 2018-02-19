package game.map.mapModel;

import game.gameModel.Player;
import game.map.Coordinates;
import game.map.buildings.Building;
import game.map.resources.ResourceTypes;

public class ResourceYieldCalculator {
    public static void calculateResourceYields(Coordinates coordinates, Building building, Player currentPlayer, Tile[][] currentMap) {
        int borderSize = building.getBorderSize();
        for (int i = coordinates.x - borderSize; i <= coordinates.x + borderSize; i++) {
            for (int j = coordinates.y - borderSize; j <= coordinates.y + borderSize; j++) {
                Tile tileBeingChecked = currentMap[i][j];
                if (currentMap[i][j].getOwner() != currentPlayer)
                    continue;
                if (tileBeingChecked.getResource().isInUse())
                    continue;
                if (tileBeingChecked.getResource().isHarvestable())
                    findAdjacentResourceType(tileBeingChecked, building);
            }
        }
    }

    private static void findAdjacentResourceType(Tile tileBeingChecked, Building building) {
        ResourceTypes resourceType = tileBeingChecked.getResource().getResourceType();
        if (building.canHarvestResourceType(resourceType))
            claimTileAndIncrementResource(resourceType, building, tileBeingChecked);
    }

    private static void claimTileAndIncrementResource(ResourceTypes resourceType, Building building, Tile tileBeingChecked) {
        building.increaseResourceHarvestAmount(resourceType);
        if(tileBeingChecked.isClaimed())
            tileBeingChecked.getClaimedBy().releaseClaimedTile(tileBeingChecked);
        tileBeingChecked.setClaimedBy(building);
        building.claimTile(tileBeingChecked);
        tileBeingChecked.getResource().setInUse(true);
    }
}
