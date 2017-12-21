package map;

import exceptions.TypeNotFound;
import main.Player;
import main.ResourceTypes;
import map.buildings.Building;
import map.resources.Resource;
import units.Unit;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static main.GameController.MAPSIZE;

public class Map {
    private final int MAPSIZE;
    private Tile[][] currentMap;
    private RoadManager roadManager;

    public Map(Boolean testMap, int MAPSIZE) {
        this.MAPSIZE = MAPSIZE;
        currentMap = new Tile[MAPSIZE + 1][MAPSIZE + 1];
        MapBuilder mapBuilder = new MapBuilder(currentMap, MAPSIZE);
        mapBuilder.setUpMap();

        if(!testMap)
            mapBuilder.setUpTerrain();

        roadManager = new RoadManager();
    }

    public void spawnCity(Player owner) {
        final int CITY_BORDER_SIZE = 3;
        int xCoord;
        int yCoord;
        Resource currentTileResource;
        do {
            xCoord = ThreadLocalRandom.current().nextInt(CITY_BORDER_SIZE, MAPSIZE - CITY_BORDER_SIZE);
            yCoord = ThreadLocalRandom.current().nextInt(CITY_BORDER_SIZE, MAPSIZE - CITY_BORDER_SIZE);
            currentTileResource = getTile(xCoord, yCoord).getResource();
            if(currentTileResource.isTraversable()){
                Boolean placeCity = true;
                //check the surrounding tiles to not collide with existing cities
                for (int x = xCoord - CITY_BORDER_SIZE; x <= xCoord + CITY_BORDER_SIZE; x++) {
                    for (int y = yCoord - CITY_BORDER_SIZE; y <= yCoord + CITY_BORDER_SIZE; y++) {
                        if(coordinatesOnMap(x,y, MAPSIZE)){
                            if (currentMap[x][y].hasOwner()) {
                                placeCity = false; //if a tile is owned by another player then the cities are too close together
                            }
                        }
                    }
                }
                if (placeCity) {
                    constructAndSetBuildingTile("City", xCoord, yCoord, owner);
                    return;
                }
            }
        } while (true);
    }

    static boolean coordinatesOnMap(int x, int y, int MAPSIZE){
        return x >= 0 && x <= MAPSIZE && y >= 0 && y <= MAPSIZE;
    }

    public int borderRequired(int currentX, int currentY, int adjacentX, int adjacentY){
        return TileOwnerHandler.borderRequired(currentMap, currentX,currentY,adjacentX,adjacentY);
    }

    public Tile getTile(int x, int y) {
        return currentMap[x][y];
    }

    public Unit getUnit(int x, int y) {
        return currentMap[x][y].getUnit();
    }

    public void moveUnit(int oldX, int oldY, int newX, int newY) {
        currentMap[newX][newY].setUnit(currentMap[oldX][oldY].getUnit());
        currentMap[oldX][oldY].setUnit(null);
    }

    public boolean checkCost (String type, Player owner, Boolean unitCheck){
        return ResourceCostChecker.checkCost(type,owner,unitCheck);
    }

    public void setUnit(int x, int y, Unit newUnit) {
        this.currentMap[x][y].setUnit(newUnit);
    }

    private void killUnitAndRefundCost(int x, int y) {
        if(currentMap[x][y].hasUnit()){
            String buildingType = currentMap[x][y].getBuilding().getType();
            if(buildingType.equals("Wheat") || buildingType.equals("Road"))
                return;

            currentMap[x][y].getUnit().getOwner().refundUnitCost(currentMap[x][y].getUnit());
            currentMap[x][y].setUnit(null);
        }
    }

    private void placeWheat(int xCoord, int yCoord, Player owner) {
        final int FARM_SIZE = 2;
        for (int x = xCoord; x <= xCoord + FARM_SIZE; x++) {
            for (int y = yCoord - FARM_SIZE; y <= yCoord; y++) {
                if(coordinatesOnMap(x,y, MAPSIZE)){
                    if (currentMap[x][y].getResource().getType().equals("Grass")) {
                        Unit unitOnTile = currentMap[x][y].getUnit();

                        constructAndSetBuildingTile("Wheat", x, y, owner);

                        if(unitOnTile != null)
                            setUnit(x, y, unitOnTile);

                        currentMap[xCoord][yCoord].getBuilding().increaseResourceHarvestAmount(6);
                        currentMap[x][y].getResource().setInUse();
                    }
                }
            }
        }
    }

    public void constructAndSetBuildingTile(String type, int x, int y, Player owner){
        if(!ConstructionPossible.isConstructionPossible(currentMap, type, x, y, owner))
            return;

        Building newBuilding;
        try {
            newBuilding = TileFactory.buildBuildingTile(type);
        }catch (TypeNotFound typeNotFound){
            typeNotFound.printStackTrace();
            return;
        }

        TileOwnerHandler.setTileOwner(currentMap, newBuilding,x, y, owner);

        currentMap[x][y].setBuilding(newBuilding);
        roadManager.addConnectableTile(currentMap[x][y]);
        owner.addBuilding(newBuilding);
        killUnitAndRefundCost(x, y);

        if(newBuilding.isResourceHarvester()){
            ResourceYieldCalculator.calculateResourceYields(x, y, newBuilding, owner,currentMap);
        }else if(type.equals("Farm")){
            placeWheat(x,y,owner);
        }

        System.out.println(type + " spawned at " + x + " " + y);
    }

