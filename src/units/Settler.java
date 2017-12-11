package units;

import javax.swing.ImageIcon;

import main.Player;

import java.util.ArrayList;

public class Settler extends Unit {

	public Settler(Player owner) {
		super(owner);
		this.Img = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\unit\\settler.png");
		this.type = "Settler";
		this.moves = 2;
		this.movesAvaliable = 2;
		this.cost[6] = 20; //20 food

		this.buttonList = new ArrayList<>();
		buttonList.add("City");
	}
}
