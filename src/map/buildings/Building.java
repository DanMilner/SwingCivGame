package map.buildings;

import main.ResourceTypes;
import map.resources.Resource;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class Building {
    ImageIcon buildingImage;
    int[] resourceCost = new int[ResourceTypes.getNumberOfResourceTypes()];
    int borderSize;
    int maxHealth;
    int currentHealth;
    boolean hasCityConnection = false;
    boolean harvestableResourceTypes[];
    ArrayList<String> buttonList;
    private int[] resourceHarvestAmount = new int[ResourceTypes.getNumberOfResourceTypes()];
    private ArrayList<Resource> claimedResourceTiles = new ArrayList<>();
    private boolean visited;
    public String type;

    public boolean canHarvestResourceType(int index) {
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

    public Boolean isVisited() {
        return visited;
    }

    public int getResourceAmount(int type) {
        final int RESOURCE_BONUS = 2;
        if (hasCityConnection)
            return resourceHarvestAmount[type] * RESOURCE_BONUS;
        return resourceHarvestAmount[type];
    }

    public int[] getResourceHarvestAmount() {
        return resourceHarvestAmount;
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

    void setImageIcon(ImageIcon imageIcon) {
        final int BUTTON_SIZE = 50;

        Image img = imageIcon.getImage();
        Image scaledImage = img.getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, java.awt.Image.SCALE_SMOOTH);

        this.buildingImage = new ImageIcon(scaledImage);
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void claimResourceTile(Resource resourceTile) {
        claimedResourceTiles.add(resourceTile);
        resourceTile.setInUse(true);
    }

    public void reduceCurrentHealthBy(int attackDamage) {
        currentHealth -= attackDamage;
    }

    public void releaseClaimedTiles() {
        for (Resource resource : claimedResourceTiles) {
            resource.setInUse(false);
        }
    }
}
