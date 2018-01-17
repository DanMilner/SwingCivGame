package units;

import main.Player;
import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Unit {

	int maxMoves;
	int remainingMoves;
	int attackRange;
	ImageIcon imageIcon;
	String type;
	private Player owner;
	int[] resourceCost = new int[ResourceTypes.getNumberOfResourceTypes()];
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
		return imageIcon;
	}

	public void setRemainingMoves(int remainingMoves){
		this.remainingMoves = remainingMoves;
	}
	
	public int getRemainingMoves(){
		return remainingMoves;
	}
	
	public void resetMoves(){
		remainingMoves = maxMoves;
	}
	
	public Player getOwner()
	{
		return owner;
	}
	
	public String getType()
	{
		return type;
	}
	
	public int[] getResourceCost(){
		return resourceCost;
	}

    public int getAttackRange() {
        return attackRange;
    }
}
