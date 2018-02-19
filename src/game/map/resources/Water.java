package game.map.resources;

import javax.swing.*;

public class Water extends Resource {
    public Water() {
        this.resourceImage = new ImageIcon("textures\\terrain\\Water.jpg");
        this.resourceType = ResourceTypes.WATER;
        this.isTraversable = false;
    }
}