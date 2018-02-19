package game.gui;

import game.map.Coordinates;

import javax.swing.*;

public class BoardButton extends JButton {
    private Coordinates coordinates;

    BoardButton(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
