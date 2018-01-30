package map.resources;

import main.ResourceTypes;

import javax.swing.*;

public class Snow extends Resource {

    public Snow() {
        this.resourceImage = new ImageIcon("textures\\terrain\\Snow.png");
        this.resourceType = ResourceTypes.SNOW;
    }
}
