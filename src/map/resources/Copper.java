package map.resources;

import javax.swing.*;

public class Copper extends Resource {
    public Copper() {
        this.resourceImage = new ImageIcon("textures\\terrain\\copper.png");
        this.resourceType = ResourceTypes.COPPER;
        this.isHarvestable = true;
    }
}