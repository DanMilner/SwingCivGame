package map;

import map.resources.Resource;

import java.util.concurrent.ThreadLocalRandom;

class RandomDirectionResult {
    private final int xCoord;
    private final int yCoord;

    RandomDirectionResult(int xCoord, int yCoord){
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

class RandomValues {
    private int amount;
    private int intensity;


    public void setAmount(int min, int max) {
        this.amount = ThreadLocalRandom.current().nextInt(min, max);
    }

    public int getAmount() {
        return amount;
    }

    public void setIntensity(int min, int max) {
        this.intensity = ThreadLocalRandom.current().nextInt(min, max);
    }

    public int getIntensity() {
        return intensity;
    }

    public int getRandomCoordinate(int mapsize){
        return ThreadLocalRandom.current().nextInt(0, mapsize);
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
        RandomValues randomValues = new RandomValues();
        //add trees
        randomValues.setAmount(MAPSIZE * 2, MAPSIZE * 3);
        randomValues.setIntensity(3, 10);
        addResource(randomValues, "Forest");

        addSnow();

        addSand();

        //add Water
        randomValues.setAmount(MAPSIZE / 10, MAPSIZE / 5);
        randomValues.setIntensity(MAPSIZE / 2, MAPSIZE * 2);
        addWater(randomValues);

        //add Mountains
        randomValues.setAmount(MAPSIZE / 10, MAPSIZE / 6);
        randomValues.setIntensity(MAPSIZE, MAPSIZE * 2);
        addResource(randomValues,"Mountain");

        //add Iron
        randomValues.setAmount(MAPSIZE / 3, MAPSIZE / 2);
        randomValues.setIntensity(5, MAPSIZE / 6);
        addResource(randomValues,"Iron");

        //add Gold
        randomValues.setAmount(MAPSIZE / 8, MAPSIZE / 5);
        randomValues.setIntensity(5, MAPSIZE / 6);
        addResource(randomValues,"Gold");

        //add Copper
        randomValues.setAmount(MAPSIZE / 7, MAPSIZE / 5);
        randomValues.setIntensity(5, MAPSIZE / 6);
        addResource(randomValues,"Copper");

        //add Coal
        randomValues.setAmount(MAPSIZE / 3, MAPSIZE / 2);
        randomValues.setIntensity(5, MAPSIZE / 6);
        addResource(randomValues,"Coal");

        //add Diamonds
        randomValues.setAmount(MAPSIZE / 6, MAPSIZE / 4);
        randomValues.setIntensity(2, 6);
        addResource(randomValues,"Diamonds");
    }

    private void generateTilesInBodies(int xCoord, int yCoord, int intensity, String tileType){
        int originX = xCoord;
        int originY = yCoord;
        int numberOfTilesGenerated = 0;
        do {
            RandomDirectionResult directionResult = randomDirection(xCoord,yCoord);
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

    private void addResource(RandomValues randomValues, String type) {
        int amount = randomValues.getAmount();
        int intensity = randomValues.getIntensity();
        int xCoord;
        int yCoord;

        for (int y = 0; y < amount; y++) {
            do {
                xCoord = randomValues.getRandomCoordinate(MAPSIZE);
                yCoord = randomValues.getRandomCoordinate(MAPSIZE);
            } while (map[xCoord][yCoord].getResource().getType().equals("Water"));

            for (int i = 0; i < intensity; i++) {
                RandomDirectionResult directionResult = randomDirection(xCoord,yCoord);
                xCoord = directionResult.getxCoord();
                yCoord = directionResult.getyCoord();
                if(coordinatesOnMap(xCoord,yCoord))
                    constructResourceTile(type, xCoord, yCoord);
            }
        }
    }

    private void addWater(RandomValues amountAndIntensity) {
        int numberOfWaterBodies = amountAndIntensity.getAmount();
        int intensity = amountAndIntensity.getIntensity();
        int xCoord;
        int yCoord;

        for (int i = 0; i < numberOfWaterBodies; i++) {
            xCoord = ThreadLocalRandom.current().nextInt(0, MAPSIZE);
            yCoord = ThreadLocalRandom.current().nextInt(0, MAPSIZE);

            generateTilesInBodies(xCoord, yCoord, intensity, "Water");
        }
    }

    private RandomDirectionResult randomDirection(int xCoord, int yCoord){
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
        return new RandomDirectionResult(xCoord,yCoord);
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
