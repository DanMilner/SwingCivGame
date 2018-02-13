package map;

import main.Player;
import map.buildings.Building;
import map.buildings.City;
import map.resources.Grass;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TileOwnerHandlerTest {
    private Tile[][] currentMap;
    private Player player;
    private TileOwnerHandler tileOwnerHandler;

    @Before
    public void setUp() {
        currentMap = new Tile[10][10];
        tileOwnerHandler = new TileOwnerHandler(currentMap.length);
        player = new Player("Daniel", Color.yellow);

        for (int i = 0; i < currentMap.length; i++) {
            for (int j = 0; j < currentMap.length; j++) {
                Coordinates coordinates = new Coordinates(i,j);
                currentMap[i][j] = new Tile(coordinates, null);
                currentMap[i][j].setResource(new Grass());
            }
        }
    }

    @Test
    public void setTileOwnerTest() {
        Building city = new City();
        int borderSize = city.getBorderSize();
        tileOwnerHandler.setTileOwner(currentMap, city, new Coordinates(5, 5), player);
        for (int x = 5 - borderSize; x <= 5 + borderSize; x++) {
            for (int y = 5 - borderSize; y <= 5 + borderSize; y++) {
                assertTrue(currentMap[x][y].getOwner() == player);
            }
        }
    }

    @Test
    public void setTileOwnerTestOtherTiles() {
        Building city = new City();
        tileOwnerHandler.setTileOwner(currentMap, city, new Coordinates(5, 5), player);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (currentMap[x][y].getOwner() != player)
                    assertTrue(currentMap[x][y].getOwner() == null);
            }
        }
    }

    @Test
    public void borderRequiredAdjacentNull() {
        currentMap[1][1].setOwner(player);
        currentMap[1][2].setOwner(null);
        assertTrue(3 == tileOwnerHandler.borderRequired(currentMap, new Coordinates(1, 1), 1, 2));
    }

    @Test
    public void borderRequiredAdjacentSelf() {
        currentMap[1][1].setOwner(player);
        currentMap[1][2].setOwner(player);
        assertFalse(3 == tileOwnerHandler.borderRequired(currentMap, new Coordinates(1, 1), 1, 2));
    }

    @Test
    public void borderRequiredAdjacentEnemy() {
        Player enemy = new Player("Darth Vader", Color.red);
        currentMap[1][1].setOwner(player);
        currentMap[1][2].setOwner(enemy);
        assertTrue(3 == tileOwnerHandler.borderRequired(currentMap, new Coordinates(1, 1), 1, 2));
    }
}