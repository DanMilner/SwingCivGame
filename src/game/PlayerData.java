package game;

import java.awt.*;

public class PlayerData {
    private String playerName;
    private Color color;

    PlayerData(String playerName, Color color) {
        this.playerName = playerName;
        this.color = color;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Color getColor() {
        return color;
    }
}
