package map;

import main.MapData;
import main.Player;
import map.buildings.Building;
import map.buildings.City;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TileOwnerHandlerTest {
    private Map map;
    private Player player;
    private TileOwnerHandler tileOwnerHandler;

    @Before
    public void setUp() {
        MapData mapData = new MapData();
        mapData.setMapData(0,0,0,0,10,0,false);
        map = new Map(mapData);
        tileOwnerHandler = new TileOwnerHandler(map);
        player = new Player("Daniel", Color.yellow);
    }

    @Test
    public void setTileOwnerTest() {
        Building city = new City();
        int borderSize = city.getBorderSize();
        tileOwnerHandler.setTileOwner(city, new Coordinates(5, 5), player);
        for (int x = 5 - borderSize; x <= 5 + borderSize; x++) {
            for (int y = 5 - borderSize; y <= 5 + borderSize; y++) {
                assertTrue(map.getTile(new Coordinates(x,y)).getOwner() == player);
            }
        }
    }

    @Test
    public void setTileOwnerTestOtherTiles() {
        Building city = new City();
        tileOwnerHandler.setTileOwner(city, new Coordinates(5, 5), player);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (map.getTile(new Coordinates(x,y)).getOwner() != player)
                    assertTrue(map.getTile(new Coordinates(x,y)).getOwner() == null);
            }
        }
    }

    @Test
    public void borderRequiredAdjacentNull() {
        map.getTile(new Coordinates(1,1)).setOwner(player);
        map.getTile(new Coordinates(1,2)).setOwner(null);
        assertTrue(3 == tileOwnerHandler.borderRequired(new Coordinates(1, 1), new Coordinates(1, 2)));
    }

    @Test
    public void borderRequiredAdjacentSelf() {
        map.getTile(new Coordinates(1,1)).setOwner(player);
        map.getTile(new Coordinates(1,2)).setOwner(player);
        assertFalse(3 == tileOwnerHandler.borderRequired(new Coordinates(1, 1), new Coordinates(1, 2)));
    }

    @Test
    public void borderRequiredAdjacentEnemy() {
        Player enemy = new Player("Darth Vader", Color.red);
        map.getTile(new Coordinates(1,1)).setOwner(player);
        map.getTile(new Coordinates(1,2)).setOwner(enemy);
        assertTrue(3 == tileOwnerHandler.borderRequired(new Coordinates(1, 1), new Coordinates(1, 2)));
    }
}