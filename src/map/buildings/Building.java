package map.buildings;

import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Building{
    ImageIcon buildingImage;
    int[] resourceCost = new int[ResourceTypes.getNumberOfResourceTypes()];
    int borderSize;
    boolean hasCityConnection = false;
    boolean harvestableResourceTypes[];
    ArrayList<String> buttonList;
    private int[] resourceHarvestAmount = new int[ResourceTypes.getNumberOfResourceTypes()];
    private boolean visited;
    public String type;

    public boolean canHarvestResourceType(int index){
        return harvestableResourceTypes[index];
    }

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

    public int[] getResourceCost() {
        return resourceCost;
    }

    public void setVisited(boolean visitedStatus) {
        visited = visitedStatus;
    }

    public Boolean isVisited(){
        return visited;
    }

    public int getResourceAmount(int type) {
        final int RESOURCE_BONUS = 2;
        if(hasCityConnection)
            return resourceHarvestAmount[type]*RESOURCE_BONUS;
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

    public boolean isResourceHarvester() {
        return harvestableResourceTypes != null;
    }
}
