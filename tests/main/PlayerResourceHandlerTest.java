package main;

import map.buildings.*;
import org.junit.Before;
import org.junit.Test;
import units.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class PlayerResourceHandlerTest {
    private Player player;

    @Before
    public void setUp() {
        player = new Player("Daniel", Color.yellow);
    }

    @Test
    public void subtractUsedResources() {
        Building city = new City();

        player.addBuilding(city);
        PlayerResourceHandler.calculateResources(player);
        ResourceIterator resourceIterator = new ResourceIterator(city, true);
        while (resourceIterator.hasNext()) {
            assertTrue(player.getResource(resourceIterator.getType()) == 200 - resourceIterator.getValue());
        }
    }

    @Test
    public void calculateResourcesTestStartResourcesOnly() {
        PlayerResourceHandler.calculateResources(player);

        for (ResourceTypes resourceType : ResourceTypes.values()) {
            assertTrue(player.getResource(resourceType) == 200);
        }
    }

    @Test
    public void calculateResourcesTestWithBuildings() {
        Map<ResourceTypes, Integer> resourceCostOfBuildings = new HashMap<>();

        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new City());
        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new Tower());
        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new Mine());
        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new Dock());
        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new LumberMill());

        PlayerResourceHandler.calculateResources(player);

        for (ResourceTypes resourceType : ResourceTypes.values()) {
            if (resourceCostOfBuildings.containsKey(resourceType))
                assertTrue(player.getResource(resourceType) == 200 - resourceCostOfBuildings.get(resourceType));
        }
    }

    @Test
    public void calculateResourcesTestWithUnits() {
        Map<ResourceTypes, Integer> resourceCostOfUnits = new HashMap<>();

        addUnitCostAndAddUnit(resourceCostOfUnits, new Builder(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Knight(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Swordsman(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Archer(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Settler(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Catapult(player));

        PlayerResourceHandler.calculateResources(player);

        for (ResourceTypes resourceType : ResourceTypes.values()) {
            if (resourceCostOfUnits.containsKey(resourceType))
                assertTrue(player.getResource(resourceType) == 200 - resourceCostOfUnits.get(resourceType));
        }
    }

    @Test
    public void calculateResourcesTestWithUnitsAndBuildings() {
        Map<ResourceTypes, Integer> resourceCost = new HashMap<>();

        addUnitCostAndAddUnit(resourceCost, new Builder(player));
        addUnitCostAndAddUnit(resourceCost, new Knight(player));
        addUnitCostAndAddUnit(resourceCost, new Swordsman(player));
        addUnitCostAndAddUnit(resourceCost, new Archer(player));
        addUnitCostAndAddUnit(resourceCost, new Settler(player));
        addUnitCostAndAddUnit(resourceCost, new Catapult(player));
        addBuildingCostAndAddBuilding(resourceCost, new City());
        addBuildingCostAndAddBuilding(resourceCost, new Tower());
        addBuildingCostAndAddBuilding(resourceCost, new Mine());
        addBuildingCostAndAddBuilding(resourceCost, new Dock());
        addBuildingCostAndAddBuilding(resourceCost, new LumberMill());

        PlayerResourceHandler.calculateResources(player);

        for (ResourceTypes resourceType : ResourceTypes.values()) {
            if (resourceCost.containsKey(resourceType))
                assertTrue(player.getResource(resourceType) == 200 - resourceCost.get(resourceType));
        }
    }

    private void addUnitCostAndAddUnit(Map<ResourceTypes, Integer> resourceCost, Unit newUnit){
        ResourceIterator resourceIterator = new ResourceIterator(newUnit);

        addCost(resourceCost, resourceIterator);

        player.addUnit(newUnit);
    }

    private void addBuildingCostAndAddBuilding(Map<ResourceTypes, Integer> resourceCost, Building newBuilding) {
        ResourceIterator resourceIterator = new ResourceIterator(newBuilding, true);

        addCost(resourceCost, resourceIterator);

        player.addBuilding(newBuilding);
    }

    private void addCost(Map<ResourceTypes, Integer> resourceCost, ResourceIterator resourceIterator) {
        while (resourceIterator.hasNext()) {
            ResourceTypes resourceType = resourceIterator.getType();
            if (resourceCost.containsKey(resourceType)) {
                resourceCost.put(resourceType, resourceIterator.getValue() + resourceCost.get(resourceType));
            } else {
                resourceCost.put(resourceType, resourceIterator.getValue());
            }
        }
    }
}