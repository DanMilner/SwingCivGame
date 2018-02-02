package map;

import main.Player;
import map.buildings.Mine;
import map.resources.Forest;
import map.resources.Grass;
import map.resources.Water;
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
                currentMap[i][j] = new Tile(i, j, player);
                currentMap[i][j].setResource(new Grass());
            }
        }
    }

    @Test
    public void isConstructionPossibleTest() {
        assertTrue(ConstructionPossible.isConstructionPossible(currentMap, Constructable.MINE, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestDockNoWater() {
        assertFalse(ConstructionPossible.isConstructionPossible(currentMap, Constructable.DOCK, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestDockWithWater() {
        currentMap[2][3].setResource(new Water());
        assertTrue(ConstructionPossible.isConstructionPossible(currentMap, Constructable.DOCK, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestInEnemyTerritory() {
        Player enemy = new Player("Baddie", Color.red);
        currentMap[2][2].setOwner(enemy);
        assertFalse(ConstructionPossible.isConstructionPossible(currentMap, Constructable.TOWER, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestTileHasBuilding() {
        currentMap[2][2].setBuilding(new Mine());
        assertFalse(ConstructionPossible.isConstructionPossible(currentMap, Constructable.TOWER, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestTileHasResourceInUse() {
        currentMap[2][2].setResource(new Forest());
        currentMap[2][2].getResource().setInUse(true);
        assertFalse(ConstructionPossible.isConstructionPossible(currentMap, Constructable.LUMBERMILL, 2, 2, player));
    }
}