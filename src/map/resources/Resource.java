package map.resources;

import javax.swing.*;

public abstract class Resource {
    ImageIcon resourceImage;
    boolean isTraversable = true;
    boolean isHarvestable = false;
    private boolean inUse = false;
    public String type;

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public String getType() {
        return type;
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
}
