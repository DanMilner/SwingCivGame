package main;

import exceptions.TypeNotFound;
import map.Map;
import map.Tile;
import map.UnitFactory;
import map.buildings.Building;
import units.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameController {
    private Map gameMap;
    private BuildingAndUnitCreator buildingAndUnitCreator;
    private PlayerHandler playerHandler;
    private UnitMovementHandler unitMovementHandler;
    private AttackHandler attackHandler;

    //temporary until a menu GUI is made
    public static final int MAPSIZE = 40;

    GameController() {
        gameMap = new Map(false, MAPSIZE);
        buildingAndUnitCreator = new BuildingAndUnitCreator(gameMap);
        playerHandler = new PlayerHandler();
        unitMovementHandler = new UnitMovementHandler(gameMap);
        attackHandler = new AttackHandler(gameMap);

        playerHandler.addPlayer("Daniel");
        playerHandler.addPlayer("Alastair");
        // playerHandler.addPlayer("James");

        playerHandler.setUpPlayers(gameMap);
    }

    public Map getMap() {
        return gameMap;
    }

    void nextPlayer() {
        playerHandler.incrementCurrentPlayer();
    }

    Player getCurrentPlayer() {
        return playerHandler.getCurrentPlayer();
    }

    boolean isValidMove(int oldX, int oldY, int newX, int newY) {
        return unitMovementHandler.isValidMove(oldX, oldY, newX, newY);
    }

    boolean moveUnit(int oldX, int oldY, int newX, int newY) {
        return unitMovementHandler.moveUnit(oldX, oldY, newX, newY);
    }

    boolean checkAvailableResources(String type, Boolean unitCheck) {
        return gameMap.checkCost(type, playerHandler.getCurrentPlayer(), unitCheck);
    }

    void buttonClicked(ButtonData data) {
        Player currentPlayer = playerHandler.getCurrentPlayer();

        if (gameMap.getTile(data.getCurrentX(), data.getCurrentY()).hasBuilding()) {
            buildingAndUnitCreator.createUnit(data, currentPlayer);
        } else {
            buildingAndUnitCreator.createBuilding(data, currentPlayer);
            PlayerResourceHandler.calculateResources(currentPlayer);
        }
    }

    public ImageIcon getTileImage(int xCoord, int yCoord) {
        return gameMap.getTileImage(xCoord, yCoord);
    }

    public ArrayList<String> getTileButtonList(boolean unitSelected, int currentX, int currentY) {
        return gameMap.getTileButtonList(unitSelected, currentX, currentY);
    }

    public boolean attackIsPossible(int currentX, int currentY, int targetX, int targetY) {
        return attackHandler.attackIsPossible(currentX, currentY, targetX, targetY);
    }

    public void performAttack(int currentX, int currentY, int targetX, int targetY) {
        attackHandler.performAttack(currentX, currentY, targetX, targetY);
    }
}

class AttackHandler {
    private Map gameMap;

    AttackHandler(Map gameMap) {
        this.gameMap = gameMap;
    }

    public boolean attackIsPossible(int currentX, int currentY, int targetX, int targetY) {
        if (!gameMap.coordinatesOnMap(targetX, targetY))
            return false;

        Unit currentUnit = gameMap.getUnit(currentX, currentY);
        if (UnitMovementHandler.tileIsOutOfRange(currentX, currentY, targetX, targetY, currentUnit.getAttackRange()))
            return false;

        Tile targetTile = gameMap.getTile(targetX, targetY);
        if (targetTile.hasUnit())
            return targetTile.getUnit().getOwner() != currentUnit.getOwner();
        if (targetTile.hasBuilding())
            return targetTile.getOwner() != currentUnit.getOwner();

        return false;
    }

    public void performAttack(int currentX, int currentY, int targetX, int targetY) {
        Unit currentUnit = gameMap.getUnit(currentX, currentY);

        if (gameMap.getTile(targetX, targetY).hasUnit()) {
            Unit targetUnit = gameMap.getUnit(targetX, targetY);
            targetUnit.reduceCurrentHealthBy(currentUnit.getAttackDamage());
            if (targetUnit.getCurrentHealth() <= 0)
                gameMap.killUnitAndRefundCost(targetX, targetY);
        } else {
            Building targetBuilding = gameMap.getTile(targetX, targetY).getBuilding();
            targetBuilding.reduceCurrentHealthBy(currentUnit.getAttackDamage());
            if (targetBuilding.getCurrentHealth() <= 0) {
                gameMap.destroyBuildingAndRefundCost(targetX, targetY);
                PlayerResourceHandler.calculateResources(gameMap.getTile(targetX, targetY).getOwner());
            }
        }
        currentUnit.setRemainingMoves(0);
    }
}

class UnitMovementHandler {
    private Map gameMap;

    UnitMovementHandler(Map gameMap) {
        this.gameMap = gameMap;
    }

    boolean isValidMove(int oldX, int oldY, int newX, int newY) {
        if (!gameMap.coordinatesOnMap(newX, newY))
            return false;
        Tile destinationTile = gameMap.getTile(newX, newY);
        Unit currentUnit = gameMap.getUnit(oldX, oldY);

        if (!destinationTile.isTraversable())
            return false;

        if (tileIsOutOfRange(oldX, oldY, newX, newY, currentUnit.getRemainingMoves()))
            return false;

        if (tileHasEnemyBuilding(currentUnit, destinationTile))
            return false;

        return !destinationTile.hasUnit();
    }

