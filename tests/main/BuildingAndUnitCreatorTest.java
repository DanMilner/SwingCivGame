package main;

import map.Constructable;
import map.Coordinates;
import map.Map;
import map.buildings.Building;
import map.units.Builder;
import map.units.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

public class BuildingAndUnitCreatorTest {
    private Player player;
    private UnitCreator unitCreator;
    private Map gameMap;

    @Before
    public void setUp() {
        player = new Player("Daniel", Color.yellow);
        MapData mapData = new MapData();
        mapData.setMapData(0, 0, 0, 0, 7, 0, false);
        gameMap = new Map(mapData);
        unitCreator = new UnitCreator(gameMap);
    }

    @Test
    public void createUnitTest() {
        gameMap.spawnCity(player);
        Coordinates coordinates = new Coordinates(5,5);
        gameMap.getTile(coordinates).setOwner(player);
        if(!gameMap.getTile(coordinates).hasBuilding()) {
            unitCreator.createUnit(coordinates, Constructable.BUILDER, player);
        }else{
            coordinates.setCoordinates(4,4);
            unitCreator.createUnit(coordinates, Constructable.BUILDER, player);
        }
        ArrayList<Unit> playerUnits = player.getUnits();

        Assert.assertTrue(playerUnits.get(0).getType().equals(Constructable.BUILDER));
    }

    @Test
    public void createBuildingTestTower() {
        gameMap.constructAndSetBuildingTile(Constructable.TOWER, new Coordinates(3, 3), player);

        ArrayList<Building> playerBuildings = player.getBuildings();

        Assert.assertTrue(playerBuildings.get(0).getType().equals(Constructable.TOWER));
    }

    @Test
    public void createBuildingTestRoad() {
        Unit newUnit = new Builder(player);
        Coordinates coordinates = new Coordinates(3, 3);
        gameMap.setUnit(coordinates, newUnit);

        gameMap.constructAndSetBuildingTile(Constructable.ROAD, coordinates, player);

        ArrayList<Building> playerBuildings = player.getBuildings();

        Assert.assertTrue(playerBuildings.get(0).getType().equals(Constructable.ROAD));
        Assert.assertTrue(gameMap.getTile(coordinates).getBuilding().getType().equals(Constructable.ROAD));
        Assert.assertTrue(gameMap.getTile(coordinates).getUnit().getType().equals(Constructable.BUILDER));
    }
}