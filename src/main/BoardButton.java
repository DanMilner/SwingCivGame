package main;

import javax.swing.*;

public class BoardButton extends JButton{
    private int Xcoord;
    private int Ycoord;

    BoardButton(int x, int y){
        this.Xcoord = x;
        this.Ycoord = y;
    }

    public int getYcoord() {
        return Ycoord;
    }

    public int getXcoord() {
        return Xcoord;
    }
}