    private boolean tileHasEnemyBuilding(Unit currentUnit, Tile destinationTile) {
        return destinationTile.hasBuilding() && destinationTile.getOwner() != currentUnit.getOwner();
    }

    public static boolean tileIsOutOfRange(int oldX, int oldY, int newX, int newY, int range) {
        int yDistance = Math.abs(oldY - newY); //distance moved on y axis
        int xDistance = Math.abs(oldX - newX); //distance moved on x axis
        return range - yDistance - xDistance < 0;
    }

    boolean moveUnit(int oldX, int oldY, int newX, int newY) {
        if (!isValidMove(oldX, oldY, newX, newY))
            return false;

        Unit unitBeingMoved = gameMap.getUnit(oldX, oldY);
        int yDistance = Math.abs(oldY - newY); //distance moved on y axis
        int xDistance = Math.abs(oldX - newX); // distance moved on x axis
        int remainingMoves = unitBeingMoved.getRemainingMoves() - yDistance - xDistance;
        unitBeingMoved.setRemainingMoves(remainingMoves);
        gameMap.moveUnit(oldX, oldY, newX, newY);
        return true;
    }
}

class PlayerHandler {
    private ArrayList<Player> players;
    private Player currentPlayer;

    PlayerHandler() {
        players = new ArrayList<>();
    }

    public void addPlayer(String playerName) {
        Color playerColour = createNewPlayerColour();
        Player newPlayer = new Player(playerName, playerColour);

        players.add(newPlayer);
    }

    private Color createNewPlayerColour() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        return new Color(r, g, b);
    }

    public void setUpPlayers(Map gameMap) {
        currentPlayer = players.get(0);

        for (Player player : players) {
            gameMap.spawnCity(player);
            PlayerResourceHandler.calculateResources(player);
        }
    }

    public void incrementCurrentPlayer() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        if (currentPlayerIndex + 1 == players.size()) {
            currentPlayer = players.get(0);
        } else {
            currentPlayerIndex++;
            currentPlayer = players.get(currentPlayerIndex);
        }
        currentPlayer.resetUnitMoves();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

class PlayerResourceHandler {
    private static void subtractUsedResourcesUnit(Unit newUnit, Player player) {
        ResourceIterator resourceIterator = new ResourceIterator(newUnit);
        while (resourceIterator.hasNext()) {
            ResourceTypes ResourceType = resourceIterator.getType();
            player.setResource(ResourceType, player.getResource(ResourceType) - resourceIterator.getValue());
        }
    }

    private static void subtractUsedResourcesBuilding(Building building, Player player) {
        ResourceIterator resourceIterator = new ResourceIterator(building, true);
        while (resourceIterator.hasNext()) {
            ResourceTypes ResourceType = resourceIterator.getType();
            player.setResource(ResourceType, player.getResource(ResourceType) - resourceIterator.getValue());
        }
    }

    static void calculateResources(Player player) {
        ResourceIterator resourceIterator;
        player.resetResources();

        giveStartingResources(player);

        for (Building currentBuilding : player.getBuildings()) {
            if (currentBuilding.isResourceHarvester()) {
                resourceIterator = new ResourceIterator(currentBuilding, false);
                while (resourceIterator.hasNext()) {
                    ResourceTypes resourceType = resourceIterator.getType();
                    player.increaseResource(resourceType, currentBuilding.getResourceAmount(resourceType));
                }
            }
            subtractUsedResourcesBuilding(currentBuilding, player);
        }

        for (Unit currentUnit : player.getUnits()) {
            subtractUsedResourcesUnit(currentUnit, player);
        }
    }

    private static void giveStartingResources(Player player) {
        for (ResourceTypes resourceType : ResourceTypes.values()) {
            player.increaseResource(resourceType, 200);
        }
    }
}

class BuildingAndUnitCreator {
    private Map gameMap;
    private Player currentPlayer;

    BuildingAndUnitCreator(Map gameMap) {
        this.gameMap = gameMap;
    }

    void createUnit(ButtonData data, Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        String buttonText = data.getText();
        int x = data.getCurrentX();
        int y = data.getCurrentY();
        Unit newUnit;
        try {
            newUnit = UnitFactory.buildUnit(buttonText, currentPlayer);
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
            return;
        }

        if (isTileAvailable(x + 1, y)) {
            gameMap.setUnit(x + 1, y, newUnit);
        } else if (isTileAvailable(x - 1, y)) {
            gameMap.setUnit(x - 1, y, newUnit);
        } else if (isTileAvailable(x, y + 1)) {
            gameMap.setUnit(x, y + 1, newUnit);
        } else if (isTileAvailable(x, y - 1)) {
            gameMap.setUnit(x, y - 1, newUnit);
        } else {
            System.out.println("nowhere to spawn a " + buttonText);
            return;
        }
        currentPlayer.addUnit(newUnit);
        PlayerResourceHandler.calculateResources(currentPlayer);
    }

    private boolean isTileAvailable(int x, int y) {
        Tile tile = gameMap.getTile(x, y);
        return !tile.hasUnit() && tile.isTraversable() && tile.getOwner() == currentPlayer;
    }

    void createBuilding(ButtonData data, Player currentPlayer) {
        String buttonText = data.getText();
        int x = data.getCurrentX();
        int y = data.getCurrentY();

        Unit tempUnit = gameMap.getUnit(x, y);

        gameMap.constructAndSetBuildingTile(buttonText, x, y, currentPlayer);

        if (buttonText.equals("Road")) {
            gameMap.setUnit(x, y, tempUnit);
        }
    }
}