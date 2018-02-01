package map;

import main.Player;
import main.ResourceIterator;
import main.ResourceTypes;
import map.buildings.Mine;
import map.resources.Forest;
import map.resources.Mountain;
import map.resources.Water;
import org.junit.Before;
import org.junit.Test;
import units.Builder;
import units.Knight;
import units.Unit;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapTest {
    private Player player;
    private Map gameMap;

    @Before
    public void setUp() {
        gameMap = new Map(true, 10);
        player = new Player("Daniel", Color.yellow);
    }

    @Test
    public void spawnCity() {
        gameMap.spawnCity(player);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (gameMap.getTile(x, y).hasBuilding()) {
                    assertTrue(gameMap.getTile(x, y).getBuilding().getType().equals("City"));
                }
            }
        }
    }

    @Test
    public void constructAndSetBuildingTileTest() {
        gameMap.constructAndSetBuildingTile("Mine", 5, 5, player);

        assertTrue(gameMap.getTile(5, 5).getBuilding().getType().equals("Mine"));
    }

    @Test
    public void constructAndSetBuildingTileTestConstructionNotPossible() {
        gameMap.getTile(5, 5).setBuilding(new Mine());
        gameMap.constructAndSetBuildingTile("Tower", 5, 5, player);

        assertFalse(gameMap.getTile(5, 5).getBuilding().getType().equals("Tower"));
    }

    @Test
    public void constructAndSetBuildingTileTestResourcesHarvested() {
        gameMap.setTileResource(4, 4, new Forest());
        gameMap.constructAndSetBuildingTile("Lumber Mill", 5, 5, player);

        assertTrue(gameMap.getTile(5, 5).getBuilding().getResourceAmount(ResourceTypes.WOOD) == 1);
    }

    @Test
    public void killUnitAndRefundCostTest() {
        Unit builder = new Builder(player);
        player.addUnit(builder);
        gameMap.setUnit(5, 5, builder);
        gameMap.constructAndSetBuildingTile("Mine", 5, 5, player);

        ResourceIterator resourceIterator = new ResourceIterator(builder);
        while (resourceIterator.hasNext()) {
            assertTrue(player.getResource(resourceIterator.getType()) == resourceIterator.getValue());
        }
        assertFalse(gameMap.getTile(5, 5).hasUnit());
    }

    @Test
    public void killUnitAndRefundCostTestRoad() {
        Unit builder = new Builder(player);
        player.addUnit(builder);
        gameMap.setUnit(5, 5, builder);
        gameMap.constructAndSetBuildingTile("Road", 5, 5, player);

        assertTrue(gameMap.getTile(5, 5).hasUnit());
    }

    @Test
    public void moveUnitTest() {
        Unit unit = new Knight(player);
        gameMap.setUnit(5, 5, unit);
        gameMap.moveUnit(5, 5, 6, 6);

        assertFalse(gameMap.getTile(5, 5).hasUnit());
        assertTrue(gameMap.getTile(6, 6).hasUnit());
        assertTrue(gameMap.getUnit(6, 6) == unit);
    }

    @Test
    public void placeWheatTest() {
        gameMap.setTileResource(5, 4, new Mountain());
        gameMap.setTileResource(6, 5, new Water());

        gameMap.constructAndSetBuildingTile("Farm", 5, 5, player);

        assertFalse(gameMap.getTile(5, 4).hasBuilding());
        assertFalse(gameMap.getTile(6, 5).hasBuilding());

        assertTrue(gameMap.getTile(6, 4).getBuilding().getType().equals("Wheat"));
    }
}