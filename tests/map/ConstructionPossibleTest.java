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
        String buildingType = "Mine";
        assertTrue(ConstructionPossible.isConstructionPossible(currentMap, buildingType, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestDockNoWater() {
        String buildingType = "Dock";
        assertFalse(ConstructionPossible.isConstructionPossible(currentMap, buildingType, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestDockWithWater() {
        String buildingType = "Dock";
        currentMap[2][3].setResource(new Water());
        assertTrue(ConstructionPossible.isConstructionPossible(currentMap, buildingType, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestInEnemyTerritory() {
        String buildingType = "Tower";
        Player enemy = new Player("Baddie", Color.red);
        currentMap[2][2].setOwner(enemy);
        assertFalse(ConstructionPossible.isConstructionPossible(currentMap, buildingType, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestTileHasBuilding() {
        String buildingType = "Tower";
        currentMap[2][2].setBuilding(new Mine());
        assertFalse(ConstructionPossible.isConstructionPossible(currentMap, buildingType, 2, 2, player));
    }

    @Test
    public void isConstructionPossibleTestTileHasResourceInUse() {
        String buildingType = "LumberMill";
        currentMap[2][2].setResource(new Forest());
        currentMap[2][2].getResource().setInUse(true);
        assertFalse(ConstructionPossible.isConstructionPossible(currentMap, buildingType, 2, 2, player));
    }
}