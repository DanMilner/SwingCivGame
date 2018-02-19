package game.gameModel;

import game.MapData;
import game.PlayerData;
import game.map.Constructable;
import game.map.Coordinates;
import game.map.mapModel.Map;

import javax.swing.*;
import java.util.ArrayList;

public class GameController {
    private Map gameMap;
    private UnitCreator unitCreator;
    private PlayerHandler playerHandler;
    private UnitMovementHandler unitMovementHandler;
    private AttackHandler attackHandler;
    private BorderExpansionBehaviour borderExpansionBehaviour;

    public GameController(ArrayList<PlayerData> playersToCreate, MapData mapData) {
        gameMap = new Map(mapData);
        unitCreator = new UnitCreator(gameMap);
        playerHandler = new PlayerHandler();
        unitMovementHandler = new UnitMovementHandler(gameMap);
        attackHandler = new AttackHandler(gameMap);
        borderExpansionBehaviour = new BorderExpansionBehaviour(gameMap);

        playerHandler.setUpPlayers(gameMap, playersToCreate);
    }

    public Map getMap() {
        return gameMap;
    }

    public void endTurn() {
        borderExpansionBehaviour.expandBorders(playerHandler.getCurrentPlayer());
        playerHandler.incrementCurrentPlayer();
    }

    public Player getCurrentPlayer() {
        return playerHandler.getCurrentPlayer();
    }

    public boolean isValidMove(Coordinates originCoordinates, Coordinates destinationCoordinates) {
        return unitMovementHandler.isValidMove(originCoordinates, destinationCoordinates);
    }

    public boolean moveUnit(Coordinates originCoordinates, Coordinates destinationCoordinates) {
        return unitMovementHandler.moveUnit(originCoordinates, destinationCoordinates);
    }

    public boolean checkAvailableResources(Constructable constructable, Boolean unitCheck) {
        return gameMap.checkCost(constructable, playerHandler.getCurrentPlayer(), unitCheck);
    }

    public void buttonClicked(Coordinates coordinates, Constructable constructable) {
        Player currentPlayer = playerHandler.getCurrentPlayer();

        if (gameMap.getTile(coordinates).hasBuilding()) {
            unitCreator.createUnit(coordinates, constructable, currentPlayer);
        } else {
            gameMap.constructAndSetBuildingTile(constructable, coordinates, currentPlayer);
            PlayerResourceHandler.calculateResources(currentPlayer);
        }
    }

    public ImageIcon getTileImage(Coordinates coordinates) {
        return gameMap.getTileImage(coordinates);
    }

    public ArrayList<Constructable> getTileButtonList(boolean unitSelected, Coordinates coordinates) {
        return gameMap.getTileButtonList(unitSelected, coordinates);
    }

    public boolean attackIsPossible(Coordinates originCoordinates, Coordinates targetCoordinates) {
        return attackHandler.attackIsPossible(originCoordinates, targetCoordinates);
    }

    public void performAttack(Coordinates originCoordinates, Coordinates targetCoordinates) {
        attackHandler.performAttack(originCoordinates, targetCoordinates);
    }
}