package map;

import main.Player;
import map.buildings.Building;
import map.resources.Resource;
import units.Unit;

import javax.swing.*;
import java.util.ArrayList;

public class Tile {
    // Instance variables.
    int Xcoord;
    int Ycoord;
    private Player owner;
    private Unit currentUnit;
    private Resource currentResource;
    private Building currentBuilding;

    public Tile(int Xcoord, int Ycoord, Player owner) {
        this.Xcoord = Xcoord;
        this.Ycoord = Ycoord;
        this.owner = owner;
        currentResource = null;
        currentBuilding = null;
    }


    public Resource getCurrentResource() {
        return currentResource;
    }

    public Building getCurrentBuilding() {
        return currentBuilding;
    }

    public boolean hasUnit() {
        return currentUnit != null;
    }

    public boolean hasBuilding() {
        return currentBuilding != null;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player newOwner) {
        owner = newOwner;
    }

    public void setCurrentBuilding(Building currentBuilding) {
        this.currentBuilding = currentBuilding;
    }

    public void setCurrentResource(Resource currentResource) {
        this.currentResource = currentResource;
    }

    Unit getCurrentUnit() {
        return currentUnit;
    }

    void setUnit(Unit newUnit) {
        currentUnit = newUnit;
    }

    public ImageIcon getImage() {
        if(hasUnit())
            return currentUnit.getImage();
        if(hasBuilding())
            return currentBuilding.getImage();
        return currentResource.getImage();
    }
}
