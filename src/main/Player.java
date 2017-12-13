package main;

import java.awt.Color;
import java.util.ArrayList;

import map.buildings.Building;
import units.Unit;


public class Player {
	private String name;
	private Color colour;
    private ArrayList<Building> buildings;
	private ArrayList<Unit> units;

	private int[] resources = new int[ResourceTypes.getNumberOfResourceTypes()];
	
	public Player(String name, Color colour){
		this.name = name;
		this.colour = colour;
        buildings = new ArrayList<>();
        units = new ArrayList<>();
	}

	ArrayList<Building> getBuildings(){
	    return buildings;
    }

    public void addBuilding(Building building){
	    buildings.add(building);
    }

	public void addUnit(Unit unit){
	    units.add(unit);
	}

    ArrayList<Unit> getUnits(){
        return units;
    }
	
	Color getColour(){
		return colour;
	}
	
	String getName(){
		return name;
	}
	
	void setResource(int type, int amount){
		resources[type] = amount;
	}
	
	void increaseResource(int type, int amount){
		resources[type] = resources[type]+amount;
	}

	public int getResource(int type){
		return resources[type];
	}
	
	void resetUnitMoves(){
        for (Unit currentUnit: units) {
            currentUnit.resetMoves();
        }
	}

    void resetResources() {
        for (int type = 0; type < 7; type++) {
            setResource(type, 0);
        }
    }

    public void refundUnitCost(Unit deadUnit) {
		int[] resourceCost = deadUnit.getResourceCost();
	    for (int type= 0; type<7;type++){
	        increaseResource(type, resourceCost[type]);
        }
        units.remove(deadUnit);
    }
}
