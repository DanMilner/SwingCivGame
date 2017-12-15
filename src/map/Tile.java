package map;

import main.Player;
import map.buildings.Building;
import map.resources.Resource;
import units.Unit;

import javax.swing.*;

public class Tile {
    // Instance variables.
    int xCoord;
    int yCoord;
    private Player owner;
    private Unit unit;
    private Resource resource;
    private Building building;

    Tile(int xCoord, int yCoord, Player owner) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.owner = owner;
        resource = null;
        building = null;
        unit = null;
    }


    public Resource getResource() {
        return resource;
    }

    public Building getBuilding() {
        return building;
    }

    public boolean hasRoad(){
        return hasBuilding() && building.type.equals("Road");
    }

    public boolean hasOwner(){
        return owner != null;
    }

    public boolean hasUnit() {
        return unit != null;
    }

    public boolean hasBuilding() {
        return building != null;
    }

    public boolean isTraversable(){
        return resource.isTraversable();
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player newOwner) {
        owner = newOwner;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Unit getUnit() {
        return unit;
    }

    void setUnit(Unit newUnit) {
        unit = newUnit;
    }

    public ImageIcon getImage() {
        if(hasUnit())
            return unit.getImage();
        if(hasBuilding())
            return building.getImage();
        return resource.getImage();
    }

    public boolean hasBuildingWithCityConnection(){
        return building != null && building.getHasCityConnection();
    }
}
