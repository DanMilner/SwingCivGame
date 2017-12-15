package main;

import map.Map;
import map.Tile;
import map.buildings.Building;
import units.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Game {
    private Map gameMap;
    private Player currentPlayer;
    private Player otherPlayer;
    public static final int MAPSIZE = 40;

    Game() {
        gameMap = new Map();

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Daniel", Color.yellow));
        players.add(new Player("Alastair", Color.red));

        currentPlayer = players.get(0);
        otherPlayer = players.get(1);

        for (Player player : players) {
            gameMap.spawnCity(player);
            calculateResources(player);
        }
    }

    private void giveStartingResources(Player player) {
        for (int type = 0; type < ResourceTypes.getNumberOfResourceTypes(); type++) {
            player.increaseResource(type, 20);
        }
        //resources needed for starting city
        player.increaseResource(ResourceTypes.WOOD, 20);
        player.increaseResource(ResourceTypes.STONE, 10);
    }

    void swapPlayers() {
        currentPlayer.resetUnitMoves();

        Player tempPlayer = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = tempPlayer;
    }

    public Map getMap() {
        return gameMap;
    }

    private void subtractUsedResources(int[] resourceCosts, Player player) {
        for (int i = 0; i < 8; i++) {
            player.setResource(i, player.getResource(i) - resourceCosts[i]);
        }
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    private boolean isTileAvailable(int x, int y) {
        Tile tile = gameMap.getTile(x,y);
        return !tile.hasUnit() && tile.getOwner() == currentPlayer;
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

    boolean checkAvailableResources(String type) {
        return gameMap.checkCost(type, currentPlayer);
    }

    void moveUnit(int oldX, int oldY, int newX, int newY) {
        Unit unitBeingMoved = gameMap.getUnit(oldX, oldY);
        int yDistance = Math.abs(oldY - newY); //distance moved on y axis
        int xDistance = Math.abs(oldX - newX); // distance moved on x axis
        int remainingMoves = unitBeingMoved.getRemainingMoves() - yDistance - xDistance;
        unitBeingMoved.setRemainingMoves(remainingMoves);
        gameMap.moveUnit(oldX, oldY, newX, newY);
    }

    private void calculateResources(Player player) {
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

    void buttonClicked(ButtonData data) {
        String type;
        if(data.isUnitSelected()) {
            type = gameMap.getTile(data.getCurrentX(), data.getCurrentY()).getUnit().getType();
        }else{
            type = gameMap.getTile(data.getCurrentX(), data.getCurrentY()).getBuilding().getType();
        }
        if(type.equals("City")){
            createUnit(data);
        }else{
            createBuilding(data);
        }
    }

    private void createUnit(ButtonData data) {
        String buttonText = data.getText();
        int x = data.getCurrentX();
        int y = data.getCurrentY();
        Unit newUnit = gameMap.constructUnit(buttonText, currentPlayer);

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

        subtractUsedResources(newUnit.getResourceCost(), currentPlayer);
    }

    private void createBuilding(ButtonData data) {
        String buttonText = data.getText();
        int x = data.getCurrentX();
        int y = data.getCurrentY();

        Unit tempUnit = gameMap.getUnit(x, y);

        gameMap.constructAndSetBuildingTile(buttonText, x, y, currentPlayer);

        if(buttonText.equals("Road")){
            gameMap.setUnit(x,y,tempUnit);
        }
        calculateResources(currentPlayer);
    }

    public ImageIcon getTileImage(int xCoord, int yCoord) {
        return gameMap.getTileImage(xCoord, yCoord);
    }
}