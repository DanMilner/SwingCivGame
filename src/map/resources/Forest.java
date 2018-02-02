package map.resources;

import javax.swing.*;

public class Forest extends Resource {
    public Forest() {
        this.resourceImage = new ImageIcon("textures\\terrain\\Tree.png");
        this.resourceType = ResourceTypes.WOOD;
        this.isHarvestable = true;
    }
}