package units;

import main.Player;
import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Builder extends Unit {

	public Builder(Player owner) {
		super(owner);
		this.imageIcon = new ImageIcon("textures\\unit\\Builder.png");
		this.type = "Builder";
		this.maxMoves = 4;
		this.remainingMoves = 4;
		this.resourceCost[ResourceTypes.FOOD] = 2; //costs 2 food

		this.buttonList = new ArrayList<>();
		buttonList.add("Farm");
		buttonList.add("Tower");
		buttonList.add("Mine");
		buttonList.add("Lumber Mill");
		buttonList.add("Aqueduct");
        buttonList.add("Dock");
        buttonList.add("Road");
		buttonList.add("Barracks");
        buttonList.add("Workshop");
        buttonList.add("ArcheryRange");
        buttonList.add("Stable");
    }
}