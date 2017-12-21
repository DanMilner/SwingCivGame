package map;

import exceptions.TypeNotFound;
import main.Player;
import main.ResourceTypes;
import map.buildings.Building;
import map.buildings.LumberMill;
import map.buildings.Mine;
import map.resources.Resource;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertTrue;

public class ResourceYieldCalculatorTest {
    private Tile[][] currentMap;
    private Player player;

    @Before
    public void setUp() {
        currentMap = new Tile[5][5];
        player = new Player("Daniel", Color.yellow);

        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap.length; j++){
                currentMap[i][j] = new Tile(i,j,player);
            }
        }
    }

    @Test
    public void calculateResourceYieldsTestLumberMill() {
        setMapTo("Forest");
        Building lumberMill = new LumberMill();
        ResourceYieldCalculator.calculateResourceYields(3,3,lumberMill,player,currentMap);
        assertTrue(lumberMill.getResourceAmount(ResourceTypes.WOOD) == 9);
    }

    @Test
    public void calculateResourceYieldsTestMine() {
        setMapTo("Iron");
        Building mine = new Mine();
        ResourceYieldCalculator.calculateResourceYields(3,3,mine,player,currentMap);
        assertTrue(mine.getResourceAmount(ResourceTypes.IRON) == 9);
    }

    @Test
    public void calculateResourceYieldsTestMultipleResources() throws TypeNotFound {
        currentMap[0][0].setResource(TileFactory.buildResourceTile("Iron"));
        currentMap[0][1].setResource(TileFactory.buildResourceTile("Coal"));
        currentMap[0][2].setResource(TileFactory.buildResourceTile("Diamonds"));
        currentMap[1][0].setResource(TileFactory.buildResourceTile("Iron"));
        currentMap[1][1].setResource(TileFactory.buildResourceTile("Mountain"));
        currentMap[1][2].setResource(TileFactory.buildResourceTile("Coal"));
        currentMap[2][0].setResource(TileFactory.buildResourceTile("Iron"));
        currentMap[2][1].setResource(TileFactory.buildResourceTile("Copper"));
        currentMap[2][2].setResource(TileFactory.buildResourceTile("Copper"));

        Building mine = new Mine();
        ResourceYieldCalculator.calculateResourceYields(1,1,mine,player,currentMap);

        assertTrue(mine.getResourceAmount(ResourceTypes.IRON) == 3);
        assertTrue(mine.getResourceAmount(ResourceTypes.COAL) == 2);
        assertTrue(mine.getResourceAmount(ResourceTypes.DIAMONDS) == 1);
        assertTrue(mine.getResourceAmount(ResourceTypes.COPPER) == 2);
        assertTrue(mine.getResourceAmount(ResourceTypes.STONE) == 1);
    }

    @Test
    public void calculateResourceYieldsTestCityConnection(){
        setMapTo("Forest");
        Building lumberMill = new LumberMill();
        ResourceYieldCalculator.calculateResourceYields(3,3,lumberMill,player,currentMap);

        lumberMill.setHasCityConnection(true);
        assertTrue(lumberMill.getResourceAmount(ResourceTypes.WOOD) == 18);
        lumberMill.setHasCityConnection(false);
        assertTrue(lumberMill.getResourceAmount(ResourceTypes.WOOD) == 9);
    }
    @Test
    public void calculateResourceYieldsTestNoResources(){
        setMapTo("Grass");
        Building lumberMill = new LumberMill();
        ResourceYieldCalculator.calculateResourceYields(3,3,lumberMill,player,currentMap);
        for (int type = 0; type<ResourceTypes.getNumberOfResourceTypes(); type++){
            assertTrue(lumberMill.getResourceAmount(type) == 0);
        }
    }

    @Test
    public void calculateResourceYieldsTestWrongHarvester(){
        setMapTo("Copper");
        Building lumberMill = new LumberMill();
        ResourceYieldCalculator.calculateResourceYields(3,3,lumberMill,player,currentMap);
        assertTrue(lumberMill.getResourceAmount(ResourceTypes.COPPER) == 0);
    }

    private void setMapTo(String mapTo) {
        Resource resource;
        try {
            for (Tile[] aCurrentMap : currentMap) {
                for (int j = 0; j < currentMap.length; j++) {
                    resource = TileFactory.buildResourceTile(mapTo);
                    aCurrentMap[j].setResource(resource);
                }
            }
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
        }
    }
}