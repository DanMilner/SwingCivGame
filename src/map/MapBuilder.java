package map;

import map.resources.Resource;

import java.util.concurrent.ThreadLocalRandom;

class randomDirectionResult{
    private final int xCoord;
    private final int yCoord;

    randomDirectionResult(int xCoord, int yCoord){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }
}

class MapBuilder {
    private Tile[][] map;
    private final int MAPSIZE;
    MapBuilder(Tile[][] currentMap, int size){
        map = currentMap;
        MAPSIZE = size;
    }

    public void setUpMap(){
        for (int x = 0; x <= MAPSIZE; x++) {
            for (int y = 0; y <= MAPSIZE; y++) {
                map[x][y] = new Tile(x,y,null);
                constructResourceTile("Grass", x, y);
            }
        }
    }

    public void setUpTerrain(){
        //add trees
        addTrees(ThreadLocalRandom.current().nextInt(MAPSIZE * 12, MAPSIZE * 15));

        addSnow();

        addSand();

        //add Water
        addWater(ThreadLocalRandom.current().nextInt(MAPSIZE / 10, MAPSIZE / 5),
                 ThreadLocalRandom.current().nextInt(MAPSIZE / 2, MAPSIZE * 2));

        //add Mountains
        addResource(ThreadLocalRandom.current().nextInt(MAPSIZE / 11, MAPSIZE / 7),
                    ThreadLocalRandom.current().nextInt(MAPSIZE / 4, MAPSIZE),
                    "Mountain");

        //add Iron
        addResource(ThreadLocalRandom.current().nextInt(MAPSIZE / 4, MAPSIZE / 3),
                    ThreadLocalRandom.current().nextInt(3, MAPSIZE / 10),
                    "Iron");

        //add Gold
        addResource(ThreadLocalRandom.current().nextInt(MAPSIZE / 10, MAPSIZE / 7),
                    ThreadLocalRandom.current().nextInt(2, MAPSIZE / 10),
                    "Gold");

        //add Copper
        addResource(ThreadLocalRandom.current().nextInt(MAPSIZE / 7, MAPSIZE / 6),
                    ThreadLocalRandom.current().nextInt(2, MAPSIZE / 10),
                    "Copper");

        //add Coal
        addResource(ThreadLocalRandom.current().nextInt(MAPSIZE / 5, MAPSIZE / 4),
                    ThreadLocalRandom.current().nextInt(2, MAPSIZE / 10),
                    "Coal");
    }

    private void generateTilesInBodies(int xCoord, int yCoord, int intensity, String tileType){
        int originX = xCoord;
        int originY = yCoord;
        int numberOfTilesGenerated = 0;
        do {
            randomDirectionResult directionResult = randomDirection(xCoord,yCoord);
            xCoord = directionResult.getxCoord();
            yCoord = directionResult.getyCoord();
            if(coordinatesOnMap(xCoord, yCoord)){
                if (!map[xCoord][yCoord].getResource().getType().equals(tileType)) {
                    constructResourceTile(tileType, xCoord, yCoord);
                    xCoord = originX;
                    yCoord = originY;
                    numberOfTilesGenerated++;
                }
            }else{
                xCoord = originX;
                yCoord = originY;
            }
        } while (numberOfTilesGenerated < intensity);
    }

    private void addSand(){
        int quarterMapSize = MAPSIZE/4;

        int intensity = ThreadLocalRandom.current().nextInt(MAPSIZE*2, MAPSIZE * 4);

        int xCoord = ThreadLocalRandom.current().nextInt(1, MAPSIZE);
        int yCoord = ThreadLocalRandom.current().nextInt(quarterMapSize, MAPSIZE-quarterMapSize);
        generateTilesInBodies(xCoord, yCoord, intensity, "Sand");
    }

    private void addSnow(){
        int rowsOfSnow = MAPSIZE/10;

        for (int y = 0; y < rowsOfSnow; y++){
            for (int x = 0; x < MAPSIZE; x++){
                constructResourceTile("Snow", x,y);
            }
        }

        for (int y = MAPSIZE; y >= MAPSIZE - rowsOfSnow; y--){
            for (int x = 0; x < MAPSIZE; x++){
                constructResourceTile("Snow", x,y);
            }
        }
    }

    private void addTrees(int amount){
        int xCoord;
        int yCoord;
        do{
            xCoord = ThreadLocalRandom.current().nextInt(0, MAPSIZE);
            yCoord = ThreadLocalRandom.current().nextInt(0, MAPSIZE);

            constructResourceTile("Forest", xCoord, yCoord);
            amount--;
        }while (amount > 0);
    }

    private void addResource(int amount, int intensity, String type) {
        int xCoord;
        int yCoord;

        for (int y = 0; y < amount; y++) {
            do {
                xCoord = ThreadLocalRandom.current().nextInt(0, MAPSIZE);
                yCoord = ThreadLocalRandom.current().nextInt(0, MAPSIZE);
            } while (map[xCoord][yCoord].getResource().getType().equals("Water"));

            for (int i = 0; i < intensity; i++) {
                randomDirectionResult directionResult = randomDirection(xCoord,yCoord);
                xCoord = directionResult.getxCoord();
                yCoord = directionResult.getyCoord();
                if(coordinatesOnMap(xCoord,yCoord))
                    constructResourceTile(type, xCoord, yCoord);
            }
        }
    }

    private void addWater(int numberOfWaterBodies, int intensity) {
        int xCoord;
        int yCoord;

        for (int i = 0; i < numberOfWaterBodies; i++) {
            xCoord = ThreadLocalRandom.current().nextInt(1, MAPSIZE);
            yCoord = ThreadLocalRandom.current().nextInt(1, MAPSIZE);

            generateTilesInBodies(xCoord, yCoord, intensity, "Water");
        }
    }

    private randomDirectionResult randomDirection(int xCoord, int yCoord){
        int direction = ThreadLocalRandom.current().nextInt(1, 5);

        if (direction == 1) { //North
            yCoord++;
        } else if (direction == 2) { //South
            yCoord--;
        } else if (direction == 3) { //East
            xCoord++;
        } else if (direction == 4) { //West
            xCoord--;
        }
        return new randomDirectionResult(xCoord,yCoord);
    }

    private boolean coordinatesOnMap(int x, int y){
        return x >= 0 && x <= MAPSIZE && y >= 0 && y <= MAPSIZE;
    }

    private void constructResourceTile(String type, int x, int y){
        Resource newResource = TileFactory.buildResourceTile(type);
        map[x][y].setResource(newResource);
        System.out.println(type + " spawned at " + x + " " + y);
    }
}
