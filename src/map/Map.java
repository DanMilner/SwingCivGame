package map;

import exceptions.TypeNotFound;
import main.GameController;
import main.Player;
import main.ResourceTypes;
import map.buildings.Building;
import map.resources.Resource;
import units.Unit;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Map {
    private static final int MAPSIZE = GameController.MAPSIZE;
    private Tile[][] currentMap = new Tile[MAPSIZE + 1][MAPSIZE + 1];
    private RoadManager roadManager;

    public Map() {
        MapBuilder mapBuilder = new MapBuilder(currentMap, MAPSIZE);
        mapBuilder.setUpMap();
        mapBuilder.setUpTerrain();

        roadManager = new RoadManager();
    }

    private void setTileOwner(int startX, int endX, int startY, int endY, Player owner){
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x <= MAPSIZE && x >= 0 && y <= MAPSIZE && y >= 0) {
                    if (!currentMap[x][y].hasOwner()) {
                        currentMap[x][y].setOwner(owner);
                    }
                }
            }
        }
    }

    public void spawnCity(Player owner) {
        final int CITY_BORDER_SIZE = 3;
        do {
            int xCoord = ThreadLocalRandom.current().nextInt(CITY_BORDER_SIZE, MAPSIZE - CITY_BORDER_SIZE);
            int yCoord = ThreadLocalRandom.current().nextInt(CITY_BORDER_SIZE, MAPSIZE - CITY_BORDER_SIZE);
            String currentResource = getTile(xCoord, yCoord).getResource().getType();
            if(!currentResource.equals("Water") && !currentResource.equals("Mountain")) {
                Boolean placeCity = true;
                //check the surrounding tiles to not collide with existing cities
                for (int x = xCoord - CITY_BORDER_SIZE; x <= xCoord + CITY_BORDER_SIZE; x++) {
                    for (int y = yCoord - CITY_BORDER_SIZE; y <= yCoord + CITY_BORDER_SIZE; y++) {
                        if(coordinatesOnMap(x,y)){
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

    private boolean coordinatesOnMap(int x, int y){
        return x >= 0 && x <= MAPSIZE && y >= 0 && y <= MAPSIZE;
    }

    public int borderRequired(int currentX, int currentY, int adjacentX, int adjacentY){
        final int BORDER_REQUIRED = 3;
        final int BORDER_NOT_REQUIRED = 0;

        if (!coordinatesOnMap(adjacentX,adjacentY)) {
            return BORDER_REQUIRED;
        }
        if (currentMap[currentX][currentY].getOwner() != currentMap[adjacentX][adjacentY].getOwner()) {
            return BORDER_REQUIRED;
        } else {
            return BORDER_NOT_REQUIRED;
        }
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
        int[] resourceCost;

        try{
            if(!unitCheck){
                Unit unitType = UnitFactory.buildUnit(type, null);
                resourceCost = unitType.getResourceCost();
            }else {
                Building buildingType = TileFactory.buildBuildingTile(type);
                resourceCost = buildingType.getResourceCost();
            }
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
            return false;
        }

        for (int i = 0; i < ResourceTypes.getNumberOfResourceTypes(); i++) {
            if (resourceCost[i] > owner.getResource(i)) {
                return false;
            }
        }
        return true;
    }

    private void calculateResourceYields(int x, int y, Building building, Player currentPlayer) {
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

    private void findAdjacentResourceType(Resource resourceTile, Building building) {
        String type = resourceTile.getType();
        int index = ResourceTypes.getResourceTypeIndex(type);
        if(building.canHarvestResourceType(index))
            incrementTileResource(index, building, resourceTile);
    }

    private void incrementTileResource(int type, Building building, Resource resourceTile){
        building.increaseResourceHarvestAmount(type);
        resourceTile.setInUse();
    }

    public void setUnit(int x, int y, Unit newUnit) {
        this.currentMap[x][y].setUnit(newUnit);
    }

    public Unit constructUnit(String type, Player owner) throws TypeNotFound {
        return UnitFactory.buildUnit(type,owner);
    }

    private void killUnitAndRefundCost(int x, int y) {
        if(currentMap[x][y].hasUnit()){
            currentMap[x][y].getUnit().getOwner().refundUnitCost(currentMap[x][y].getUnit());
            currentMap[x][y].setUnit(null);
        }
    }

    private void placeWheat(int xCoord, int yCoord, Player owner) {
        final int FARM_SIZE = 2;
        for (int x = xCoord; x <= xCoord + FARM_SIZE; x++) {
            for (int y = yCoord - FARM_SIZE; y <= yCoord; y++) {
                if(coordinatesOnMap(x,y)){
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

    private boolean checkForNearbyWater(int xCoord, int yCoord){
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

    private boolean isConstructionPossible(String type, int xCoord, int yCoord, Player owner){
        if(type.equals("Dock")){
            if(!checkForNearbyWater(xCoord, yCoord))
                return false;
        }
        Tile candidateTile = currentMap[xCoord][yCoord];
        if(!candidateTile.hasOwner() && candidateTile.getOwner() == owner) {
            System.out.println("Cannot build " + type + ". Cannot build in another players territory");
            return false;
        }
        if(candidateTile.hasBuilding() && !candidateTile.getBuilding().getType().equals("Road")) {
            System.out.println("Cannot build " + type + ". Cannot build on top of another building");
            return false;
        }
        if(candidateTile.getResource().isInUse()){
            System.out.println("Cannot build " + type + ". Cannot build on top of a resource being harvested");
            return false;
        }
        return true;
    }

    public void constructAndSetBuildingTile(String type, int x, int y, Player owner){

        if(!isConstructionPossible(type, x, y, owner))
            return;

        Building newBuilding;
        try {
            newBuilding = createBuildingTile(type);
        }catch (TypeNotFound typeNotFound){
            typeNotFound.printStackTrace();
            return;
        }

        int borderSize = newBuilding.getBorderSize();
        setTileOwner(x-borderSize, x+borderSize, y-borderSize, y+borderSize, owner);

        if(!type.equals("Wheat") && !type.equals("Road")) {
            killUnitAndRefundCost(x, y);
        }

        currentMap[x][y].setBuilding(newBuilding);

        roadManager.addConnectableTile(currentMap[x][y]);

        owner.addBuilding(newBuilding);

        if(newBuilding.isResourceHarvester()){
            calculateResourceYields(x, y, newBuilding, owner);
        }else if(type.equals("Farm")){
            placeWheat(x,y,owner);
        }

        System.out.println(type + " spawned at " + x + " " + y);
    }

    private Building createBuildingTile(String type) throws TypeNotFound {
        return TileFactory.buildBuildingTile(type);
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
