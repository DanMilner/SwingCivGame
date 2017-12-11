package units;

import javax.swing.ImageIcon;

import main.Player;

import java.util.ArrayList;

public class Unit {

	int moves;
	int movesAvaliable;
	ImageIcon Img;
	String type;
	private Player owner;
	int[] cost = new int[8];
	ArrayList<String> buttonList;

	Unit(Player owner)
	{
		this.owner = owner;
	}

	public ArrayList<String> getButtonList() {
		return buttonList;
	}

	public ImageIcon getImage()
	{
		return Img;
	}

	public void setAvaliableMoves(int movesAvaliable){
		this.movesAvaliable = movesAvaliable;
	}
	
	public int getAvaliableMoves(){
		return this.movesAvaliable;
	}
	
	public void resetMoves(){
		this.movesAvaliable = this.moves;
	}
	
	public Player getOwner()
	{
		return owner;
	}
	
	public String getType()
	{
		return type;
	}
	
	public int getCost(int type){
		return cost[type];
	}
}
