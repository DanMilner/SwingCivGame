package game.map;

import game.gameModel.Player;
import game.map.buildings.Mine;
import game.map.mapModel.ConstructionPossibleChecker;
import game.map.mapModel.Tile;
import game.map.resources.Forest;
import game.map.resources.Grass;
import game.map.resources.Water;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConstructionPossibleTest {
    private Tile[][] currentMap;
    private Player player;

    @Before
    public void setUp() {
        currentMap = new Tile[5][5];
        player = new Player("Daniel", Color.yellow);

        for (int i = 0; i < currentMap.length; i++) {
            for (int j = 0; j < currentMap.length; j++) {
                Coordinates coordinates = new Coordinates(i,j);
                currentMap[i][j] = new Tile(coordinates, player);
                currentMap[i][j].setResource(new Grass());
            }
        }
    }

    @Test
    public void isConstructionPossibleTest() {
        assertTrue(ConstructionPossibleChecker.isConstructionPossible(currentMap, Constructable.MINE, new Coordinates(2, 2), player));
    }

    @Test
    public void isConstructionPossibleTestDockNoWater() {
        assertFalse(ConstructionPossibleChecker.isConstructionPossible(currentMap, Constructable.DOCK, new Coordinates(2, 2), player));
    }

    @Test
    public void isConstructionPossibleTestDockWithWater() {
        currentMap[2][3].setResource(new Water());
        assertTrue(ConstructionPossibleChecker.isConstructionPossible(currentMap, Constructable.DOCK, new Coordinates(2, 2), player));
    }

    @Test
    public void isConstructionPossibleTestInEnemyTerritory() {
        Player enemy = new Player("Baddie", Color.red);
        currentMap[2][2].setOwner(enemy);
        assertFalse(ConstructionPossibleChecker.isConstructionPossible(currentMap, Constructable.TOWER, new Coordinates(2, 2), player));
    }

    @Test
    public void isConstructionPossibleTestTileHasBuilding() {
        currentMap[2][2].setBuilding(new Mine());
        assertFalse(ConstructionPossibleChecker.isConstructionPossible(currentMap, Constructable.TOWER, new Coordinates(2, 2), player));
    }

    @Test
    public void isConstructionPossibleTestTileHasResourceInUse() {
        currentMap[2][2].setResource(new Forest());
        currentMap[2][2].getResource().setInUse(true);
        assertFalse(ConstructionPossibleChecker.isConstructionPossible(currentMap, Constructable.LUMBERMILL, new Coordinates(2, 2), player));
    }
}