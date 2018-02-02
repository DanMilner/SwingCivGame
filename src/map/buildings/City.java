package map.buildings;

import map.Constructable;
import map.resources.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class City extends Building {
    public City() {
        this.buildingImage = new ImageIcon("textures\\buildings\\village.png");
        this.type = Constructable.CITY;

        this.resourceCost.put(ResourceTypes.WOOD, 20);
        this.resourceCost.put(ResourceTypes.STONE, 10);

        this.hasCityConnection = true;
        this.borderSize = 3;
        this.maxHealth = 1000;
        this.currentHealth = maxHealth;

        this.buttonList = new ArrayList<>();
        buttonList.add(Constructable.SETTLER);
        buttonList.add(Constructable.BUILDER);
        buttonList.add(Constructable.SWORDSMAN);
        buttonList.add(Constructable.ARCHER);
        buttonList.add(Constructable.KNIGHT);
        buttonList.add(Constructable.CATAPULT);
    }
}