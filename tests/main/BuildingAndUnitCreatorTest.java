package main;

import map.Map;
import map.buildings.Building;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import units.Builder;
import units.Unit;

import java.awt.*;
import java.util.ArrayList;

public class BuildingAndUnitCreatorTest {
    private Player player;
    private BuildingAndUnitCreator buildingAndUnitCreator;
    private Map gameMap;
    @Before
    public void setUp(){
        player = new Player("Daniel", Color.yellow);
        gameMap = new Map(true, 7);
        buildingAndUnitCreator = new BuildingAndUnitCreator(gameMap);
    }

    @Test
    public void createUnitTest() {
        gameMap.spawnCity(player);

        ButtonData data = new ButtonData(3, 3, "Builder");
        buildingAndUnitCreator.createUnit(data,player);
        ArrayList<Unit> playerUnits = player.getUnits();

        Assert.assertTrue(playerUnits.get(0).getType().equals("Builder"));
    }

    @Test
    public void createBuildingTestTower() {
        ButtonData data = new ButtonData(3, 3, "Tower");
        buildingAndUnitCreator.createBuilding(data, player);

        ArrayList<Building> playerBuildings = player.getBuildings();

        Assert.assertTrue(playerBuildings.get(0).getType().equals("Tower"));
    }

    @Test
    public void createBuildingTestRoad() {
        ButtonData data = new ButtonData( 3, 3, "Road");
        Unit newUnit = new Builder(player);
        gameMap.setUnit(3,3,newUnit);

        buildingAndUnitCreator.createBuilding(data, player);

        ArrayList<Building> playerBuildings = player.getBuildings();

        Assert.assertTrue(playerBuildings.get(0).getType().equals("Road"));
        Assert.assertTrue(gameMap.getTile(3,3).getBuilding().getType().equals("Road"));
        Assert.assertTrue(gameMap.getTile(3,3).getUnit().getType().equals("Builder"));
    }
}