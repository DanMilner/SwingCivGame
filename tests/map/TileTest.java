package map;

import main.Player;
import org.junit.Test;


import java.awt.*;

import static org.junit.Assert.*;

public class TileTest {

    @Test
    public void test(){
        Player player = new Player("Daniel", Color.yellow);
        Tile tile = new Tile(1,1,player);

        assertTrue(tile.hasOwner());
    }
}