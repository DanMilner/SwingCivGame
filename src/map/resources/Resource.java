package map.resources;

import javax.swing.*;

public class Resource {
    ImageIcon resourceImage;
    protected int resourceType;
    private boolean inUse = false;
    public String type;

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse() {
        inUse = true;
    }

    public String getType() {
        return type;
    }

    public ImageIcon getImage() {
        return resourceImage;
    }
}
