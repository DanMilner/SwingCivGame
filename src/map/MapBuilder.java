package map;

import exceptions.TypeNotFound;
import map.resources.Resource;
import map.resources.ResourceTypes;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

    public Coordinates getRandomCoordinates(int mapsize) {
        Random random = new Random();
        return new Coordinates(random.nextInt(mapsize), random.nextInt(mapsize));
    }
}

class MapBuilder {
    private Tile[][] map;
    private final int MAPSIZE;
    private RandomValues randomValues;

    MapBuilder(Tile[][] currentMap, int size) {
        map = currentMap;
        MAPSIZE = size;
        randomValues = new RandomValues();
    }

    public void setUpMap() {
        for (int x = 0; x <= MAPSIZE; x++) {
            for (int y = 0; y <= MAPSIZE; y++) {
                Coordinates coordinates = new Coordinates(x , y);
                map[x][y] = new Tile(x, y, null);
                constructResourceTile(ResourceTypes.GRASS, coordinates);
            }
        }
    }

    public void setUpTerrain() {
        //add trees
        randomValues.setAmount(MAPSIZE * 2, MAPSIZE * 3);
        randomValues.setIntensity(3, 10);
        addResource(ResourceTypes.WOOD);

        addSnow();

        addSand();

        //add Water
        randomValues.setAmount(MAPSIZE / 10, MAPSIZE / 5);
        randomValues.setIntensity(MAPSIZE / 2, MAPSIZE * 2);
        addWater();

        //add Mountains
        randomValues.setAmount(MAPSIZE / 10, MAPSIZE / 6);
        randomValues.setIntensity(MAPSIZE, MAPSIZE * 2);
        addResource(ResourceTypes.STONE);

        //add Iron
        randomValues.setAmount(MAPSIZE / 3, MAPSIZE / 2);
        randomValues.setIntensity(5, MAPSIZE / 6);
        addResource(ResourceTypes.IRON);

        //add Gold
        randomValues.setAmount(MAPSIZE / 8, MAPSIZE / 5);
        randomValues.setIntensity(5, MAPSIZE / 6);
        addResource(ResourceTypes.GOLD);

        //add Copper
        randomValues.setAmount(MAPSIZE / 7, MAPSIZE / 5);
        randomValues.setIntensity(5, MAPSIZE / 6);
        addResource(ResourceTypes.COPPER);

        //add Coal
        randomValues.setAmount(MAPSIZE / 3, MAPSIZE / 2);
        randomValues.setIntensity(5, MAPSIZE / 6);
        addResource(ResourceTypes.COAL);

        //add Diamonds
        randomValues.setAmount(MAPSIZE / 6, MAPSIZE / 4);
        randomValues.setIntensity(2, 6);
        addResource(ResourceTypes.DIAMONDS);
    }

    public Coordinates generateNewCityCoordinates() {
        final int CITY_BORDER_SIZE = 3;
        int xCoord;
        int yCoord;
        Resource currentTileResource;
        do {
            xCoord = ThreadLocalRandom.current().nextInt(CITY_BORDER_SIZE, MAPSIZE - CITY_BORDER_SIZE);
            yCoord = ThreadLocalRandom.current().nextInt(CITY_BORDER_SIZE, MAPSIZE - CITY_BORDER_SIZE);
            currentTileResource = map[xCoord][yCoord].getResource();
            if (currentTileResource.isTraversable()) {
                Boolean placeCity = true;
                //check the surrounding tiles to not collide with existing cities
                for (int x = xCoord - CITY_BORDER_SIZE; x <= xCoord + CITY_BORDER_SIZE; x++) {
                    for (int y = yCoord - CITY_BORDER_SIZE; y <= yCoord + CITY_BORDER_SIZE; y++) {
                        if (coordinatesOnMap(new Coordinates(x, y))) {
                            if (map[x][y].hasOwner()) {
                                placeCity = false; //if a tile is owned by another player then the cities are too close together
                            }
                        }
                    }
                }
                if (placeCity) {
                    return new Coordinates(xCoord, yCoord);
                }
            }
        } while (true);
    }

