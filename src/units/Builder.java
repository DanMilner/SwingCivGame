package units;

import javax.swing.ImageIcon;

import main.Player;

import java.util.ArrayList;

public class Builder extends Unit {

	public Builder(Player owner) {
		super(owner);
		this.imageIcon = new ImageIcon("textures\\unit\\Builder.png");
		this.type = "Builder";
		this.maxMoves = 4;
		this.remainingMoves = 4;
		this.resourceCost[6] = 2; //costs 2 food

		this.buttonList = new ArrayList<>();
		buttonList.add("Farm");
		buttonList.add("Tower");
		buttonList.add("Mine");
		buttonList.add("Lumber Mill");
		buttonList.add("Aqueduct");
        buttonList.add("Dock");
        buttonList.add("Road");
    }
}