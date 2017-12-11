package map.buildings;

import javax.swing.*;
import java.util.ArrayList;

public class Building{
    ImageIcon buildingImage;
    int[] resourceCost = new int[8];
    int borderSize;
    boolean hasCityConnection = false;
    ArrayList<String> buttonList;
    private int[] resourceHarvestAmount = new int[7];
    private boolean visited;
    public String type;

    public int getBorderSize() {
        return borderSize;
    }

    public ArrayList<String> getButtonList() {
        return buttonList;
    }

    public boolean getHasCityConnection() {
        return hasCityConnection;
    }

    public void setHasCityConnection(boolean connected) {
        hasCityConnection = connected;
    }

    public String getType() {
        return type;
    }

    public int getCost(int type) {
        return resourceCost[type];
    }

    public void setVisited(boolean visitedStatus) {
        visited = visitedStatus;
    }

    public Boolean isVisited(){
        return visited;
    }

    public int getResourceAmount(int type) {
        if(hasCityConnection)
            return resourceHarvestAmount[type]*2;
        return resourceHarvestAmount[type];
    }

    public void increaseResourceHarvestAmount(int type) {
        resourceHarvestAmount[type]++;
    }

    public ImageIcon getImage() {
        return buildingImage;
    }

    public ImageIcon getImage(boolean north, boolean south, boolean east, boolean west) {
        return null;
    }
}
