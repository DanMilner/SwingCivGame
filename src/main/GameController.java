package main;

import exceptions.TypeNotFound;
import map.*;
import map.buildings.Building;
import map.resources.ResourceTypes;
import map.units.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameController {
    private Map gameMap;
    private UnitCreator unitCreator;
    private PlayerHandler playerHandler;
    private UnitMovementHandler unitMovementHandler;
    private AttackHandler attackHandler;

    //temporary until a menu GUI is made
    public static final int MAPSIZE = 40;

    GameController() {
        gameMap = new Map(false, MAPSIZE);
        unitCreator = new UnitCreator(gameMap);
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

    boolean isValidMove(Coordinates originCoordinates, Coordinates destinationCoordinates) {
        return unitMovementHandler.isValidMove(originCoordinates, destinationCoordinates);
    }

    boolean moveUnit(Coordinates originCoordinates, Coordinates destinationCoordinates) {
        return unitMovementHandler.moveUnit(originCoordinates, destinationCoordinates);
    }

    boolean checkAvailableResources(Constructable constructable, Boolean unitCheck) {
        return gameMap.checkCost(constructable, playerHandler.getCurrentPlayer(), unitCheck);
    }

    void buttonClicked(Coordinates coordinates, Constructable constructable) {
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

class AttackHandler {
    private Map gameMap;

    AttackHandler(Map gameMap) {
        this.gameMap = gameMap;
    }

    public boolean attackIsPossible(Coordinates originCoordinates, Coordinates targetCoordinates) {
        if (!gameMap.coordinatesOnMap(targetCoordinates))
            return false;

        Unit currentUnit = gameMap.getUnit(originCoordinates);
        if (UnitMovementHandler.tileIsOutOfRange(originCoordinates, targetCoordinates, currentUnit.getAttackRange()))
            return false;

        Tile targetTile = gameMap.getTile(targetCoordinates);
        if (targetTile.hasUnit())
            return targetTile.getUnit().getOwner() != currentUnit.getOwner();
        if (targetTile.hasBuilding())
            return targetTile.getOwner() != currentUnit.getOwner();

        return false;
    }

    public void performAttack(Coordinates originCoordinates, Coordinates targetCoordinates) {
        Unit currentUnit = gameMap.getUnit(originCoordinates);

        if (gameMap.getTile(targetCoordinates).hasUnit()) {
            Unit targetUnit = gameMap.getUnit(targetCoordinates);
            targetUnit.reduceCurrentHealthBy(currentUnit.getAttackDamage());
            if (targetUnit.getCurrentHealth() <= 0)
                gameMap.killUnitAndRefundCost(targetCoordinates);
        } else {
            Building targetBuilding = gameMap.getTile(targetCoordinates).getBuilding();
            targetBuilding.reduceCurrentHealthBy(currentUnit.getAttackDamage());
            if (targetBuilding.getCurrentHealth() <= 0) {
                gameMap.destroyBuildingAndRefundCost(targetCoordinates);
                PlayerResourceHandler.calculateResources(gameMap.getTile(targetCoordinates).getOwner());
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

    boolean isValidMove(Coordinates originCoordinates, Coordinates targetCoordinates) {
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

    boolean moveUnit(Coordinates originCoordinates, Coordinates targetCoordinates) {
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

class UnitCreator {
    private Map gameMap;
    private Player currentPlayer;

    UnitCreator(Map gameMap) {
        this.gameMap = gameMap;
    }

    void createUnit(Coordinates coordinates, Constructable unitType, Player currentPlayer) {
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