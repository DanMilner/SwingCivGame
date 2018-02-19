package game.gameModel;

import game.ResourceIterator;
import game.map.buildings.Building;
import game.map.resources.ResourceTypes;
import game.map.units.Unit;

public class PlayerResourceHandler {

    private static void subtractUsedResourcesUnit(Unit newUnit, Player player) {
        ResourceIterator resourceIterator = new ResourceIterator(newUnit);
        while (resourceIterator.hasNext()) {
            ResourceTypes ResourceType = resourceIterator.getType();
            player.setResource(ResourceType, player.getResource(ResourceType) - resourceIterator.getValue());
        }
    }

    private static void subtractUsedResourcesBuilding(Building building, Player player) {
        ResourceIterator resourceIterator = new ResourceIterator(building, true);
        while (resourceIterator.hasNext()) {
            ResourceTypes ResourceType = resourceIterator.getType();
            player.setResource(ResourceType, player.getResource(ResourceType) - resourceIterator.getValue());
        }
    }

    public static void calculateResources(Player player) {
        ResourceIterator resourceIterator;
        player.resetResources();

        giveStartingResources(player);

        for (Building currentBuilding : player.getBuildings()) {
            if (currentBuilding.isResourceHarvester()) {
                resourceIterator = new ResourceIterator(currentBuilding, false);
                while (resourceIterator.hasNext()) {
                    ResourceTypes resourceType = resourceIterator.getType();
                    player.increaseResource(resourceType, currentBuilding.getResourceAmount(resourceType));
                }
            }
            subtractUsedResourcesBuilding(currentBuilding, player);
        }

        for (Unit currentUnit : player.getUnits()) {
            subtractUsedResourcesUnit(currentUnit, player);
        }
    }

    private static void giveStartingResources(Player player) {
        for (ResourceTypes resourceType : ResourceTypes.values()) {
            player.increaseResource(resourceType, 200);
        }
    }
}
