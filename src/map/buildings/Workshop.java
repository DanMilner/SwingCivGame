package map.buildings;

import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Workshop extends Building {
    public Workshop() {
        ImageIcon icon = new ImageIcon("textures\\buildings\\Workshop.png");

        super.setImageIcon(icon);
        this.type = "Workshop";

        this.resourceCost[ResourceTypes.COPPER] = 2; //20 Copper
        this.resourceCost[ResourceTypes.WOOD] = 10; //10 Wood
        this.resourceCost[ResourceTypes.IRON] = 1; //1 Iron

        this.buttonList = new ArrayList<>();
        buttonList.add("Catapult");
    }
}