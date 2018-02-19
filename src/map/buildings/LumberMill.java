package map.buildings;

import map.Constructable;
import map.resources.ResourceTypes;

import javax.swing.*;
import java.util.HashMap;

public class LumberMill extends Building {
    public LumberMill() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Lumber_Mill.png");
        this.type = Constructable.LUMBERMILL;
        this.resourceCost.put(ResourceTypes.STONE, 1);
        this.resourceHarvestAmount = new HashMap<>();
//        this.claimedResourceTiles = new ArrayList<>();

        this.borderSize = 1;
        this.maxHealth = 300;
        this.currentHealth = maxHealth;
        this.resourceHarvestAmount.put(ResourceTypes.WOOD, 0);
    }
}

