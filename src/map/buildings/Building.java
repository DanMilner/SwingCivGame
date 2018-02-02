package map.buildings;

import map.Constructable;
import map.resources.ResourceTypes;
import map.resources.Resource;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Building {
    ImageIcon buildingImage;
    Map<ResourceTypes, Integer> resourceCost = new HashMap<>();
    int borderSize;
    int maxHealth;
    int currentHealth;
    boolean hasCityConnection = false;
    ArrayList<Constructable> buttonList;
    Map<ResourceTypes, Integer> resourceHarvestAmount;
    ArrayList<Resource> claimedResourceTiles;
    private boolean visited;
    public Constructable type;

    public boolean canHarvestResourceType(ResourceTypes resourceType) {
        return resourceHarvestAmount.containsKey(resourceType);
    }

    public int getBorderSize() {
        return borderSize;
    }

    public ArrayList<Constructable> getButtonList() {
        return buttonList;
    }

    public boolean getHasCityConnection() {
        return hasCityConnection;
    }

    public void setHasCityConnection(boolean connected) {
        hasCityConnection = connected;
    }

    public Constructable getType() {
        return type;
    }

    public void setVisited(boolean visitedStatus) {
        visited = visitedStatus;
    }

    public Boolean isVisited() {
        return visited;
    }

    public int getResourceAmount(ResourceTypes resourceType) {
        final int RESOURCE_BONUS = 2;
        if (hasCityConnection)
            return resourceHarvestAmount.get(resourceType) * RESOURCE_BONUS;
        return resourceHarvestAmount.get(resourceType);
    }

    public void increaseResourceHarvestAmount(ResourceTypes resourceType) {
        int amount = resourceHarvestAmount.get(resourceType) + 1;
        resourceHarvestAmount.put(resourceType, amount);
    }

    public ImageIcon getImage() {
        return buildingImage;
    }

    public ImageIcon getImage(boolean north, boolean south, boolean east, boolean west) {
        return null;
    }

    public boolean isResourceHarvester() {
        return resourceHarvestAmount != null;
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

    public Map<ResourceTypes, Integer> getResourceCostMap() {
        return resourceCost;
    }

    public Map<ResourceTypes, Integer> getHarvestedResourcesMap() {
        return resourceHarvestAmount;
    }
}
