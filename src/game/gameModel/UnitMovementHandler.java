package game.gameModel;

import game.map.Coordinates;
import game.map.mapModel.Map;
import game.map.mapModel.Tile;
import game.map.units.Unit;

public class UnitMovementHandler {

    private Map gameMap;

    public UnitMovementHandler(Map gameMap) {
        this.gameMap = gameMap;
    }

    public boolean isValidMove(Coordinates originCoordinates, Coordinates targetCoordinates) {
        if (!gameMap.coordinatesOnMap(targetCoordinates))
            return false;
        Tile destinationTile = gameMap.getTile(targetCoordinates);
        Unit currentUnit = gameMap.getUnit(originCoordinates);

        if (!destinationTile.isTraversable())
            return false;

        if (tileIsOutOfRange(originCoordinates, targetCoordinates, currentUnit.getRemainingMoves()))
            return false;

        if (tileHasEnemyBuilding(currentUnit, destinationTile))
            return false;

        return !destinationTile.hasUnit();
    }

    private boolean tileHasEnemyBuilding(Unit currentUnit, Tile destinationTile) {
        return destinationTile.hasBuilding() && destinationTile.getOwner() != currentUnit.getOwner();
    }

    public static boolean tileIsOutOfRange(Coordinates originCoordinates, Coordinates targetCoordinates, int range) {
        int yDistance = Math.abs(originCoordinates.y - targetCoordinates.y); //distance moved on y axis
        int xDistance = Math.abs(originCoordinates.x - targetCoordinates.x); //distance moved on x axis
        return range - yDistance - xDistance < 0;
    }

    public boolean moveUnit(Coordinates originCoordinates, Coordinates targetCoordinates) {
        if (!isValidMove(originCoordinates, targetCoordinates))
            return false;

        Unit unitBeingMoved = gameMap.getUnit(originCoordinates);
        int yDistance = Math.abs(originCoordinates.y - targetCoordinates.y); //distance moved on y axis
        int xDistance = Math.abs(originCoordinates.x - targetCoordinates.x); // distance moved on x axis
        int remainingMoves = unitBeingMoved.getRemainingMoves() - yDistance - xDistance;
        unitBeingMoved.setRemainingMoves(remainingMoves);
        gameMap.moveUnit(originCoordinates, targetCoordinates);
        return true;
    }
}
