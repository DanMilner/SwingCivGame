package main;

import map.Map;
import map.buildings.Building;
import units.Unit;

import java.awt.*;
import java.util.ArrayList;

public class Game {
    Map gameMap;
    private Player currentPlayer;
    private Player otherPlayer;
    public static final int MAPSIZE = 40;

    Game() {

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Daniel", Color.yellow));
        players.add(new Player("Alastair", Color.red));

        currentPlayer = players.get(0);
        otherPlayer = players.get(1);

        gameMap = new Map();

        for (Player player : players) {
            gameMap.spawnCity(player);
            calculateResources(player);
        }

        System.out.println("Game Made");
    }

    private void giveStartingResources(Player player) {
        for (int i = 0; i < 7; i++) {
            player.increaseResource(i, 20);
        }
        //resources needed for starting city
        player.increaseResource(0, 20);
        player.increaseResource(5, 10);
    }

    void swapPlayer() {
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

    private boolean availableTile(int x, int y) {
        return !gameMap.getTile(x, y).hasUnit() && gameMap.getTile(x, y).getOwner() == currentPlayer;
    }

    boolean isValidMove(int oldX, int oldY, int newX, int newY) {
        if (newX == oldX && newY == oldY) {
            return false;
        }

        int yDistance = Math.abs(oldY - newY); //distance moved on y axis
        int xDistance = Math.abs(oldX - newX); //distance moved on x axis
        if ((gameMap.getUnit(oldX, oldY).getRemainingMoves() - yDistance - xDistance) < 0)
            return false;    //not enough moves available
        return !gameMap.getTile(newX, newY).hasUnit();
    }

    boolean checkAvailableResources(String type) {
        return gameMap.checkCost(type, currentPlayer);
    }

    void moveUnit(int oldX, int oldY, int newX, int newY) {
        int yDistance = Math.abs(oldY - newY); //distance moved on y axis
        int xDistance = Math.abs(oldX - newX); // distance moved on x axis
        int remainingMoves = gameMap.getUnit(oldX, oldY).getRemainingMoves() - yDistance - xDistance;
        gameMap.getUnit(oldX, oldY).setRemainingMoves(remainingMoves);
        gameMap.moveUnit(oldX, oldY, newX, newY);
    }

    private void calculateResources(Player player) {
        player.resetResources();

        giveStartingResources(player);

        for (Building currentBuilding : player.getBuildings()) {
            for (int type = 0; type < 7; type++) {
                player.increaseResource(type, currentBuilding.getResourceAmount(type));
            }
            subtractUsedResources(currentBuilding.getResourceCost(), player);
        }

        for (Unit currentUnit : player.getUnits()) {
            subtractUsedResources(currentUnit.getResourceCost(), player);
        }
    }

    void buttonClicked(ButtonData data) {
        if(data.getType().equals("City")){
            cityButtons(data);
        }else{
            builderButtons(data);
        }
    }

    private void cityButtons(ButtonData data) {
        String buttonText = data.getText();
        int x = data.getCurrentX();
        int y = data.getCurrentY();
        Unit newUnit = gameMap.constructUnit(buttonText, currentPlayer);

        if (availableTile(x + 1, y)) {
            gameMap.setUnit(x + 1, y, newUnit);
        } else if (availableTile(x - 1, y)) {
            gameMap.setUnit(x - 1, y, newUnit);
        } else if (availableTile(x, y + 1)) {
            gameMap.setUnit(x, y + 1, newUnit);
        } else if (availableTile(x, y - 1)) {
            gameMap.setUnit(x, y - 1, newUnit);
        } else {
            System.out.println("nowhere to spawn a " + buttonText);
            return;
        }
        subtractUsedResources(newUnit.getResourceCost(), currentPlayer);
    }

    private void builderButtons(ButtonData data) {
        String buttonText = data.getText();
        int x = data.getCurrentX();
        int y = data.getCurrentY();

        Unit tempUnit = gameMap.getUnit(x, y);

        if (!gameMap.constructBuildingTile(buttonText, x, y, currentPlayer))
            System.out.println(buttonText + " not built");

        if(buttonText.equals("Road")){
            gameMap.setUnit(x,y,tempUnit);
        }
        calculateResources(currentPlayer);
    }
}