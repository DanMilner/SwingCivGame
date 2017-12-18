package map;

import main.Player;
import map.buildings.City;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class MapTest {
    private Tile[][] currentMap;
    private Player playerOne;
    private Player playerTwo;

    @Before
    public void setUp(){
        currentMap = new Tile[40][40];
        playerOne = new Player("Daniel", Color.yellow);
        playerTwo = new Player("Alastair", Color.red);
    }

    @Test
    public void spawnCity() {
        Tile newTile = new Tile(1,1,playerOne);

        City city = new City();

        newTile.setBuilding(city);

        assertEquals("City",newTile.getBuilding().getType());
    }
}