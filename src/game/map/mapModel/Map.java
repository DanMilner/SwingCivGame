package game.map.mapModel;

import exceptions.TypeNotFound;
import game.MapData;
import game.gameModel.Player;
import game.map.Constructable;
import game.map.Coordinates;
import game.map.buildings.Building;
import game.map.resources.Resource;
import game.map.resources.ResourceTypes;
import game.map.units.Unit;

import javax.swing.*;
import java.util.ArrayList;

public class Map {
    private final int MAPSIZE;
    private Tile[][] currentMap;
    private RoadManager roadManager;
    private MapBuilder mapBuilder;
    private TileOwnerHandler tileOwnerHandler;

    public Map(MapData mapData) {
        this.MAPSIZE = mapData.getMapsize();
        currentMap = new Tile[MAPSIZE][MAPSIZE];
        mapBuilder = new MapBuilder(currentMap, mapData);
        mapBuilder.setUpMap();
        mapBuilder.setUpTerrain();

        tileOwnerHandler = new TileOwnerHandler(this);

        roadManager = new RoadManager();
    }

    public void spawnCity(Player owner) {
        Coordinates coordinates = mapBuilder.generateNewCityCoordinates();
        constructAndSetBuildingTile(Constructable.CITY, coordinates, owner);
    }

    public boolean borderRequired(Coordinates coordinates, Coordinates adjacentCoordinates) {
        return tileOwnerHandler.borderRequired(coordinates, adjacentCoordinates);
    }

    public boolean coordinatesOnMap(Coordinates coordinates) {
        return coordinates.x >= 0 && coordinates.x < MAPSIZE && coordinates.y >= 0 && coordinates.y < MAPSIZE;
    }

    public Tile getTile(Coordinates coordinates) {
        return currentMap[coordinates.x][coordinates.y];
    }

    public void setTileResource(Coordinates coordinates, Resource newResource) {
        currentMap[coordinates.x][coordinates.y].setResource(newResource);
    }

    public Unit getUnit(Coordinates coordinates) {
        return currentMap[coordinates.x][coordinates.y].getUnit();
    }

    public void moveUnit(Coordinates originCoordinates, Coordinates targetCoordinates) {
        currentMap[targetCoordinates.x][targetCoordinates.y].setUnit(currentMap[originCoordinates.x][originCoordinates.y].getUnit());
        currentMap[originCoordinates.x][originCoordinates.y].setUnit(null);
    }

    public boolean checkCost(Constructable constructable, Player owner, Boolean unitCheck) {
        return ResourceCostChecker.checkCost(constructable, owner, unitCheck);
    }

    public void setUnit(Coordinates coordinates, Unit newUnit) {
        this.currentMap[coordinates.x][coordinates.y].setUnit(newUnit);
    }

    public void killUnitAndRefundCost(Coordinates coordinates) {
        if (currentMap[coordinates.x][coordinates.y].hasUnit()) {
            if (currentMap[coordinates.x][coordinates.y].hasBuilding()) {
                Constructable buildingType = currentMap[coordinates.x][coordinates.y].getBuilding().getType();
                if (buildingType == Constructable.WHEAT || buildingType == Constructable.ROAD)
                    return;
            }
            Unit unit = currentMap[coordinates.x][coordinates.y].getUnit();
            Player owner = unit.getOwner();
            owner.refundUnitCost(unit);
            currentMap[coordinates.x][coordinates.y].setUnit(null);
        }
    }

    private void placeWheat(Coordinates originCoordinates, Player owner) {
        final int FARM_SIZE = 2;
        Building farmBuilding = currentMap[originCoordinates.x][originCoordinates.y].getBuilding();
        Coordinates newCoordinates = new Coordinates(0, 0);
        for (int x = originCoordinates.x; x <= originCoordinates.x + FARM_SIZE; x++) {
            for (int y = originCoordinates.y - FARM_SIZE; y <= originCoordinates.y; y++) {
                newCoordinates.setCoordinates(x, y);
                if (coordinatesOnMap(newCoordinates)) {
                    if (currentMap[x][y].getResource().getResourceType() == ResourceTypes.GRASS) {
                        Unit unitOnTile = currentMap[x][y].getUnit();

                        constructAndSetBuildingTile(Constructable.WHEAT, newCoordinates, owner);

                        if (unitOnTile != null)
                            setUnit(newCoordinates, unitOnTile);

                        farmBuilding.increaseResourceHarvestAmount(ResourceTypes.FOOD);
                        farmBuilding.claimTile(currentMap[x][y]);
                    }
                }
            }
        }
    }

    public void constructAndSetBuildingTile(Constructable buildingType, Coordinates coordinates, Player owner) {
        if (!ConstructionPossibleChecker.isConstructionPossible(currentMap, buildingType, coordinates, owner))
            return;

        Building newBuilding;
        try {
            newBuilding = TileFactory.buildBuildingTile(buildingType);
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
            return;
        }

        tileOwnerHandler.setTileOwner(newBuilding, coordinates, owner);

        currentMap[coordinates.x][coordinates.y].setBuilding(newBuilding);
        roadManager.addConnectableTile(currentMap[coordinates.x][coordinates.y]);
        owner.addBuilding(newBuilding);
        killUnitAndRefundCost(coordinates);

        if (newBuilding.isResourceHarvester()) {
            if (buildingType == Constructable.FARM) {
                placeWheat(coordinates, owner);
            } else {
                ResourceYieldCalculator.calculateResourceYields(coordinates, newBuilding, owner, currentMap);
            }
        }

        System.out.println(buildingType + " spawned at " + coordinates.x + " " + coordinates.y);
    }

    public ImageIcon getTileImage(Coordinates coordinates) {
        Tile currentTile = currentMap[coordinates.x][coordinates.y];
        if (currentTile.hasUnit())
            return currentTile.getImage();
        if (currentTile.hasRoad())
            return roadManager.getRoadImage(currentTile);
        return currentTile.getImage();
    }

    public ArrayList<Constructable> getTileButtonList(boolean unitSelected, Coordinates coordinates) {
        return currentMap[coordinates.x][coordinates.y].getButtonList(unitSelected);
    }

    public void destroyBuildingAndRefundCost(Coordinates coordinates) {
        Tile tile = currentMap[coordinates.x][coordinates.y];
        tile.getOwner().destroyBuilding(tile.getBuilding());
        roadManager.removeRoad(tile);
        tile.setBuilding(null);
    }
}