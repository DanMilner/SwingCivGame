package main;

import map.Map;
import map.Tile;
import map.buildings.Building;
import units.Unit;

import java.awt.*;
import java.util.ArrayList;

public class Game {
    Map gameMap;
    private Player currentPlayer;
    private Player otherPlayer;

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

    private void subtractUsedResourcesUnit(Unit newUnit, Player player) {
        for (int i = 0; i < 8; i++) {
            player.setResource(i, player.getResource(i) - newUnit.getCost(i));
        }
    }

    private void subtractUsedResourcesBuilding(Building newBuilding, Player player) {
        for (int i = 0; i < 8; i++) {
            player.setResource(i, player.getResource(i) - newBuilding.getCost(i));
        }
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    private boolean avaliableTile(int x, int y) {
        if (!gameMap.getTile(x, y).hasUnit()) {
            if (gameMap.getUnit(x, y) == null && gameMap.getTile(x, y).getOwner() == currentPlayer) {
                return true;
            }
        }
        return false;
    }

    boolean isValidMove(int oldX, int oldY, int newX, int newY) {

        if (newX == oldX && newY == oldY) {
            return false;
        }

        int yMove = Math.abs(oldY - newY); //distance moved on y axis
        int xMove = Math.abs(oldX - newX); //distance moved on x axis
        if ((gameMap.getUnit(oldX, oldY).getAvaliableMoves() - yMove - xMove) < 0)
            return false;    //not enough moves available
        if (gameMap.getUnit(newX, newY) != null)
            return false;
        return !gameMap.getTile(newX, newY).hasUnit();

    }

    boolean CheckAvailableResources(String type) {
        return gameMap.checkCost(type, currentPlayer);
    }

    void moveUnit(int oldX, int oldY, int newX, int newY) {
        int yMove = Math.abs(oldY - newY); //distance moved on y axis
        int xMove = Math.abs(oldX - newX); // distance moved on x axis
        gameMap.getUnit(oldX, oldY).setAvaliableMoves((gameMap.getUnit(oldX, oldY).getAvaliableMoves() - yMove - xMove));    //set the new number of avaliable moves
        gameMap.moveUnit(oldX, oldY, newX, newY);        //move the unit
    }

    private void calculateResources(Player player) {
        player.resetResources();

        giveStartingResources(player);

        for (Building currentBuilding : player.getBuildings()) {
            for (int type = 0; type < 7; type++) {
                player.increaseResource(type, currentBuilding.getResourceAmount(type));
            }
            subtractUsedResourcesBuilding(currentBuilding, player);
        }

        for (Unit currentUnit : player.getUnits()) {
            subtractUsedResourcesUnit(currentUnit, player);
        }
    }

    void buttonClicked(ButtonData data) {
        if(data.getType().equals("City")){
            CityButtons(data);
        }else{
            BuilderButtons(data);
        }
    }

    private void CityButtons(ButtonData data) {
        String buttonText = data.getText();
        int x = data.getCurrentX();
        int y = data.getCurrentY();
        Unit newUnit = gameMap.constructUnit(buttonText, currentPlayer);

        if (avaliableTile(x + 1, y)) {
            gameMap.setUnit(x + 1, y, newUnit);
        } else if (avaliableTile(x - 1, y)) {
            gameMap.setUnit(x - 1, y, newUnit);
        } else if (avaliableTile(x, y + 1)) {
            gameMap.setUnit(x, y + 1, newUnit);
        } else if (avaliableTile(x, y - 1)) {
            gameMap.setUnit(x, y - 1, newUnit);
        } else {
            System.out.println("nowhere to spawn a " + buttonText);
            return;
        }
        subtractUsedResourcesUnit(newUnit, currentPlayer);
    }

    private void BuilderButtons(ButtonData data) {
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