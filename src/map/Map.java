package map;

import main.Game;
import main.Player;
import map.buildings.Building;
import map.resources.Resource;
import units.Unit;

import java.util.concurrent.ThreadLocalRandom;

public class Map {
    private static final int MAPSIZE = Game.MAPSIZE;
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
                    if (currentMap[x][y].getOwner() == null) {
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
                            if (currentMap[x][y].getOwner() != null) {
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
        final int BORDER_REQUIRED = 2;
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

    public boolean roadAdjacent(int x, int y) {
        return coordinatesOnMap(x, y) && roadManager.roadAdjacent(x, y);
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

    public boolean checkCost (String type, Player owner){
        final int RESOURCE_TYPES = 8;
        int[] resourceCost;
        Building buildingType = TileFactory.buildBuildingTile(type);
        if(buildingType == null){
            Unit unitType = UnitFactory.buildUnit(type, null);
            assert unitType != null;
            resourceCost = unitType.getResourceCost();
        }else{
            resourceCost = buildingType.getResourceCost();
        }

        for (int i = 0; i < RESOURCE_TYPES; i++) {
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
                calculateAdjacentResources(resourceBeingChecked, building);
            }
        }
    }

    private void calculateAdjacentResources(Resource resourceTile, Building building) {
        String type = building.getType();
        switch(type){
            case "Lumber Mill":
                if (resourceTile.getType().equals("Forest")) {
                    incrementTileResource(0, building, resourceTile);
                }
                break;
            case "Mine":
                String tempType = resourceTile.getType();
                switch (tempType) {
                    case "Iron":
                        incrementTileResource(1, building, resourceTile);
                        break;
                    case "Gold":
                        incrementTileResource(2, building, resourceTile);
                        break;
                    case "Coal":
                        incrementTileResource(3, building, resourceTile);
                        break;
                    case "Copper":
                        incrementTileResource(4, building, resourceTile);
                        break;
                    case "Mountain":
                        incrementTileResource(5, building, resourceTile);
                        break;
                }
                break;
        }
    }

    private void incrementTileResource(int type, Building building, Resource resourceTile){
        building.increaseResourceHarvestAmount(type);
        resourceTile.setInUse();
    }

    public void setUnit(int x, int y, Unit newUnit) {
        this.currentMap[x][y].setUnit(newUnit);
    }

    public Unit constructUnit(String type, Player owner) {
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
        return false;
    }

    private boolean isConstructionPossible(String type, int xCoord, int yCoord, Player owner){
        if(type.equals("Dock")){
            if(!checkForNearbyWater(xCoord, yCoord))
                return false;
        }
        Tile candidateTile = currentMap[xCoord][yCoord];
        //tile must be in nature or the current players territory.
        if(candidateTile.getOwner() == null && candidateTile.getOwner() == owner)
            return false;
        //cannot build onto of another building unless it is a road.
        if(candidateTile.hasBuilding() && !candidateTile.getBuilding().getType().equals("Road"))
            return false;
        //cannot build on top of resources currently being harvested.
        return !candidateTile.getResource().isInUse();
    }

    public boolean constructAndSetBuildingTile(String type, int x, int y, Player owner){
        if(!isConstructionPossible(type, x, y, owner))
            return false;

        Building newBuilding = TileFactory.buildBuildingTile(type);
        assert newBuilding != null;

        int borderSize = newBuilding.getBorderSize();
        setTileOwner(x-borderSize, x+borderSize, y-borderSize, y+borderSize, owner);

        if(!type.equals("Wheat"))
            killUnitAndRefundCost(x,y);

        currentMap[x][y].setBuilding(newBuilding);

        roadManager.addConnectableTile(currentMap[x][y]);

        owner.addBuilding(newBuilding);

        if(newBuilding.isResourceHarvester()){
            calculateResourceYields(x, y, newBuilding, owner);
        }else if(type.equals("Farm")){
            placeWheat(x,y,owner);
        }

        System.out.println(type + " spawned at " + x + " " + y);
        return true;
    }
}