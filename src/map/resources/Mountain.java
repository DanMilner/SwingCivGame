package map.resources;

import javax.swing.*;

public class Mountain extends Resource {
    public Mountain() {
        this.resourceImage = new ImageIcon("textures\\terrain\\Mountain.png");
        this.type = "Mountain";
        this.isTraversable = false;
        this.isHarvestable = true;
    }
}