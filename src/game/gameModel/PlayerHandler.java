package game.gameModel;

import game.PlayerData;
import game.map.mapModel.Map;

import java.awt.*;
import java.util.ArrayList;

public class PlayerHandler {
    private ArrayList<Player> players;
    private Player currentPlayer;

    public PlayerHandler() {
        players = new ArrayList<>();
    }

    private void addPlayer(String playerName, Color color) {
        Player newPlayer = new Player(playerName, color);

        players.add(newPlayer);
    }

    public void setUpPlayers(Map gameMap, ArrayList<PlayerData> playersToCreate) {
        for (PlayerData player : playersToCreate) {
            addPlayer(player.getPlayerName(), player.getColor());
        }

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