    private void generateTilesInBodies(Coordinates originCoordinates, int intensity, ResourceTypes resourceType) {
        int numberOfTilesGenerated = 0;
        Coordinates coordinates = new Coordinates(originCoordinates.x, originCoordinates.y);
        do {
            coordinates = randomDirection(coordinates.x, coordinates.y);
            if (coordinatesOnMap(coordinates)) {
                if (map[coordinates.x][coordinates.y].getResource().getResourceType() != resourceType) {
                    constructResourceTile(resourceType, coordinates);
                    coordinates.setCoordinates(originCoordinates.x, originCoordinates.y);
                    numberOfTilesGenerated++;
                }
            } else {
                coordinates.setCoordinates(originCoordinates.x, originCoordinates.y);
            }
        } while (numberOfTilesGenerated < intensity);
    }

    private void addSand() {
        int quarterMapSize = MAPSIZE / 4;

        int intensity = ThreadLocalRandom.current().nextInt(MAPSIZE * 2, MAPSIZE * 4);

        int xCoord = ThreadLocalRandom.current().nextInt(1, MAPSIZE);
        int yCoord = ThreadLocalRandom.current().nextInt(quarterMapSize, MAPSIZE - quarterMapSize);
        generateTilesInBodies(new Coordinates(xCoord, yCoord), intensity, ResourceTypes.SAND);
    }

    private void addSnow() {
        int rowsOfSnow = MAPSIZE / 10;

        for (int y = 0; y < rowsOfSnow; y++) {
            for (int x = 0; x < MAPSIZE; x++) {
                constructResourceTile(ResourceTypes.SNOW, new Coordinates(x,y));
            }
        }

        for (int y = MAPSIZE; y >= MAPSIZE - rowsOfSnow; y--) {
            for (int x = 0; x < MAPSIZE; x++) {
                constructResourceTile(ResourceTypes.SNOW, new Coordinates(x,y));
            }
        }
    }

    private void addResource(ResourceTypes resourceType) {
        int amount = randomValues.getAmount();
        int intensity = randomValues.getIntensity();
        Coordinates randomCoordinates;
        for (int y = 0; y < amount; y++) {
            do{
                randomCoordinates = randomValues.getRandomCoordinates(MAPSIZE);
            }while(map[randomCoordinates.x][randomCoordinates.y].getResource().getResourceType() == ResourceTypes.WATER);

            for (int i = 0; i < intensity; i++) {
                randomCoordinates = randomDirection(randomCoordinates.x, randomCoordinates.y);
                if (coordinatesOnMap(randomCoordinates))
                    constructResourceTile(resourceType, randomCoordinates);
            }
        }
    }

    private void addWater() {
        int numberOfWaterBodies = randomValues.getAmount();
        int intensity = randomValues.getIntensity();
        Coordinates coordinates;

        for (int i = 0; i < numberOfWaterBodies; i++) {
            coordinates = randomValues.getRandomCoordinates(MAPSIZE);
            generateTilesInBodies(coordinates, intensity, ResourceTypes.WATER);
        }
    }

    private Coordinates randomDirection(int xCoord, int yCoord) {
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
        return new Coordinates(xCoord, yCoord);
    }

    private boolean coordinatesOnMap(Coordinates coordinates) {
        return coordinates.x >= 0 && coordinates.x <= MAPSIZE && coordinates.y >= 0 && coordinates.y <= MAPSIZE;
    }

    private void constructResourceTile(ResourceTypes resourceType,Coordinates coordinates) {
        try {
            Resource newResource = TileFactory.buildResourceTile(resourceType);
            map[coordinates.x][coordinates.y].setResource(newResource);
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
        }
    }
}
