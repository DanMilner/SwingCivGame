package main;

import exceptions.TypeNotFound;
import map.Map;
import map.Tile;
import map.buildings.Building;
import units.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class Game {
    private Map gameMap;
    private BuildingAndUnitCreator buildingAndUnitCreator;
    private PlayerHandler playerHandler;

    public static final int MAPSIZE = 40;

    Game() {
        gameMap = new Map();
        buildingAndUnitCreator = new BuildingAndUnitCreator(gameMap);
        playerHandler = new PlayerHandler();

        playerHandler.addPlayer("Daniel");
        playerHandler.addPlayer("Alastair");

        playerHandler.setUpPlayers(gameMap);
    }

    void nextPlayer() {
        playerHandler.incrementCurrentPlayer();
    }

    public Map getMap() {
        return gameMap;
    }

    Player getCurrentPlayer() {
        return playerHandler.getCurrentPlayer();
    }

    boolean isValidMove(int oldX, int oldY, int newX, int newY) {
        Tile destinationTile = gameMap.getTile(newX, newY);

        if (!destinationTile.isTraversable())
            return false;

        int yDistance = Math.abs(oldY - newY); //distance moved on y axis
        int xDistance = Math.abs(oldX - newX); //distance moved on x axis
        return (gameMap.getUnit(oldX, oldY).getRemainingMoves() - yDistance - xDistance) >= 0
                && !destinationTile.hasUnit();
    }

    boolean checkAvailableResources(String type, Boolean unitCheck) {
        return gameMap.checkCost(type, playerHandler.getCurrentPlayer(), unitCheck);
    }

    boolean moveUnit(int oldX, int oldY, int newX, int newY) {
        if(!isValidMove(oldX, oldY, newX, newY))
            return false;

        Unit unitBeingMoved = gameMap.getUnit(oldX, oldY);
        int yDistance = Math.abs(oldY - newY); //distance moved on y axis
        int xDistance = Math.abs(oldX - newX); // distance moved on x axis
        int remainingMoves = unitBeingMoved.getRemainingMoves() - yDistance - xDistance;
        unitBeingMoved.setRemainingMoves(remainingMoves);
        gameMap.moveUnit(oldX, oldY, newX, newY);
        return true;
    }

    void buttonClicked(ButtonData data) {
        String type;
        Player currentPlayer = playerHandler.getCurrentPlayer();

        if(data.isUnitSelected()) {
            type = gameMap.getTile(data.getCurrentX(), data.getCurrentY()).getUnit().getType();
        }else{
            type = gameMap.getTile(data.getCurrentX(), data.getCurrentY()).getBuilding().getType();
        }
        if(type.equals("City")){
            buildingAndUnitCreator.createUnit(data,currentPlayer);
        }else{
            buildingAndUnitCreator.createBuilding(data, currentPlayer);
            ResourceHandler.calculateResources(currentPlayer);
        }
    }

    public ImageIcon getTileImage(int xCoord, int yCoord) {
        return gameMap.getTileImage(xCoord, yCoord);
    }

    public ArrayList<String> getTileButtonList(boolean unitSelected, int currentX, int currentY) {
        return gameMap.getTileButtonList(unitSelected, currentX, currentY);
    }
}

class PlayerHandler{
    private ArrayList<Player> players;
    private Player currentPlayer;

    PlayerHandler(){
        players = new ArrayList<>();
    }

    public void addPlayer(String playerName){
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
            ResourceHandler.calculateResources(player);
        }
    }

    public void incrementCurrentPlayer(){
        int currentPlayerIndex = players.indexOf(currentPlayer);
        if(currentPlayerIndex == players.size()) {
            currentPlayer = players.get(0);
        }else{
            currentPlayerIndex++;
            currentPlayer = players.get(currentPlayerIndex);
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

class ResourceHandler{
    static void subtractUsedResources(int[] resourceCosts, Player player) {
        for (int i = 0; i < 8; i++) {
            player.setResource(i, player.getResource(i) - resourceCosts[i]);
        }
    }

    static void calculateResources(Player player) {
        player.resetResources();

        giveStartingResources(player);

        for (Building currentBuilding : player.getBuildings()) {
            for (int type = 0; type < ResourceTypes.getNumberOfResourceTypes(); type++) {
                player.increaseResource(type, currentBuilding.getResourceAmount(type));
            }
            subtractUsedResources(currentBuilding.getResourceCost(), player);
        }

        for (Unit currentUnit : player.getUnits()) {
            subtractUsedResources(currentUnit.getResourceCost(), player);
        }
    }

    private static void giveStartingResources(Player player) {
        for (int type = 0; type < ResourceTypes.getNumberOfResourceTypes(); type++) {
            player.increaseResource(type, 200);
        }
        //resources needed for starting city
        player.increaseResource(ResourceTypes.WOOD, 20);
        player.increaseResource(ResourceTypes.STONE, 10);
    }
}

class BuildingAndUnitCreator{
    private Map gameMap;
    private Player currentPlayer;

    BuildingAndUnitCreator(Map gameMap){
        this.gameMap = gameMap;
    }

    void createUnit(ButtonData data, Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        String buttonText = data.getText();
        int x = data.getCurrentX();
        int y = data.getCurrentY();
        Unit newUnit;
        try{
            newUnit = gameMap.constructUnit(buttonText, currentPlayer);
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
        ResourceHandler.subtractUsedResources(newUnit.getResourceCost(), currentPlayer);
    }

    private boolean isTileAvailable(int x, int y) {
        Tile tile = gameMap.getTile(x,y);
        return !tile.hasUnit() && tile.isTraversable() && tile.getOwner() == currentPlayer;
    }

    void createBuilding(ButtonData data, Player currentPlayer) {
        String buttonText = data.getText();
        int x = data.getCurrentX();
        int y = data.getCurrentY();

        Unit tempUnit = gameMap.getUnit(x, y);

        gameMap.constructAndSetBuildingTile(buttonText, x, y, currentPlayer);

        if(buttonText.equals("Road")){
            gameMap.setUnit(x,y,tempUnit);
        }
    }
}