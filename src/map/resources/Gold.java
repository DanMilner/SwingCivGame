package map.resources;

import javax.swing.*;

public class Gold extends Resource {
    public Gold() {
        this.resourceImage = new ImageIcon("textures\\terrain\\gold.png");
        this.type = "Gold";
        this.isHarvestable = true;
    }
}