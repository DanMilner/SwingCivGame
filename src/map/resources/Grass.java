package map.resources;

import main.ResourceTypes;

import javax.swing.*;

public class Grass extends Resource {
    public Grass() {
        this.resourceImage = new ImageIcon("textures\\terrain\\Grass.jpg");
        this.resourceType = ResourceTypes.GRASS;
    }
}
