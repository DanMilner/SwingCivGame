package main;

import map.buildings.*;
import org.junit.Before;
import org.junit.Test;
import units.*;

import java.awt.*;

import static org.junit.Assert.assertTrue;

public class PlayerResourceHandlerTest {
    private Player player;
    @Before
    public void setUp(){
        player = new Player("Daniel", Color.yellow);
    }

    @Test
    public void subtractUsedResources() {
        int[] resourceCostOfBuildings = new int[ResourceTypes.getNumberOfResourceTypes()];

        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new City());

        PlayerResourceHandler.subtractUsedResources(resourceCostOfBuildings, player);
        for(int i = 0; i < ResourceTypes.getNumberOfResourceTypes(); i++){
            assertTrue(player.getResource(i) == -resourceCostOfBuildings[i]);
        }
    }

    @Test
    public void calculateResourcesTestStartResourcesOnly() {
        PlayerResourceHandler.calculateResources(player);

        for(int i = 0; i < ResourceTypes.getNumberOfResourceTypes(); i++) {
            assertTrue(player.getResource(i) == 200);
        }
    }

    @Test
    public void calculateResourcesTestWithBuildings() {
        int[] resourceCostOfBuildings = new int[ResourceTypes.getNumberOfResourceTypes()];

        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new City());
        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new Tower());
        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new Mine());
        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new Dock());
        addBuildingCostAndAddBuilding(resourceCostOfBuildings, new LumberMill());

        PlayerResourceHandler.calculateResources(player);

        for(int i = 0; i < ResourceTypes.getNumberOfResourceTypes(); i++){
            assertTrue(player.getResource(i) == 200 - resourceCostOfBuildings[i]);
        }
    }

    @Test
    public void calculateResourcesTestWithUnits() {
        int[] resourceCostOfUnits = new int[ResourceTypes.getNumberOfResourceTypes()];

        addUnitCostAndAddUnit(resourceCostOfUnits, new Builder(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Knight(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Swordsman(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Archer(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Settler(player));
        addUnitCostAndAddUnit(resourceCostOfUnits, new Catapult(player));

        PlayerResourceHandler.calculateResources(player);

        for(int i = 0; i < ResourceTypes.getNumberOfResourceTypes(); i++){
            assertTrue(player.getResource(i) == 200 - resourceCostOfUnits[i]);
        }
    }

    @Test
    public void calculateResourcesTestWithUnitsAndBuildings() {
        int[] resourceCost = new int[ResourceTypes.getNumberOfResourceTypes()];

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

        for(int i = 0; i < ResourceTypes.getNumberOfResourceTypes(); i++){
            assertTrue(player.getResource(i) == 200 - resourceCost[i]);
        }
    }

    private void addBuildingCostAndAddBuilding(int[] resourceCostOfBuildings, Building newBuilding){
        int[] resourceCost = newBuilding.getResourceCost();

        for(int i = 0; i < ResourceTypes.getNumberOfResourceTypes(); i++){
            resourceCostOfBuildings[i] += resourceCost[i];
        }
        player.addBuilding(newBuilding);
    }

    private void addUnitCostAndAddUnit(int[] resourceCostOfUnits, Unit newUnit){
        int[] resourceCost = newUnit.getResourceCost();

        for(int i = 0; i < ResourceTypes.getNumberOfResourceTypes(); i++){
            resourceCostOfUnits[i] += resourceCost[i];
        }
        player.addUnit(newUnit);
    }
}