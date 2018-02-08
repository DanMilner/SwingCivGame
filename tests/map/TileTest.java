package map;

import main.Player;
import map.buildings.Building;
import map.buildings.City;
import map.buildings.Road;
import map.resources.Mountain;
import map.resources.Resource;
import map.resources.Water;
import org.junit.Before;
import org.junit.Test;
import map.units.Builder;
import map.units.Unit;

import java.awt.*;

import static org.junit.Assert.*;

public class TileTest {
    private Player player;
    private Tile tile;

    @Before
    public void setUp() {
        player = new Player("Daniel", Color.yellow);
        tile = new Tile(new Coordinates(1,1), player);
    }

    @Test
    public void hasOwnerTest() {
        assertTrue(tile.hasOwner());
    }

    @Test
    public void hasUnitTest() {
        givenTileHasUnit();

        assertTrue(tile.hasUnit());
    }

    @Test
    public void hasBuildingTest() {
        givenTileHasBuilding();

        assertTrue(tile.hasBuilding());
    }

    @Test
    public void hasRoadTest() {
        Building road = new Road();
        tile.setBuilding(road);
        assertTrue(tile.hasRoad());
    }

    @Test
    public void isTraversableTest() {
        Resource mountain = new Mountain();
        tile.setResource(mountain);
        assertFalse(tile.isTraversable());

        Resource water = new Water();
        tile.setResource(water);
        assertFalse(tile.isTraversable());
    }

    @Test
    public void getImageUnitTest() {
        givenTileHasUnit();
        givenTileHasBuilding();
        givenTileHasResource();

        String imageLocation = tile.getUnit().getImage().toString();
        assertEquals(imageLocation, tile.getImage().toString());
    }

    @Test
    public void getImageBuildingTest() {
        givenTileHasBuilding();
        givenTileHasResource();

        String imageLocation = tile.getBuilding().getImage().toString();
        assertEquals(imageLocation, tile.getImage().toString());
    }

    @Test
    public void getImageResourceTest() {
        givenTileHasResource();

        String imageLocation = tile.getResource().getImage().toString();
        assertEquals(imageLocation, tile.getImage().toString());
    }

    @Test
    public void hasBuildingWithCityConnectionTest() {
        givenTileHasBuilding();
        tile.getBuilding().setHasCityConnection(true);

        assertTrue(tile.hasBuildingWithCityConnection());
    }

    @Test
    public void getButtonListTest() {
        givenTileHasUnit();
        givenTileHasBuilding();

        Boolean unitSelected = true;
        assertEquals(tile.getButtonList(unitSelected), tile.getUnit().getButtonList());

        unitSelected = false;
        assertEquals(tile.getButtonList(unitSelected), tile.getBuilding().getButtonList());
    }

    private void givenTileHasUnit() {
        Unit unit = new Builder(player);
        tile.setUnit(unit);
    }

    private void givenTileHasBuilding() {
        Building city = new City();
        tile.setBuilding(city);
    }

    private void givenTileHasResource() {
        Resource mountain = new Mountain();
        tile.setResource(mountain);
    }
}