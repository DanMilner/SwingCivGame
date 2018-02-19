package game.gameModel;

import game.ResourceIterator;
import game.map.buildings.Building;
import game.map.resources.ResourceTypes;
import game.map.units.Unit;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private Color colour;
    private ArrayList<Building> buildings;
    private ArrayList<Unit> units;
    private Map<ResourceTypes, Integer> resources;

    public Player(String name, Color colour) {
        this.name = name;
        this.colour = colour;
        buildings = new ArrayList<>();
        units = new ArrayList<>();
        resources = new HashMap<>();
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public Color getColour() {
        return colour;
    }

    public String getName() {
        return name;
    }

    public void setResource(ResourceTypes resourceType, int amount) {
        resources.put(resourceType, amount);
    }

    public void increaseResource(ResourceTypes resourceType, int amount) {
        amount += resources.get(resourceType);
        resources.put(resourceType, amount);
    }

    public int getResource(ResourceTypes resourceType) {
        return resources.get(resourceType);
    }

    public void resetUnitMoves() {
        for (Unit currentUnit : units) {
            currentUnit.resetMoves();
        }
    }

    public void resetResources() {
        for (ResourceTypes resourceType : ResourceTypes.values()) {
            setResource(resourceType, 0);
        }
    }

    public void refundUnitCost(Unit deadUnit) {
        ResourceIterator resourceIterator = new ResourceIterator(deadUnit);
        while (resourceIterator.hasNext()) {
            ResourceTypes resourceType = resourceIterator.getType();
            setResource(resourceType, getResource(resourceType) + resourceIterator.getValue());
        }
        units.remove(deadUnit);
    }

    private void removeBuilding(Building building) {
        buildings.remove(building);
    }

    public void destroyBuilding(Building building) {
        refundBuildingCost(building);
        removeHarvestedResources(building);
        building.releaseClaimedTiles();
        removeBuilding(building);
    }

    private void removeHarvestedResources(Building building) {
        ResourceIterator resourceIterator;

        if (building.isResourceHarvester()) {
            int multiplier = building.getHasCityConnection() ? 1 : 2;

            resourceIterator = new ResourceIterator(building, false);
            while (resourceIterator.hasNext()) {
                ResourceTypes resourceType = resourceIterator.getType();
                setResource(resourceType, getResource(resourceType) - resourceIterator.getValue() * multiplier);
            }
        }
    }

    private void refundBuildingCost(Building destroyedBuilding) {
        ResourceIterator resourceIterator = new ResourceIterator(destroyedBuilding, true);
        while (resourceIterator.hasNext()) {
            setResource(resourceIterator.getType(), resourceIterator.getValue());
        }
    }
}
