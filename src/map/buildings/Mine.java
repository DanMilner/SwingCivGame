package map.buildings;

import map.Constructable;
import map.resources.ResourceTypes;

import javax.swing.*;
import java.util.HashMap;

public class Mine extends Building {
    public Mine() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Quarry.png");
        this.type = Constructable.MINE;
        this.resourceCost.put(ResourceTypes.WOOD, 2);
        this.resourceHarvestAmount = new HashMap<>();
//        this.claimedResourceTiles = new ArrayList<>();

        this.borderSize = 1;
        this.maxHealth = 300;
        this.currentHealth = maxHealth;

        this.resourceHarvestAmount.put(ResourceTypes.STONE, 0);
        this.resourceHarvestAmount.put(ResourceTypes.IRON, 0);
        this.resourceHarvestAmount.put(ResourceTypes.COPPER, 0);
        this.resourceHarvestAmount.put(ResourceTypes.GOLD, 0);
        this.resourceHarvestAmount.put(ResourceTypes.COAL, 0);
        this.resourceHarvestAmount.put(ResourceTypes.DIAMONDS, 0);

    }
}

