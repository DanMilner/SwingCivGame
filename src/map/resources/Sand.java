package map.resources;

import main.ResourceTypes;

import javax.swing.*;

public class Sand extends Resource {
    public Sand() {
        this.resourceImage = new ImageIcon("textures\\terrain\\sand.png");
        this.resourceType = ResourceTypes.SAND;
    }
}
