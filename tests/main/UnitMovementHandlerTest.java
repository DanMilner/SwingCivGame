package main;

import map.Map;
import map.resources.Mountain;
import map.resources.Water;
import org.junit.Before;
import org.junit.Test;
import map.units.Builder;
import map.units.Unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnitMovementHandlerTest {
    private UnitMovementHandler unitMovementHandler;
    private int maxMoves;
    private Map gameMap;

    @Before
    public void SetUp() {
        final int MAPSIZE = 20;
        gameMap = new Map(true, MAPSIZE);
        unitMovementHandler = new UnitMovementHandler(gameMap);
        Unit newUnit = new Builder(null);
        gameMap.setUnit(5, 5, newUnit);
        maxMoves = newUnit.getRemainingMoves();
    }

    @Test
    public void isValidMoveTestMoveNorth() {
        assertTrue(unitMovementHandler.isValidMove(5, 5, 5, 6));
    }

    @Test
    public void isValidMoveTestMoveSouth() {
        assertTrue(unitMovementHandler.isValidMove(5, 5, 5, 4));
    }

    @Test
    public void isValidMoveTestMoveEast() {
        assertTrue(unitMovementHandler.isValidMove(5, 5, 6, 5));
    }

    @Test
    public void isValidMoveTestMoveWest() {
        assertTrue(unitMovementHandler.isValidMove(5, 5, 4, 6));
    }

    @Test
    public void isValidMoveTestMaxMovesStraight() {
        assertTrue(unitMovementHandler.isValidMove(5, 5, 5 + maxMoves, 5));
    }

    @Test
    public void isValidMoveTestMaxMovesDiagonal() {
        assertTrue(unitMovementHandler.isValidMove(5, 5, 5 + maxMoves / 2, 5 + maxMoves / 2));
    }

    @Test
    public void isValidMoveTestMoreThanMaxMoves() {
        assertFalse(unitMovementHandler.isValidMove(5, 5, 5 + maxMoves + 1, 5));
    }

    @Test
    public void isValidMoveTestTileNotTraversable() {
        gameMap.setTileResource(7, 7, new Mountain());
        assertFalse(unitMovementHandler.isValidMove(5, 5, 7, 7));
        gameMap.setTileResource(7, 7, new Water());
        assertFalse(unitMovementHandler.isValidMove(5, 5, 7, 7));
    }

    @Test
    public void moveUnitTestValidMove() {
        unitMovementHandler.moveUnit(5, 5, 7, 7);
        assertFalse(gameMap.getTile(5, 5).hasUnit());
        assertTrue(gameMap.getTile(7, 7).hasUnit());
    }

    @Test
    public void moveUnitTestInvalidMove() {
        unitMovementHandler.moveUnit(5, 5, 15, 15);
        assertTrue(gameMap.getTile(5, 5).hasUnit());
        assertFalse(gameMap.getTile(15, 15).hasUnit());
    }
}