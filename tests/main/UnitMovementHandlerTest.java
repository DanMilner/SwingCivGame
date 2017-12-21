package main;

import map.Map;
import map.Tile;
import map.resources.Mountain;
import map.resources.Water;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import units.Builder;
import units.Unit;

import static org.junit.Assert.*;

public class UnitMovementHandlerTest {
    private UnitMovementHandler unitMovementHandler;
    private int maxMoves;
    private Map gameMap;
    @Before
    public void SetUp(){
        final int MAPSIZE = 20;
        gameMap = new Map(true, MAPSIZE);
        unitMovementHandler = new UnitMovementHandler(gameMap, MAPSIZE);
        Unit newUnit = new Builder(null);
        gameMap.setUnit(5,5, newUnit);
        maxMoves = newUnit.getRemainingMoves();
    }

    @Test
    public void isValidMoveTestMoveNorth(){
        Assert.assertTrue(unitMovementHandler.isValidMove(5,5,5,6));
    }
    @Test
    public void isValidMoveTestMoveSouth(){
        Assert.assertTrue(unitMovementHandler.isValidMove(5,5,5,4));
    }
    @Test
    public void isValidMoveTestMoveEast(){
        Assert.assertTrue(unitMovementHandler.isValidMove(5,5,6,5));
    }
    @Test
    public void isValidMoveTestMoveWest(){
        Assert.assertTrue(unitMovementHandler.isValidMove(5,5,4,6));
    }
    @Test
    public void isValidMoveTestMaxMovesStraight(){
        Assert.assertTrue(unitMovementHandler.isValidMove(5,5,5+maxMoves,5));
    }

    @Test
    public void isValidMoveTestMaxMovesDiagonal(){
        Assert.assertTrue(unitMovementHandler.isValidMove(5,5,5+maxMoves/2,5+maxMoves/2));
    }

    @Test
    public void isValidMoveTestMoreThanMaxMoves(){
        Assert.assertFalse(unitMovementHandler.isValidMove(5,5,5+maxMoves+1,5));
    }

    @Test
    public void isValidMoveTestTileNotTraversable(){
        gameMap.setTileResource(7,7,new Mountain());
        Assert.assertFalse(unitMovementHandler.isValidMove(5,5,7,7));
        gameMap.setTileResource(7,7,new Water());
        Assert.assertFalse(unitMovementHandler.isValidMove(5,5,7,7));
    }
    @Test
    public void moveUnitTestValidMove() {
        unitMovementHandler.moveUnit(5,5,7,7);
        Assert.assertFalse(gameMap.getTile(5,5).hasUnit());
        Assert.assertTrue(gameMap.getTile(7,7).hasUnit());
    }

    @Test
    public void moveUnitTestInvalidMove() {
        unitMovementHandler.moveUnit(5,5,15,15);
        Assert.assertTrue(gameMap.getTile(5,5).hasUnit());
        Assert.assertFalse(gameMap.getTile(15,15).hasUnit());
    }
}