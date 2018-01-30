package map.resources;

import main.ResourceTypes;

import javax.swing.*;

public class Gold extends Resource {
    public Gold() {
        this.resourceImage = new ImageIcon("textures\\terrain\\gold.png");
        this.resourceType = ResourceTypes.GOLD;
        this.isHarvestable = true;
    }
}