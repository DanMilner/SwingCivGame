package game.map.resources;

import javax.swing.*;

public class Diamonds extends Resource {
    public Diamonds() {
        this.resourceImage = new ImageIcon("textures\\terrain\\diamonds.png");
        this.resourceType = ResourceTypes.DIAMONDS;
        this.isHarvestable = true;
    }
}