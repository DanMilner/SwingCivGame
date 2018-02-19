package game.map.buildings;

import game.map.Constructable;
import game.map.resources.ResourceTypes;

import javax.swing.*;

public class Tower extends Building {
    public Tower() {
        this.buildingImage = new ImageIcon("textures\\buildings\\tower.png");
        this.type = Constructable.TOWER;
        this.resourceCost.put(ResourceTypes.WOOD, 7);
        this.resourceCost.put(ResourceTypes.IRON, 1);
        this.resourceCost.put(ResourceTypes.STONE, 5);

        this.borderSize = 2;
        this.maxHealth = 500;
        this.currentHealth = maxHealth;
    }
}
