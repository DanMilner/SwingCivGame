package units;

import javax.swing.ImageIcon;

import main.Player;

import java.util.ArrayList;

public class Settler extends Unit {

	public Settler(Player owner) {
		super(owner);
		this.imageIcon = new ImageIcon("textures\\unit\\settler.png");
		this.type = "Settler";
		this.maxMoves = 2;
		this.remainingMoves = 2;
		this.resourceCost[6] = 20; //20 food

		this.buttonList = new ArrayList<>();
		buttonList.add("City");
	}
}
