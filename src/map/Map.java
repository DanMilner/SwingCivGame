package map;

import exceptions.TypeNotFound;
import main.Player;
import main.ResourceIterator;
import map.buildings.Building;
import map.resources.Resource;
import map.resources.ResourceTypes;
import map.units.Unit;

import javax.swing.*;
import java.util.ArrayList;

import static main.GameController.MAPSIZE;

public class Map {
    private final int MAPSIZE;
    private Tile[][] currentMap;
    private RoadManager roadManager;
    private MapBuilder mapBuilder;

    public Map(Boolean isTestMap, int MAPSIZE) {
        this.MAPSIZE = MAPSIZE;
        currentMap = new Tile[MAPSIZE + 1][MAPSIZE + 1];
        mapBuilder = new MapBuilder(currentMap, MAPSIZE);
        mapBuilder.setUpMap();

        if (!isTestMap)
            mapBuilder.setUpTerrain();

        roadManager = new RoadManager();
    }

    public void spawnCity(Player owner) {
        Coordinates coordinates = mapBuilder.generateNewCityCoordinates();
        constructAndSetBuildingTile(Constructable.CITY, coordinates, owner);
    }

    public boolean coordinatesOnMap(Coordinates coordinates) {
        return coordinates.x >= 0 && coordinates.x <= MAPSIZE && coordinates.y >= 0 && coordinates.y <= MAPSIZE;
    }

    public int borderRequired(Coordinates coordinates, int adjacentX, int adjacentY) {
        return TileOwnerHandler.borderRequired(currentMap, coordinates, adjacentX, adjacentY);
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
                        farmBuilding.claimResourceTile(currentMap[x][y].getResource());
                    }
                }
            }
        }
    }

    public void constructAndSetBuildingTile(Constructable buildingType, Coordinates coordinates, Player owner) {
        if (!ConstructionPossible.isConstructionPossible(currentMap, buildingType, coordinates, owner))
            return;

        Building newBuilding;
        try {
            newBuilding = TileFactory.buildBuildingTile(buildingType);
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
            return;
        }

        TileOwnerHandler.setTileOwner(currentMap, newBuilding, coordinates, owner);

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
        tile.getOwner().refundBuildingCost(tile.getBuilding());
        roadManager.removeRoad(tile);
        tile.setBuilding(null);
    }
}

class TileOwnerHandler {
    static void setTileOwner(Tile[][] currentMap, Building newBuilding, Coordinates coordinates, Player owner) {
        int borderSize = newBuilding.getBorderSize();
        int startX = coordinates.x - borderSize;
        int startY = coordinates.y - borderSize;
        int endX = coordinates.x + borderSize;
        int endY = coordinates.y + borderSize;
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (coordinatesOnMap(x, y)) {
                    if (!currentMap[x][y].hasOwner()) {
                        currentMap[x][y].setOwner(owner);
                    }
                }
            }
        }
    }

    static int borderRequired(Tile[][] currentMap, Coordinates coordinates, int adjacentX, int adjacentY) {
        final int BORDER_REQUIRED = 3;
        final int BORDER_NOT_REQUIRED = 0;

        if (!coordinatesOnMap(adjacentX, adjacentY)) {
            return BORDER_REQUIRED;
        }
        if (currentMap[coordinates.x][coordinates.y].getOwner() != currentMap[adjacentX][adjacentY].getOwner()) {
            return BORDER_REQUIRED;
        } else {
            return BORDER_NOT_REQUIRED;
        }
    }

    private static boolean coordinatesOnMap(int x, int y) {
        return x >= 0 && x <= MAPSIZE && y >= 0 && y <= MAPSIZE;
    }
}

class ConstructionPossible {
    static boolean isConstructionPossible(Tile[][] currentMap, Constructable buildingType, Coordinates coordinates, Player owner) {
        if (buildingType == Constructable.DOCK) {
            if (!checkForNearbyWater(currentMap, coordinates))
                return false;
        }
        Tile candidateTile = currentMap[coordinates.x][coordinates.y];

        if (isTileInEnemyTerritory(candidateTile, owner))
            return false;

        if (doesTileAlreadyHaveABuilding(candidateTile))
            return false;

        return !doesTileHaveAResourceInUse(candidateTile);
    }

