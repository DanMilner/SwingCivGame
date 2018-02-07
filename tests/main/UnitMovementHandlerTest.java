package main;

import map.Coordinates;
import map.Map;
import map.resources.Mountain;
import map.resources.Water;
import map.units.Builder;
import map.units.Unit;
import org.junit.Before;
import org.junit.Test;

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
        gameMap.setUnit(new Coordinates(5, 5), newUnit);
        maxMoves = newUnit.getRemainingMoves();
    }

    @Test
    public void isValidMoveTestMoveNorth() {
        assertTrue(unitMovementHandler.isValidMove(new Coordinates(5, 5), new Coordinates(5, 6)));
    }

    @Test
    public void isValidMoveTestMoveSouth() {
        assertTrue(unitMovementHandler.isValidMove(new Coordinates(5, 5), new Coordinates(5, 4)));
    }

    @Test
    public void isValidMoveTestMoveEast() {
        assertTrue(unitMovementHandler.isValidMove(new Coordinates(5, 5), new Coordinates(6, 5)));
    }

    @Test
    public void isValidMoveTestMoveWest() {
        assertTrue(unitMovementHandler.isValidMove(new Coordinates(5, 5), new Coordinates(4, 6)));
    }

    @Test
    public void isValidMoveTestMaxMovesStraight() {
        assertTrue(unitMovementHandler.isValidMove(new Coordinates(5, 5), new Coordinates(5 + maxMoves, 5)));
    }

    @Test
    public void isValidMoveTestMaxMovesDiagonal() {
        assertTrue(unitMovementHandler.isValidMove(new Coordinates(5, 5), new Coordinates(5 + maxMoves / 2, 5 + maxMoves / 2)));
    }

    @Test
    public void isValidMoveTestMoreThanMaxMoves() {
        assertFalse(unitMovementHandler.isValidMove(new Coordinates(5, 5), new Coordinates(5 + maxMoves + 1, 5)));
    }

    @Test
    public void isValidMoveTestTileNotTraversable() {
        gameMap.setTileResource(new Coordinates(7, 7), new Mountain());
        assertFalse(unitMovementHandler.isValidMove(new Coordinates(5, 5), new Coordinates(7, 7)));
        gameMap.setTileResource(new Coordinates(7, 7), new Water());
        assertFalse(unitMovementHandler.isValidMove(new Coordinates(5, 5), new Coordinates(7, 7)));
    }

    @Test
    public void moveUnitTestValidMove() {
        unitMovementHandler.moveUnit(new Coordinates(5, 5), new Coordinates(7, 7));
        assertFalse(gameMap.getTile(new Coordinates(5, 5)).hasUnit());
        assertTrue(gameMap.getTile(new Coordinates(7, 7)).hasUnit());
    }

    @Test
    public void moveUnitTestInvalidMove() {
        unitMovementHandler.moveUnit(new Coordinates(5, 5), new Coordinates(15, 15));
        assertTrue(gameMap.getTile(new Coordinates(5, 5)).hasUnit());
        assertFalse(gameMap.getTile(new Coordinates(15, 15)).hasUnit());
    }
}