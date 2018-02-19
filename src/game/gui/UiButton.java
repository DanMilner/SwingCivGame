package game.gui;

import game.map.Constructable;

import javax.swing.*;

public class UiButton extends JButton {
    private Constructable constructable;

    public Constructable getConstructable() {
        return constructable;
    }

    public void setConstructable(Constructable constructable) {
        this.constructable = constructable;
    }
}