    private static boolean isTileInEnemyTerritory(Tile candidateTile, Player tileOwner) {
        if (candidateTile.hasOwner() && candidateTile.getOwner() != tileOwner) {
            System.out.println("Cannot build in another players territory");
            return true;
        }
        return false;
    }

    private static boolean doesTileAlreadyHaveABuilding(Tile candidateTile) {
        if (candidateTile.hasBuilding() && !candidateTile.getBuilding().getType().equals(Constructable.ROAD)) {
            System.out.println("Cannot build on top of another building");
            return true;
        }
        return false;
    }

    private static boolean doesTileHaveAResourceInUse(Tile candidateTile) {
        if (candidateTile.getResource().isInUse()) {
            System.out.println("Cannot build on top of a resource being harvested");
            return true;
        }
        return false;
    }

    private static boolean checkForNearbyWater(Tile[][] currentMap, Coordinates coordinates) {
        final int DOCK_SIZE = 1;

        for (int x = coordinates.x - DOCK_SIZE; x <= coordinates.x + DOCK_SIZE; x++) {
            for (int y = coordinates.y - DOCK_SIZE; y <= coordinates.y + DOCK_SIZE; y++) {
                if (x <= MAPSIZE && x >= 0 && y <= MAPSIZE && y >= 0) {
                    if (currentMap[x][y].getResource().getResourceType() == ResourceTypes.WATER) {
                        return true;
                    }
                }
            }
        }
        System.out.println("Cannot build dock. There is no water nearby");
        return false;
    }
}

class ResourceCostChecker {
    static boolean checkCost(Constructable constructable, Player owner, Boolean buildingCostCheck) {
        try {
            if (buildingCostCheck) {
                Building buildingType = TileFactory.buildBuildingTile(constructable);
                return playerHasEnoughResourcesForBuilding(buildingType, owner);
            } else {
                Unit unitType = UnitFactory.buildUnit(constructable, null);
                return playerHasEnoughResourcesForUnit(unitType, owner);
            }
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
            return false;
        }
    }

    private static boolean playerHasEnoughResourcesForUnit(Unit unit, Player owner) {
        ResourceIterator resourceIterator = new ResourceIterator(unit);
        while (resourceIterator.hasNext()) {
            if (resourceIterator.getValue() > owner.getResource(resourceIterator.getType())) {
                return false;
            }
        }
        return true;
    }

    private static boolean playerHasEnoughResourcesForBuilding(Building building, Player owner) {
        ResourceIterator resourceIterator = new ResourceIterator(building, true);
        while (resourceIterator.hasNext()) {
            if (resourceIterator.getValue() > owner.getResource(resourceIterator.getType())) {
                return false;
            }
        }
        return true;
    }
}

class ResourceYieldCalculator {
    static void calculateResourceYields(Coordinates coordinates, Building building, Player currentPlayer, Tile[][] currentMap) {
        int borderSize = building.getBorderSize();
        for (int i = coordinates.x - borderSize; i <= coordinates.x + borderSize; i++) {
            for (int j = coordinates.y - borderSize; j <= coordinates.y + borderSize; j++) {
                Resource resourceBeingChecked = currentMap[i][j].getResource();
                if (currentMap[i][j].getOwner() != currentPlayer)
                    continue;
                if (resourceBeingChecked.isInUse())
                    continue;
                if (resourceBeingChecked.isHarvestable())
                    findAdjacentResourceType(resourceBeingChecked, building);
            }
        }
    }

    private static void findAdjacentResourceType(Resource resourceTile, Building building) {
        ResourceTypes resourceType = resourceTile.getResourceType();
        if (building.canHarvestResourceType(resourceType))
            incrementTileResource(resourceType, building, resourceTile);
    }

    private static void incrementTileResource(ResourceTypes resourceType, Building building, Resource resourceTile) {
        building.increaseResourceHarvestAmount(resourceType);
        building.claimResourceTile(resourceTile);
    }
}