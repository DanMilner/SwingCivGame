package main;

import map.Constructable;

public class ButtonData {
    private int CurrentX;
    private int CurrentY;
    private Constructable constructable;

    ButtonData(int x, int y, Constructable constructable) {
        this.CurrentX = x;
        this.CurrentY = y;
        this.constructable = constructable;
    }

    int getCurrentX() {
        return CurrentX;
    }

    int getCurrentY() {
        return CurrentY;
    }

    Constructable getConstructable() {
        return constructable;
    }
}
