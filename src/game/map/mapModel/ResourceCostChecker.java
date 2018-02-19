package game.map.mapModel;

import exceptions.TypeNotFound;
import game.ResourceIterator;
import game.gameModel.Player;
import game.map.Constructable;
import game.map.buildings.Building;
import game.map.units.Unit;

public class ResourceCostChecker {

    public static boolean checkCost(Constructable constructable, Player owner, Boolean buildingCostCheck) {
        try {
            if (buildingCostCheck) {
                Building buildingType = TileFactory.buildBuildingTile(constructable);
                return playerHasEnoughResourcesForBuilding(buildingType, owner);
            } else {
                Unit unitType = UnitFactory.buildUnit(constructable, null);
                return playerHasEnoughResourcesForUnit(unitType, owner);
            }
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
            return false;
        }
    }

    private static boolean playerHasEnoughResourcesForUnit(Unit unit, Player owner) {
        ResourceIterator resourceIterator = new ResourceIterator(unit);
        while (resourceIterator.hasNext()) {
            if (resourceIterator.getValue() > owner.getResource(resourceIterator.getType())) {
                return false;
            }
        }
        return true;
    }

    private static boolean playerHasEnoughResourcesForBuilding(Building building, Player owner) {
        ResourceIterator resourceIterator = new ResourceIterator(building, true);
        while (resourceIterator.hasNext()) {
            if (resourceIterator.getValue() > owner.getResource(resourceIterator.getType())) {
                return false;
            }
        }
        return true;
    }
}
