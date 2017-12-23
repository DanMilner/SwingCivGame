package map.buildings;

import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Barracks extends Building {
    public Barracks() {
        ImageIcon icon = new ImageIcon("textures\\buildings\\Barracks.png");

        super.setImageIcon(icon);
        this.type = "Barracks";

        this.resourceCost[ResourceTypes.WOOD] = 15; //15 wood
        this.resourceCost[ResourceTypes.STONE] = 3; //3 stone

        this.buttonList = new ArrayList<>();
        buttonList.add("Swordsman");
    }
}