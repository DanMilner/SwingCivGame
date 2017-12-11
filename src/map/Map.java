package map;

import main.CivGUI;
import main.Player;
import map.buildings.Building;
import map.buildings.Road;
import map.resources.Resource;
import units.Unit;

import java.util.concurrent.ThreadLocalRandom;

public class Map {
    private static final int MAPSIZE = CivGUI.MAPSIZE;
    private Tile[][] currentMap = new Tile[MAPSIZE + 1][MAPSIZE + 1];
    private RoadManager roadManager;

    public Map() {
        for (int x = 0; x <= MAPSIZE; x++) {
            for (int y = 0; y <= MAPSIZE; y++) {
                currentMap[x][y] = new Tile(x,y,null);
            }
        }

        //create board of just grass;
        for (int x = 0; x <= MAPSIZE; x++) {
            for (int y = 0; y <= MAPSIZE; y++) {
                constructResourceTile("Grass", x, y);
                currentMap[x][y].setUnit(null);
            }
        }

        roadManager = new RoadManager();

        //add trees
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE * 12, MAPSIZE * 15), 0, "Forest");

        //add Water
        addWater(ThreadLocalRandom.current().nextInt(MAPSIZE / 10, MAPSIZE / 5), ThreadLocalRandom.current().nextInt(MAPSIZE / 2, MAPSIZE * 2));

        //add Mountains
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 11, MAPSIZE / 7), ThreadLocalRandom.current().nextInt(MAPSIZE / 4, MAPSIZE), "Mountain");

        //add Iron
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 4, MAPSIZE / 3), ThreadLocalRandom.current().nextInt(3, MAPSIZE / 10), "Iron");

        //add Gold
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 10, MAPSIZE / 7), ThreadLocalRandom.current().nextInt(2, MAPSIZE / 10), "Gold");

        //add Copper
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 7, MAPSIZE / 6), ThreadLocalRandom.current().nextInt(2, MAPSIZE / 10), "Copper");

        //add Coal
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 5, MAPSIZE / 4), ThreadLocalRandom.current().nextInt(2, MAPSIZE / 10), "Coal");
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
        int amount = 0;
        do {
            int Xcoords = ThreadLocalRandom.current().nextInt(3, MAPSIZE - 3);
            int Ycoords = ThreadLocalRandom.current().nextInt(3, MAPSIZE - 3);
            String currentResource = getTile(Xcoords, Ycoords).getCurrentResource().getType();
            if(!currentResource.equals("Water") && !currentResource.equals("Mountain")) {
                boolean placeCity = true;
                //check the surrounding tiles to not collide with existing cities
                for (int x = Xcoords - 3; x < (Xcoords + 4); x++) {
                    for (int y = Ycoords - 3; y < (Ycoords + 4); y++) {
                        if (x <= MAPSIZE && x >= 0 && y <= MAPSIZE && y >= 0) {
                            if (currentMap[x][y].getOwner() != null) {
                                placeCity = false; //if a tile is owned by another player then the cities are too close together
                            }
                        }
                    }
                }
                if (placeCity) {
                    constructBuildingTile("City", Xcoords, Ycoords, owner);
                    amount++;
                }
            }
        } while (amount < 1);
    }

    public int BorderRequired(int x, int y, int nx, int ny){
        if (nx < 0 || nx > MAPSIZE || ny < 0 || ny > MAPSIZE) {
            return 2; //these values are not on the board
        }
        if (currentMap[x][y].getOwner() != currentMap[nx][ny].getOwner()) {
            return 2;
        } else {
            return 0;
        }
    }

    public boolean RoadAdjacent(int x, int y){
        if (x < 0 || x > MAPSIZE || y < 0 || y > MAPSIZE)
            return false; //these values are not on the board
        return roadManager.roadAdjacent(x,y);
    }

    private void addWater(int amount, int intensity) {

        int Xcoords;
        int Ycoords;

        for (int y = 0; y < amount; y++) {
            Xcoords = ThreadLocalRandom.current().nextInt(1, MAPSIZE);
            Ycoords = ThreadLocalRandom.current().nextInt(1, MAPSIZE);
            int originalX = Xcoords;
            int originalY = Ycoords;
            int direction;
            int i = 0;
            do {
                direction = ThreadLocalRandom.current().nextInt(1, 5);
                if (direction == 1) { //North
                    Ycoords = Ycoords + 1;
                } else if (direction == 2) { //South
                    Ycoords = Ycoords - 1;
                } else if (direction == 3) { //East
                    Xcoords = Xcoords + 1;
                } else if (direction == 4) { //West
                    Xcoords = Xcoords - 1;
                }
                if (Ycoords <= MAPSIZE && Xcoords <= MAPSIZE && Ycoords >= 0 && Xcoords >= 0) {
                    if (!getTile(Xcoords, Ycoords).getCurrentResource().getType().equals("Water")) {
                        constructResourceTile("Water", Xcoords, Ycoords);
                        Xcoords = originalX;
                        Ycoords = originalY;
                        i++;
                    }
                } else {
                    Xcoords = originalX;
                    Ycoords = originalY;
                }
            } while (i < intensity);
        }
    }

    private void addTerrain(int amount, int intensity, String type) {

        int Xcoords;
        int Ycoords;

        for (int y = 0; y < amount; y++) {
            do {
                Xcoords = ThreadLocalRandom.current().nextInt(0, MAPSIZE);
                Ycoords = ThreadLocalRandom.current().nextInt(0, MAPSIZE);
            } while (currentMap[Xcoords][Ycoords].getCurrentResource().getType().equals("Water"));

            if (type.equals("Forest")) {
                constructResourceTile("Forest", Xcoords, Ycoords);
            } else {
                for (int i = 0; i < intensity; i++) {
                    int direction = ThreadLocalRandom.current().nextInt(1, 5);

                    if (direction == 1) { //North
                        Ycoords = Ycoords + 1;
                    } else if (direction == 2) { //South
                        Ycoords = Ycoords - 1;
                    } else if (direction == 3) { //East
                        Xcoords = Xcoords + 1;
                    } else if (direction == 4) { //West
                        Xcoords = Xcoords - 1;
                    }
                    if (Ycoords <= MAPSIZE && Xcoords <= MAPSIZE && Ycoords >= 0 && Xcoords >= 0) {
                        String currentResource = getTile(Xcoords, Ycoords).getCurrentResource().getType();

                        if (currentResource.equals("Mountain") || currentResource.equals("Iron") || currentResource.equals("Gold") || currentResource.equals("Copper") || currentResource.equals("Coal")) {
                            i--;
                        } else {
                            constructResourceTile(type, Xcoords, Ycoords);
                        }
                    }
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        return currentMap[x][y];
    }

    public Unit getUnit(int x, int y) {
        return currentMap[x][y].getCurrentUnit();
    }

    public void moveUnit(int oldX, int oldY, int newX, int newY) {
        currentMap[newX][newY].setUnit(currentMap[oldX][oldY].getCurrentUnit());
        currentMap[oldX][oldY].setUnit(null);
    }

    public boolean checkCost (String type, Player owner){

        TileFactory tileFactory = new TileFactory();
        Building buildingType = tileFactory.buildBuildingTile(type);
        if(buildingType == null){
            UnitFactory factory = new UnitFactory();
            Unit unitType = factory.BuildUnit(type, null);
            for (int i = 0; i < 8; i++) {
                if (unitType.getCost(i) > owner.getResource(i)) {
                    return false;
                }
            }
        }else{
            for (int i = 0; i < 8; i++) {
                if (buildingType.getCost(i) > owner.getResource(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void calculateResourceYields(int x, int y, Building building, Player currentPlayer) {
        for (int i = (x - 1); i < (x + 2); i++) {
            for (int j = (y - 1); j < (y + 2); j++) {
                Resource resourceBeingChecked = currentMap[i][j].getCurrentResource();
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
        newUnit.getOwner().addUnit(newUnit);
    }

    public Unit constructUnit(String type, Player owner) {
        UnitFactory factory = new UnitFactory();
        return factory.BuildUnit(type,owner);
    }

    private void killUnit(int x, int y) {
        if(this.currentMap[x][y].getCurrentUnit() != null){
            this.currentMap[x][y].getCurrentUnit().getOwner().refundUnitCost(this.currentMap[x][y].getCurrentUnit());
            this.currentMap[x][y].setUnit(null);
        }
    }

    private void placeWheat(int Xcoords, int Ycoords, Player owner) {
        for (int x = Xcoords; x < (Xcoords + 3); x++) {
            for (int y = Ycoords - 2; y < (Ycoords + 1); y++) {
                if (x <= MAPSIZE && y <= MAPSIZE && y >= 0) {
                    if (currentMap[x][y].getCurrentResource().getType().equals("Grass")) {
                        Unit tempUnit = currentMap[x][y].getCurrentUnit();
                        constructBuildingTile("Wheat", x, y, owner);
                        if(tempUnit != null)
                            setUnit(x, y, tempUnit);
                        currentMap[Xcoords][Ycoords].getCurrentBuilding().increaseResourceHarvestAmount(6);
                        currentMap[x][y].getCurrentResource().setInUse();
                    }
                }
            }
        }
    }

    private boolean checkForNearbyWater(int Xcoords, int Ycoords){
        for (int x = Xcoords-1; x <= Xcoords+1; x++) {
            for (int y = Ycoords-1; y <= Ycoords+1; y++) {
                if (x <= MAPSIZE && x >= 0 && y <= MAPSIZE && y >= 0) {
                    if (currentMap[x][y].getCurrentResource().getType().equals("Water")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isConstructionPossible(String type, int Xcoords, int Ycoords, Player owner){
        if(type.equals("Dock")){
            if(!checkForNearbyWater(Xcoords, Ycoords))
                return false;
        }
        Tile candidateTile = currentMap[Xcoords][Ycoords];
        //tile must be in nature or the current players territory.
        if(candidateTile.getOwner() == null && candidateTile.getOwner() == owner)
            return false;
        //cannot build onto of another building unless it is a road.
        if(candidateTile.hasBuilding() && !candidateTile.getCurrentBuilding().getType().equals("Road"))
            return false;
        //cannot build on top of resources currently being harvested.
        return !candidateTile.getCurrentResource().isInUse();
    }

    public boolean constructBuildingTile(String type, int x, int y, Player owner){
        if(!isConstructionPossible(type, x, y, owner))
            return false;

        TileFactory factory = new TileFactory();
        Building newBuilding = factory.buildBuildingTile(type);

        int borderSize = newBuilding.getBorderSize();
        setTileOwner(x-borderSize, x+borderSize, y-borderSize, y+borderSize, owner);

        if(!type.equals("Wheat"))
            killUnit(x,y);

        this.currentMap[x][y].setCurrentBuilding(newBuilding);

        if(type.equals("City")) {
            roadManager.addCity(this.currentMap[x][y]);
        }else{
            roadManager.addRoad(this.currentMap[x][y]);
        }
        owner.addBuilding(newBuilding);

        if(type.equals("Lumber Mill") || type.equals("Mine")){
            calculateResourceYields(x, y, newBuilding, owner);
        }else if(type.equals("Farm")){
            placeWheat(x,y,owner);
        }

        System.out.println(type + " spawned at " + x + " " + y);
        return true;
    }

    private void constructResourceTile(String type, int x, int y){
        TileFactory factory = new TileFactory();
        Resource newResource = factory.buildResourceTile(type);
        this.currentMap[x][y].setCurrentResource(newResource);
        System.out.println(type + " spawned at " + x + " " + y);
    }
}