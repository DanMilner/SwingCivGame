package map.buildings;

import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Stable extends Building {
    public Stable() {
        ImageIcon icon = new ImageIcon("textures\\buildings\\Stable.png");

        super.setImageIcon(icon);
        this.type = "Stable";

        this.resourceCost[ResourceTypes.WOOD] = 10; //10 wood
        this.resourceCost[ResourceTypes.GOLD] = 2; //2 Gold

        this.buttonList = new ArrayList<>();
        buttonList.add("Knight");
    }
}