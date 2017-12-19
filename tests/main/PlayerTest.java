package main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import units.Catapult;
import units.Knight;
import units.Unit;

import java.awt.*;

public class PlayerTest {
    private Player player;

    @Before
    public void setUp(){
        player = new Player("Daniel", Color.yellow);
    }

    @Test
    public void setResourceTest(){
        player.setResource(ResourceTypes.WOOD, 100);
        Assert.assertEquals(100, player.getResource(ResourceTypes.WOOD));
    }

    @Test
    public void increaseResourceTest(){
        player.increaseResource(ResourceTypes.WOOD, 10);
        Assert.assertEquals(10, player.getResource(ResourceTypes.WOOD));
    }

    @Test
    public void resetUnitMovesTest(){
        Unit unit = new Knight(player);
        int maxMoves = unit.getRemainingMoves();

        unit.setRemainingMoves(0);
        player.addUnit(unit);
        player.resetUnitMoves();

        Assert.assertEquals(maxMoves, unit.getRemainingMoves());
    }

    @Test
    public void resetResourcesTest(){
        player.setResource(ResourceTypes.WOOD, 100);
        player.setResource(ResourceTypes.IRON, 100);
        player.setResource(ResourceTypes.COPPER, 100);
        player.setResource(ResourceTypes.COAL, 100);
        player.setResource(ResourceTypes.DIAMONDS, 100);
        player.setResource(ResourceTypes.FOOD, 100);
        player.setResource(ResourceTypes.STONE, 100);
        player.setResource(ResourceTypes.GOLD, 100);
        player.setResource(ResourceTypes.WATER, 100);

        player.resetResources();

        for (int type = 0; type < ResourceTypes.getNumberOfResourceTypes(); type++) {
            Assert.assertEquals(0,player.getResource(type));
        }
    }

    @Test
    public void refundUnitCostTest(){
        Unit unit = new Catapult(player);
        int cost[] = unit.getResourceCost();

        player.addUnit(unit);
        player.refundUnitCost(unit);

        for (int type = 0; type < ResourceTypes.getNumberOfResourceTypes(); type++) {
            Assert.assertEquals(cost[type],player.getResource(type));
        }
    }
}