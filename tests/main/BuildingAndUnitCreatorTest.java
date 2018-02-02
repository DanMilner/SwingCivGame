package main;

import map.Constructable;
import map.Map;
import map.buildings.Building;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import map.units.Builder;
import map.units.Unit;

import java.awt.*;
import java.util.ArrayList;

public class BuildingAndUnitCreatorTest {
    private Player player;
    private BuildingAndUnitCreator buildingAndUnitCreator;
    private Map gameMap;

    @Before
    public void setUp() {
        player = new Player("Daniel", Color.yellow);
        gameMap = new Map(true, 7);
        buildingAndUnitCreator = new BuildingAndUnitCreator(gameMap);
    }

    @Test
    public void createUnitTest() {
        gameMap.spawnCity(player);

        ButtonData data = new ButtonData(3, 3, Constructable.BUILDER);
        buildingAndUnitCreator.createUnit(data, player);
        ArrayList<Unit> playerUnits = player.getUnits();

        Assert.assertTrue(playerUnits.get(0).getType().equals(Constructable.BUILDER));
    }

    @Test
    public void createBuildingTestTower() {
        ButtonData data = new ButtonData(3, 3, Constructable.TOWER);
        buildingAndUnitCreator.createBuilding(data, player);

        ArrayList<Building> playerBuildings = player.getBuildings();

        Assert.assertTrue(playerBuildings.get(0).getType().equals(Constructable.TOWER));
    }

    @Test
    public void createBuildingTestRoad() {
        ButtonData data = new ButtonData(3, 3, Constructable.ROAD);
        Unit newUnit = new Builder(player);
        gameMap.setUnit(3, 3, newUnit);

        buildingAndUnitCreator.createBuilding(data, player);

        ArrayList<Building> playerBuildings = player.getBuildings();

        Assert.assertTrue(playerBuildings.get(0).getType().equals(Constructable.ROAD));
        Assert.assertTrue(gameMap.getTile(3, 3).getBuilding().getType().equals(Constructable.ROAD));
        Assert.assertTrue(gameMap.getTile(3, 3).getUnit().getType().equals(Constructable.BUILDER));
    }
}