package map.resources;

import javax.swing.*;

public class Iron extends Resource {
    public Iron() {
        this.resourceImage = new ImageIcon("textures\\terrain\\iron.png");
        this.resourceType = ResourceTypes.IRON;
        this.isHarvestable = true;
    }
}