    public ImageIcon getTileImage(int x, int y) {
        Tile currentTile = currentMap[x][y];
        if(currentTile.hasUnit())
            return currentTile.getImage();
        if(currentTile.hasRoad())
            return roadManager.getRoadImage(currentTile);
        return currentTile.getImage();
    }

    public ArrayList<String> getTileButtonList(boolean unitSelected, int currentX, int currentY) {
        return currentMap[currentX][currentY].getButtonList(unitSelected);
    }
}

class TileOwnerHandler{
    static void setTileOwner(Tile[][] currentMap, Building newBuilding, int xOrigin, int yOrigin, Player owner){
        int borderSize = newBuilding.getBorderSize();
        int startX = xOrigin-borderSize;
        int startY = yOrigin-borderSize;
        int endX = xOrigin + borderSize;
        int endY = yOrigin + borderSize;
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (Map.coordinatesOnMap(x,y,currentMap.length)) {
                    if (!currentMap[x][y].hasOwner()) {
                        currentMap[x][y].setOwner(owner);
                    }
                }
            }
        }
    }

    static int borderRequired(Tile[][] currentMap, int currentX, int currentY, int adjacentX, int adjacentY){
        final int BORDER_REQUIRED = 3;
        final int BORDER_NOT_REQUIRED = 0;

        if (!Map.coordinatesOnMap(adjacentX,adjacentY,currentMap.length)) {
            return BORDER_REQUIRED;
        }
        if (currentMap[currentX][currentY].getOwner() != currentMap[adjacentX][adjacentY].getOwner()) {
            return BORDER_REQUIRED;
        } else {
            return BORDER_NOT_REQUIRED;
        }
    }
}

class ConstructionPossible{
    static boolean isConstructionPossible(Tile[][] currentMap, String type, int xCoord, int yCoord, Player owner){
        if(type.equals("Dock")){
            if(!checkForNearbyWater(currentMap, xCoord, yCoord))
                return false;
        }
        Tile candidateTile = currentMap[xCoord][yCoord];

        if(isTileInEnemyTerritory(candidateTile,owner))
            return false;

        if(doesTileAlreadyHaveABuilding(candidateTile))
            return false;

        return !doesTileHaveAResourceInUse(candidateTile);
    }

    private static boolean isTileInEnemyTerritory(Tile candidateTile, Player tileOwner){
        if(candidateTile.hasOwner() && candidateTile.getOwner() != tileOwner) {
            System.out.println("Cannot build in another players territory");
            return true;
        }
        return false;
    }

    private static boolean doesTileAlreadyHaveABuilding(Tile candidateTile){
        if(candidateTile.hasBuilding() && !candidateTile.getBuilding().getType().equals("Road")) {
            System.out.println("Cannot build on top of another building");
            return true;
        }
        return false;
    }

    private static boolean doesTileHaveAResourceInUse(Tile candidateTile){
        if(candidateTile.getResource().isInUse()){
            System.out.println("Cannot build on top of a resource being harvested");
            return true;
        }
        return false;
    }

    private static boolean checkForNearbyWater(Tile[][] currentMap, int xCoord, int yCoord){
        final int DOCK_SIZE = 1;

        for (int x = xCoord-DOCK_SIZE; x <= xCoord+DOCK_SIZE; x++) {
            for (int y = yCoord-DOCK_SIZE; y <= yCoord+DOCK_SIZE; y++) {
                if (x <= MAPSIZE && x >= 0 && y <= MAPSIZE && y >= 0) {
                    if (currentMap[x][y].getResource().getType().equals("Water")) {
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
    static boolean checkCost (String type, Player owner, Boolean buildingCostCheck){
        int[] resourceCost;

        try{
            if(buildingCostCheck){
                resourceCost = getBuildingResourceCost(type);
            }else {
                resourceCost = getUnitResourceCost(type);
            }
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
            return false;
        }
        return playerHasEnoughResources(resourceCost, owner);
    }

    private static int[] getUnitResourceCost(String type) throws TypeNotFound {
        Unit unitType = UnitFactory.buildUnit(type, null);
        return unitType.getResourceCost();
    }

    private static int[] getBuildingResourceCost(String type) throws TypeNotFound {
        Building buildingType = TileFactory.buildBuildingTile(type);
        return buildingType.getResourceCost();
    }

    private static boolean playerHasEnoughResources(int[] resourceCost, Player owner){
        for (int i = 0; i < ResourceTypes.getNumberOfResourceTypes(); i++) {
            if (resourceCost[i] > owner.getResource(i)) {
                return false;
            }
        }
        return true;
    }
}

class ResourceYieldCalculator {
    static void calculateResourceYields(int x, int y, Building building, Player currentPlayer, Tile[][] currentMap) {
        int borderSize = building.getBorderSize();
        for (int i = x - borderSize; i <= x + borderSize; i++) {
            for (int j = y - borderSize; j <= y + borderSize; j++) {
                Resource resourceBeingChecked = currentMap[i][j].getResource();
                if (currentMap[i][j].getOwner() != currentPlayer)
                    return;
                if (resourceBeingChecked.isInUse())
                    return;
                if (resourceBeingChecked.isHarvestable())
                    findAdjacentResourceType(resourceBeingChecked, building);
            }
        }
    }

    private static void findAdjacentResourceType(Resource resourceTile, Building building) {
        String type = resourceTile.getType();
        int index = ResourceTypes.getResourceTypeIndex(type);
        if(building.canHarvestResourceType(index))
            incrementTileResource(index, building, resourceTile);
    }

    private static void incrementTileResource(int type, Building building, Resource resourceTile){
        building.increaseResourceHarvestAmount(type);
        resourceTile.setInUse();
    }
}
