package game.map;

import game.MapData;
import game.gameModel.Player;
import game.ResourceIterator;
import game.map.buildings.Mine;
import game.map.mapModel.Map;
import game.map.resources.Forest;
import game.map.resources.Mountain;
import game.map.resources.ResourceTypes;
import game.map.resources.Water;
import game.map.units.Builder;
import game.map.units.Knight;
import game.map.units.Unit;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapTest {
    private Player player;
    private Map gameMap;

    @Before
    public void setUp() {
        MapData mapData = new MapData();
        mapData.setMapData(0,0,0,0,30, 0, false);
        gameMap = new Map(mapData);
        player = new Player("Daniel", Color.yellow);
    }

    @Test
    public void spawnCity() {
        gameMap.spawnCity(player);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (gameMap.getTile(new Coordinates(x, y)).hasBuilding()) {
                    assertTrue(gameMap.getTile(new Coordinates(x, y)).getBuilding().getType().equals(Constructable.CITY));
                }
            }
        }
    }

    @Test
    public void constructAndSetBuildingTileTest() {
        gameMap.constructAndSetBuildingTile(Constructable.MINE, new Coordinates(5, 5), player);

        assertTrue(gameMap.getTile(new Coordinates(5, 5)).getBuilding().getType().equals(Constructable.MINE));
    }

    @Test
    public void constructAndSetBuildingTileTestConstructionNotPossible() {
        gameMap.getTile(new Coordinates(5, 5)).setBuilding(new Mine());
        gameMap.constructAndSetBuildingTile(Constructable.TOWER, new Coordinates(5, 5), player);

        assertFalse(gameMap.getTile(new Coordinates(5, 5)).getBuilding().getType().equals(Constructable.TOWER));
    }

    @Test
    public void constructAndSetBuildingTileTestResourcesHarvested() {
        gameMap.setTileResource(new Coordinates(4, 4), new Forest());
        gameMap.constructAndSetBuildingTile(Constructable.LUMBERMILL, new Coordinates(5, 5), player);

        assertTrue(gameMap.getTile(new Coordinates(5, 5)).getBuilding().getResourceAmount(ResourceTypes.WOOD) == 1);
    }

    @Test
    public void killUnitAndRefundCostTest() {
        Unit builder = new Builder(player);
        player.addUnit(builder);
        player.setResource(ResourceTypes.FOOD, 10);
        gameMap.setUnit(new Coordinates(5, 5), builder);
        gameMap.constructAndSetBuildingTile(Constructable.MINE, new Coordinates(5, 5), player);

        ResourceIterator resourceIterator = new ResourceIterator(builder);
        while (resourceIterator.hasNext()) {
            assertTrue(player.getResource(resourceIterator.getType()) == resourceIterator.getValue() + 10);
        }
        assertFalse(gameMap.getTile(new Coordinates(5, 5)).hasUnit());
    }

    @Test
    public void killUnitAndRefundCostTestRoad() {
        Unit builder = new Builder(player);
        player.addUnit(builder);
        Coordinates coordinates = new Coordinates(5, 5);
        gameMap.setUnit(coordinates, builder);
        gameMap.constructAndSetBuildingTile(Constructable.ROAD, coordinates, player);

        assertTrue(gameMap.getTile(new Coordinates(5, 5)).hasUnit());
    }

    @Test
    public void moveUnitTest() {
        Unit unit = new Knight(player);
        Coordinates coordinates = new Coordinates(5, 5);
        Coordinates targetCoordinates = new Coordinates(6, 6);

        gameMap.setUnit(coordinates, unit);
        gameMap.moveUnit(coordinates, targetCoordinates);

        assertFalse(gameMap.getTile(coordinates).hasUnit());
        assertTrue(gameMap.getTile(targetCoordinates).hasUnit());
        assertTrue(gameMap.getUnit(targetCoordinates) == unit);
    }

    @Test
    public void placeWheatTest() {
        gameMap.setTileResource(new Coordinates(5, 4), new Mountain());
        gameMap.setTileResource(new Coordinates(6, 5), new Water());

        gameMap.constructAndSetBuildingTile(Constructable.FARM, new Coordinates(5, 5), player);

        assertFalse(gameMap.getTile(new Coordinates(5, 4)).hasBuilding());
        assertFalse(gameMap.getTile(new Coordinates(6, 5)).hasBuilding());

        assertTrue(gameMap.getTile(new Coordinates(6, 4)).getBuilding().getType().equals(Constructable.WHEAT));
    }
}