package units;

import javax.swing.ImageIcon;

import main.Player;

import java.util.ArrayList;

public class Builder extends Unit {

	public Builder(Player owner) {
		super(owner);
		this.Img = new ImageIcon("textures\\unit\\Builder.png");
		this.type = "Builder";
		this.moves = 4;
		this.movesAvaliable = 4;
		this.cost[6] = 2; //costs 2 food

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