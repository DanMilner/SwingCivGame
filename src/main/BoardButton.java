package main;

import javax.swing.*;

public class BoardButton extends JButton{
    private int xCoord;
    private int yCoord;

    BoardButton(int xCoord, int yCoord){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public int getXCoord() {
        return xCoord;
    }
}
