package main;

import map.buildings.Building;
import map.resources.ResourceTypes;
import map.units.Unit;

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

    ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    ArrayList<Unit> getUnits() {
        return units;
    }

    Color getColour() {
        return colour;
    }

    String getName() {
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

    void resetUnitMoves() {
        for (Unit currentUnit : units) {
            currentUnit.resetMoves();
        }
    }

    void resetResources() {
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

    public void refundBuildingCost(Building destroyedBuilding) {
        ResourceIterator resourceIterator = new ResourceIterator(destroyedBuilding, true);
        while (resourceIterator.hasNext()) {
            setResource(resourceIterator.getType(), resourceIterator.getValue());
        }

        if (destroyedBuilding.isResourceHarvester()) {
            int multiplier = destroyedBuilding.getHasCityConnection() ? 1 : 2;

            resourceIterator = new ResourceIterator(destroyedBuilding, false);
            while (resourceIterator.hasNext()) {
                ResourceTypes resourceType = resourceIterator.getType();
                setResource(resourceType, getResource(resourceType) - resourceIterator.getValue() * multiplier);
            }
            destroyedBuilding.releaseClaimedTiles();
        }

        buildings.remove(destroyedBuilding);
    }
}
