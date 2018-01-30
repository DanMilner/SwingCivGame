package map.resources;

import main.ResourceTypes;

import javax.swing.*;

public abstract class Resource {
    ImageIcon resourceImage;
    boolean isTraversable = true;
    boolean isHarvestable = false;
    private boolean inUse = false;
    ResourceTypes resourceType;

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public ImageIcon getImage() {
        return resourceImage;
    }

    public boolean isTraversable() {
        return isTraversable;
    }

    public boolean isHarvestable() {
        return isHarvestable;
    }

    public ResourceTypes getResourceType() {
        return resourceType;
    }
}
