package game.map;

import exceptions.TypeNotFound;
import game.gameModel.Player;
import game.ResourceIterator;
import game.map.buildings.Building;
import game.map.buildings.LumberMill;
import game.map.buildings.Mine;
import game.map.mapModel.ResourceYieldCalculator;
import game.map.mapModel.Tile;
import game.map.mapModel.TileFactory;
import game.map.resources.Resource;
import game.map.resources.ResourceTypes;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ResourceYieldCalculatorTest {
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
            }
        }
    }

    @Test
    public void calculateResourceYieldsTestLumberMill() {
        setMapTo(ResourceTypes.WOOD);
        Building lumberMill = new LumberMill();
        ResourceYieldCalculator.calculateResourceYields(new Coordinates(3, 3), lumberMill, player, currentMap);
        assertTrue(lumberMill.getResourceAmount(ResourceTypes.WOOD) == 9);
    }

    @Test
    public void calculateResourceYieldsTestMine() {
        setMapTo(ResourceTypes.IRON);
        Building mine = new Mine();
        ResourceYieldCalculator.calculateResourceYields(new Coordinates(3, 3), mine, player, currentMap);
        assertTrue(mine.getResourceAmount(ResourceTypes.IRON) == 9);
    }

    @Test
    public void calculateResourceYieldsTestMultipleResources() throws TypeNotFound {
        currentMap[0][0].setResource(TileFactory.buildResourceTile(ResourceTypes.IRON));
        currentMap[0][1].setResource(TileFactory.buildResourceTile(ResourceTypes.COAL));
        currentMap[0][2].setResource(TileFactory.buildResourceTile(ResourceTypes.DIAMONDS));
        currentMap[1][0].setResource(TileFactory.buildResourceTile(ResourceTypes.IRON));
        currentMap[1][1].setResource(TileFactory.buildResourceTile(ResourceTypes.STONE));
        currentMap[1][2].setResource(TileFactory.buildResourceTile(ResourceTypes.COAL));
        currentMap[2][0].setResource(TileFactory.buildResourceTile(ResourceTypes.IRON));
        currentMap[2][1].setResource(TileFactory.buildResourceTile(ResourceTypes.COPPER));
        currentMap[2][2].setResource(TileFactory.buildResourceTile(ResourceTypes.COPPER));

        Building mine = new Mine();
        ResourceYieldCalculator.calculateResourceYields(new Coordinates(1, 1), mine, player, currentMap);

        assertTrue(mine.getResourceAmount(ResourceTypes.IRON) == 3);
        assertTrue(mine.getResourceAmount(ResourceTypes.COAL) == 2);
        assertTrue(mine.getResourceAmount(ResourceTypes.DIAMONDS) == 1);
        assertTrue(mine.getResourceAmount(ResourceTypes.COPPER) == 2);
        assertTrue(mine.getResourceAmount(ResourceTypes.STONE) == 1);
    }

    @Test
    public void calculateResourceYieldsTestCityConnection() {
        setMapTo(ResourceTypes.WOOD);
        Building lumberMill = new LumberMill();
        ResourceYieldCalculator.calculateResourceYields(new Coordinates(3, 3), lumberMill, player, currentMap);

        lumberMill.setHasCityConnection(true);
        assertTrue(lumberMill.getResourceAmount(ResourceTypes.WOOD) == 18);
        lumberMill.setHasCityConnection(false);
        assertTrue(lumberMill.getResourceAmount(ResourceTypes.WOOD) == 9);
    }

    @Test
    public void calculateResourceYieldsTestNoResources() {
        setMapTo(ResourceTypes.GRASS);
        Building lumberMill = new LumberMill();
        ResourceYieldCalculator.calculateResourceYields(new Coordinates(3, 3), lumberMill, player, currentMap);
        ResourceIterator resourceIterator = new ResourceIterator(lumberMill, false);
        while (resourceIterator.hasNext()) {
            assertTrue(lumberMill.getResourceAmount(resourceIterator.getType()) == 0);
        }
    }

    @Test(expected = NullPointerException.class)
    public void calculateResourceYieldsTestWrongHarvester() {
        setMapTo(ResourceTypes.COPPER);
        Building lumberMill = new LumberMill();
        ResourceYieldCalculator.calculateResourceYields(new Coordinates(3, 3), lumberMill, player, currentMap);
        lumberMill.getResourceAmount(ResourceTypes.COPPER);
        fail();
    }

    private void setMapTo(ResourceTypes resourceType) {
        Resource resource;
        try {
            for (Tile[] aCurrentMap : currentMap) {
                for (int j = 0; j < currentMap.length; j++) {
                    resource = TileFactory.buildResourceTile(resourceType);
                    aCurrentMap[j].setResource(resource);
                }
            }
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
        }
    }
}