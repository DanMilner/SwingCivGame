package map;

import exceptions.TypeNotFound;
import map.resources.Resource;
import map.resources.ResourceTypes;

import java.util.Random;

class RandomValues {
    private int amount;
    private int intensity;
    private Random random;

    RandomValues() {
        random = new Random();
    }

    public void setAmount(int min, int max) {
        this.amount = random.nextInt(max - min + 1) + min;
    }

    public int getAmount() {
        return amount;
    }

    public void setIntensity(int min, int max) {
        this.intensity = random.nextInt(max - min + 1) + min;
    }

    public int getIntensity() {
        return intensity;
    }

    public Coordinates getRandomCoordinates(int mapsize) {
        return new Coordinates(random.nextInt(mapsize), random.nextInt(mapsize));
    }

    public Coordinates getRandomCoordinates(int min, int max) {
        int x = random.nextInt(max - min + 1) + min;
        int y = random.nextInt(max - min + 1) + min;
        return new Coordinates(x, y);
    }

    public int getRandomCoordinate(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public Coordinates getRandomDirection(Coordinates coordinates) {
        int direction = random.nextInt(5) + 1;

        if (direction == 1) { //North
            coordinates.setCoordinates(coordinates.x, coordinates.y + 1);
        } else if (direction == 2) { //South
            coordinates.setCoordinates(coordinates.x, coordinates.y - 1);
        } else if (direction == 3) { //East
            coordinates.setCoordinates(coordinates.x + 1, coordinates.y);
        } else if (direction == 4) { //West
            coordinates.setCoordinates(coordinates.x - 1, coordinates.y);
        }
        return coordinates;
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
                Coordinates coordinates = new Coordinates(x, y);
                map[x][y] = new Tile(coordinates, null);
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
        Coordinates coordinates;
        Resource currentTileResource;
        do {
            coordinates = randomValues.getRandomCoordinates(CITY_BORDER_SIZE, MAPSIZE - CITY_BORDER_SIZE);
            currentTileResource = map[coordinates.x][coordinates.y].getResource();
            if (!currentTileResource.isTraversable())
                continue;

            Boolean cityHasBeenPlaced = true;
            //check the surrounding tiles to not collide with existing cities
            for (int x = coordinates.x - CITY_BORDER_SIZE; x <= coordinates.x + CITY_BORDER_SIZE; x++) {
                for (int y = coordinates.y - CITY_BORDER_SIZE; y <= coordinates.y + CITY_BORDER_SIZE; y++) {
                    if (coordinatesOnMap(new Coordinates(x, y)) && map[x][y].hasOwner())
                        cityHasBeenPlaced = false; //if a tile is owned by another player then the cities are too close together
                }
            }
            if (cityHasBeenPlaced)
                return new Coordinates(coordinates.x, coordinates.y);
        } while (true);
    }

    private void generateTilesInBodies(Coordinates originCoordinates, ResourceTypes resourceType) {
        int numberOfTilesGenerated = 0;
        Coordinates coordinates = new Coordinates(originCoordinates.x, originCoordinates.y);
        do {
            coordinates = randomValues.getRandomDirection(coordinates);
            if (coordinatesOnMap(coordinates)) {
                if (map[coordinates.x][coordinates.y].getResource().getResourceType() != resourceType) {
                    constructResourceTile(resourceType, coordinates);
                    coordinates.setCoordinates(originCoordinates.x, originCoordinates.y);
                    numberOfTilesGenerated++;
                }
            } else {
                coordinates.setCoordinates(originCoordinates.x, originCoordinates.y);
            }
        } while (numberOfTilesGenerated < randomValues.getIntensity());
    }

    private void addSand() {
        int quarterMapSize = MAPSIZE / 4;
        randomValues.setIntensity(MAPSIZE * 2, MAPSIZE * 4);
        Coordinates coordinates = new Coordinates(randomValues.getRandomCoordinate(1, MAPSIZE),
                randomValues.getRandomCoordinate(quarterMapSize, MAPSIZE - quarterMapSize));
        generateTilesInBodies(coordinates, ResourceTypes.SAND);
    }

    private void addSnow() {
        int rowsOfSnow = MAPSIZE / 10;

        for (int y = 0; y < rowsOfSnow; y++) {
            for (int x = 0; x < MAPSIZE; x++) {
                constructResourceTile(ResourceTypes.SNOW, new Coordinates(x, y));
            }
        }

        for (int y = MAPSIZE; y >= MAPSIZE - rowsOfSnow; y--) {
            for (int x = 0; x < MAPSIZE; x++) {
                constructResourceTile(ResourceTypes.SNOW, new Coordinates(x, y));
            }
        }
    }

    private void addResource(ResourceTypes resourceType) {
        Coordinates randomCoordinates;
        for (int y = 0; y < randomValues.getAmount(); y++) {
            do {
                randomCoordinates = randomValues.getRandomCoordinates(MAPSIZE);
            }
            while (map[randomCoordinates.x][randomCoordinates.y].getResource().getResourceType() == ResourceTypes.WATER);

            for (int i = 0; i < randomValues.getIntensity(); i++) {
                randomCoordinates = randomValues.getRandomDirection(randomCoordinates);
                if (coordinatesOnMap(randomCoordinates))
                    constructResourceTile(resourceType, randomCoordinates);
            }
        }
    }

    private void addWater() {
        Coordinates coordinates;
        for (int i = 0; i < randomValues.getAmount(); i++) {
            coordinates = randomValues.getRandomCoordinates(MAPSIZE);
            generateTilesInBodies(coordinates, ResourceTypes.WATER);
        }
    }

    private boolean coordinatesOnMap(Coordinates coordinates) {
        return coordinates.x >= 0 && coordinates.x <= MAPSIZE && coordinates.y >= 0 && coordinates.y <= MAPSIZE;
    }

    private void constructResourceTile(ResourceTypes resourceType, Coordinates coordinates) {
        try {
            Resource newResource = TileFactory.buildResourceTile(resourceType);
            map[coordinates.x][coordinates.y].setResource(newResource);
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
        }
    }
}
