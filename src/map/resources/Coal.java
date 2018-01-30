package map.resources;

import main.ResourceTypes;

import javax.swing.*;

public class Coal extends Resource {
    public Coal() {
        this.resourceImage = new ImageIcon("textures\\terrain\\coal.png");
        this.resourceType = ResourceTypes.COAL;
        this.isHarvestable = true;
    }
}