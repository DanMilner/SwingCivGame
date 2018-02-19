package game.map.mapModel;

import game.gameModel.Player;
import game.map.Constructable;
import game.map.Coordinates;
import game.map.buildings.Building;
import game.map.resources.Resource;
import game.map.units.Unit;

import javax.swing.*;
import java.util.ArrayList;

public class Tile {
    private Player owner;
    private Unit unit;
    private Resource resource;
    private Building building;
    private Building claimedBy;
    private Coordinates coordinates;

    public Tile(Coordinates coordinates, Player owner) {
        this.coordinates = coordinates;
        this.owner = owner;
        resource = null;
        building = null;
        unit = null;
        claimedBy = null;
    }

    public void setClaimedBy(Building building){
        this.claimedBy = building;
    }

    public Building getClaimedBy() {
        return claimedBy;
    }

    public Resource getResource() {
        return resource;
    }

    public Building getBuilding() {
        return building;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public boolean hasRoad() {
        return hasBuilding() && building.type == Constructable.ROAD;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public boolean hasUnit() {
        return unit != null;
    }

    public boolean hasBuilding() {
        return building != null;
    }

    public boolean isTraversable() {
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

    public void setUnit(Unit newUnit) {
        unit = newUnit;
    }

    public ImageIcon getImage() {
        if (hasUnit())
            return unit.getImage();
        if (hasBuilding())
            return building.getImage();
        return resource.getImage();
    }

    public boolean hasBuildingWithCityConnection() {
        return building != null && building.getHasCityConnection();
    }

    public ArrayList<Constructable> getButtonList(boolean unitSelected) {
        if (unitSelected)
            return unit.getButtonList();
        return building.getButtonList();
    }

    public void setNatureTile() {
        getResource().setInUse(false);
        setOwner(null);
        setClaimedBy(null);
    }

    public boolean isClaimed() {
        return claimedBy != null;
    }
}
