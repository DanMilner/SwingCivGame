package main;

import java.awt.Color;
import java.util.ArrayList;

import map.Tile;
import units.Unit;


public class Player {
	private String name;
	private Color Colour;
    private ArrayList<Tile> buildings;
	private ArrayList<Unit> Units;


	private int[] Resources = new int[8];
	//int[] ResourcesSpent = new int[8];
	//resources are
	//wood = 0
	//iron = 1
	//gold = 2
	//coal = 3
	//copper = 4
	//stone = 5
	//food = 6
	//water = 7
	
	public Player(String name, Color Colour){
		this.name = name;
		this.Colour = Colour;
        buildings = new ArrayList<>();
        Units = new ArrayList<>();
	}

	ArrayList<Tile> getBuildings(){
	    return buildings;
    }

    public void addBuilding(Tile structure){
	    buildings.add(structure);
    }

	public void addUnit(Unit unit){
	    Units.add(unit);
	}

    ArrayList<Unit> getUnits(){
        return Units;
    }
	
	Color getColour(){
		return Colour;
	}
	
	String getName(){
		return name;
	}
	
	void setResource(int type, int amount){
		Resources[type] = amount;
	}
	
	void increaseResource(int type, int amount){
		Resources[type] = Resources[type]+amount;
	}

	public int getResource(int type){
		return Resources[type];
	}
	
	void resetUnitMoves(){
        for (Unit currentUnit: Units) {
            currentUnit.resetMoves();
        }
	}

    void resetResources() {
        for (int type = 0; type < 7; type++) {
            setResource(type, 0);
        }
    }

    public void refundUnitCost(Unit deadUnit) {
	    for (int type= 0; type<7;type++){
	        increaseResource(type, deadUnit.getCost(type));
        }
    }
}
