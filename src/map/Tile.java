package map;

import main.Player;
import units.Unit;

import javax.swing.*;
import java.util.ArrayList;

public class Tile {
    // Instance variables.
    int Xcoord;
    int Ycoord;
    public String type;
    protected ImageIcon tileImage;
    protected boolean isOccupied = false;
    protected int[] cost = new int[8];
    protected int resourceType;
    private Player owner;
    private Unit currentUnit;
    private int[] resourceAmount = new int[7];
    protected boolean hasCityConnection = false;
    private boolean inUse = false;
    private boolean visited;
    protected ArrayList<String> buttonList;
    protected int borderSize;

    public Tile(int Xcoord, int Ycoord, Player owner) {
        this.Xcoord = Xcoord;
        this.Ycoord = Ycoord;
        this.owner = owner;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public ArrayList<String> getButtonList() {
        return buttonList;
    }

    public boolean isInUse() {
        return inUse;
    }

    void setInUse() {
        this.inUse = true;
    }

    public boolean getHasCityConnection() {
        return hasCityConnection;
    }

    void setHasCityConnection(boolean connected) {
        this.hasCityConnection = connected;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    void increaseResourceAmount(int type) {
        this.resourceAmount[type]++;
    }

    public int getResourceAmount(int type) {
        if(hasCityConnection)
            return resourceAmount[type]*2;
        return resourceAmount[type];
    }

    public String getType() {
        return type;
    }

    Unit getUnit() {
        return currentUnit;
    }

    void setUnit(Unit currentUnit) {
        this.currentUnit = currentUnit;
    }

    public int getCost(int type) {
        return cost[type];
    }

    public ImageIcon getImage() {
        return tileImage;
    }

    public ImageIcon getImage(boolean north, boolean south, boolean east, boolean west) {
        return null;
    }

    void setVisited(boolean visited) {
        this.visited = visited;
    }
    Boolean isVisited(){
        return visited;
    }
}
