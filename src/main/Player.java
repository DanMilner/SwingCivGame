package main;

import map.buildings.Building;
import units.Unit;

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
        deadUnit.setUpResourceIterator();
        while (deadUnit.hasNextResourceCost()) {
            ResourceTypes resourceType = deadUnit.getNextType();
            int value = deadUnit.getNextValue();
            resources.put(resourceType, value);
        }
        units.remove(deadUnit);
    }

    public void refundBuildingCost(Building destroyedBuilding) {
        destroyedBuilding.setUpResourceIterator();
        while (destroyedBuilding.hasNextResourceCost()) {
            setResource(destroyedBuilding.getNextType(), destroyedBuilding.getNextValue());
        }

        int multiplier = 1;
        if (destroyedBuilding.getHasCityConnection()) {
            multiplier = 2;
        }

        destroyedBuilding.setUpHarvestedResourcesIterator();
        while (destroyedBuilding.hasNextResourceCost()) {
            ResourceTypes resourceType = destroyedBuilding.getNextType();
            setResource(resourceType, getResource(resourceType) - destroyedBuilding.getNextValue() * multiplier);
        }

        destroyedBuilding.releaseClaimedTiles();
        buildings.remove(destroyedBuilding);
    }
}
