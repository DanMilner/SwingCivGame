package game.map.buildings;

import game.map.Constructable;
import game.map.resources.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class City extends Building {
    public City() {
        this.buildingImage = new ImageIcon("textures\\buildings\\village.png");
        this.type = Constructable.CITY;

        this.resourceCost.put(ResourceTypes.WOOD, 20);
        this.resourceCost.put(ResourceTypes.STONE, 10);

        this.hasCityConnection = true;
        this.borderSize = 1;
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