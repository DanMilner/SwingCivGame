package map.buildings;

import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class City extends Building {
    public City() {
        this.buildingImage = new ImageIcon("textures\\buildings\\village.png");
        this.type = "City";

        this.resourceCost.put(ResourceTypes.WOOD, 20);
        this.resourceCost.put(ResourceTypes.STONE, 10);

        this.hasCityConnection = true;
        this.borderSize = 3;
        this.maxHealth = 1000;
        this.currentHealth = maxHealth;

        this.buttonList = new ArrayList<>();
        buttonList.add("Settler");
        buttonList.add("Builder");
        buttonList.add("Swordsman");
        buttonList.add("Archer");
        buttonList.add("Knight");
        buttonList.add("Catapult");
    }
}