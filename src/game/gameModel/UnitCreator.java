package game.gameModel;

import exceptions.TypeNotFound;
import game.map.*;
import game.map.mapModel.Map;
import game.map.mapModel.Tile;
import game.map.mapModel.UnitFactory;
import game.map.units.Unit;

public class UnitCreator {

    private Map gameMap;
    private Player currentPlayer;

    public UnitCreator(Map gameMap) {
        this.gameMap = gameMap;
    }

    public void createUnit(Coordinates coordinates, Constructable unitType, Player currentPlayer) {
        this.currentPlayer = currentPlayer;

        Unit newUnit;
        try {
            newUnit = UnitFactory.buildUnit(unitType, currentPlayer);
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
            return;
        }

        coordinates = getUnitSpawnCoordinates(coordinates);
        if (coordinates == null)
            return;

        gameMap.setUnit(coordinates, newUnit);
        currentPlayer.addUnit(newUnit);
        PlayerResourceHandler.calculateResources(currentPlayer);
    }

    private Coordinates getUnitSpawnCoordinates(Coordinates coordinates) {
        Coordinates newCoordinates = new Coordinates(coordinates.x + 1, coordinates.y);
        if (isTileAvailable(newCoordinates))
            return newCoordinates;

        newCoordinates.setCoordinates(coordinates.x - 1, coordinates.y);
        if (isTileAvailable(newCoordinates))
            return newCoordinates;

        newCoordinates.setCoordinates(coordinates.x, coordinates.y - 1);
        if (isTileAvailable(newCoordinates))
            return newCoordinates;

        newCoordinates.setCoordinates(coordinates.x, coordinates.y + 1);
        if (isTileAvailable(newCoordinates))
            return newCoordinates;

        System.out.println("nowhere to spawn a unit");
        return null;
    }

    private boolean isTileAvailable(Coordinates newCoordinates) {
        Tile tile = gameMap.getTile(newCoordinates);
        return !tile.hasUnit() && tile.isTraversable() && tile.getOwner() == currentPlayer;
    }
}
