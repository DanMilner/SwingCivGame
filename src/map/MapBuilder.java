package map;

import exceptions.TypeNotFound;
import main.MapData;
import map.resources.Resource;
import map.resources.ResourceTypes;

import java.util.Random;

class RandomValues {
    private int intensity;
    private Random random;

    RandomValues() {
        random = new Random();
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
    private MapData mapData;

    MapBuilder(Tile[][] currentMap, MapData mapData) {
        this.map = currentMap;
        this.MAPSIZE = mapData.getMapsize();
        this.mapData = mapData;
        this.randomValues = new RandomValues();
    }

    public void setUpMap() {
        for (int x = 0; x <= MAPSIZE; x++) {
            for (int y = 0; y <= MAPSIZE; y++) {
                Coordinates coordinates = new Coordinates(x, y);
                map[x][y] = new Tile(coordinates, null);
                constructResourceTile(ResourceTypes.WATER, coordinates);
            }
        }
    }

    public void setUpTerrain() {
        int totalNumberOfTiles = (MAPSIZE + 1) * (MAPSIZE + 1);

        Double waterAmount = 0.30;
        waterAmount *= mapData.getWater() / 100;
        Double landAmount = totalNumberOfTiles * (1 - waterAmount);
        int numberOfLandTiles = Math.round(landAmount.floatValue());

        addLand(numberOfLandTiles);

        if (mapData.isArcticRegions())
            addSnow();

        Double sandAmount = .10 * mapData.getDeserts() / 100;
        Double treesAmount = .20 * mapData.getTrees() / 100;
        Double mountainsAmount = .10 * mapData.getMountains() / 100;
        Double resourcesAmount = .15 * mapData.getResources() / 100;

        sandAmount = (numberOfLandTiles * .45) * sandAmount;
        treesAmount = (numberOfLandTiles * .45) * treesAmount;
        mountainsAmount = (numberOfLandTiles * .45) * mountainsAmount;
        resourcesAmount = (numberOfLandTiles * .45) * resourcesAmount;

        if (mapData.getDeserts() != 0)
            addSand(Math.round(sandAmount.floatValue()));

        addResource(ResourceTypes.WOOD, Math.round(treesAmount.floatValue()));

        addResource(ResourceTypes.STONE, Math.round(mountainsAmount.floatValue()));

        addResource(ResourceTypes.IRON, Math.round(resourcesAmount.floatValue() * 0.40f));
        addResource(ResourceTypes.GOLD, Math.round(resourcesAmount.floatValue() * 0.15f));
        addResource(ResourceTypes.COPPER, Math.round(resourcesAmount.floatValue() * 0.25f));
        addResource(ResourceTypes.COAL, Math.round(resourcesAmount.floatValue() * 0.15f));
        addResource(ResourceTypes.DIAMONDS, Math.round(resourcesAmount.floatValue() * 0.05f));
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

    private void generateTilesInBodies(Coordinates originCoordinates, ResourceTypes resourceType, int totalTiles) {
        int numberOfTilesGenerated = 0;
        Coordinates coordinates = new Coordinates(originCoordinates.x, originCoordinates.y);
        do {
            coordinates = randomValues.getRandomDirection(coordinates);
            if (coordinatesOnMap(coordinates)) {
                if (map[coordinates.x][coordinates.y].getResource().getResourceType() != resourceType) {
                    constructResourceTile(resourceType, coordinates);
                    coordinates.setCoordinates(originCoordinates.x, originCoordinates.y);
                    numberOfTilesGenerated++;
                    totalTiles--;
                }
            } else {
                coordinates.setCoordinates(originCoordinates.x, originCoordinates.y);
            }
        } while (numberOfTilesGenerated < randomValues.getIntensity() && totalTiles > 0);
    }

    private void addSand(int totalLandTiles) {
        Coordinates randomCoordinates;
        int quarterMapSize = MAPSIZE / 4;
        do {
            randomCoordinates = new Coordinates(randomValues.getRandomCoordinate(1, MAPSIZE),
                    randomValues.getRandomCoordinate(quarterMapSize, MAPSIZE - quarterMapSize));
        }
        while (map[randomCoordinates.x][randomCoordinates.y].getResource().getResourceType() != ResourceTypes.GRASS);

        generateTilesInBodies(randomCoordinates, ResourceTypes.SAND, totalLandTiles);
    }

    private void addSnow() {
        int rowsOfSnow = MAPSIZE / 20;

        for (int y = 0; y <= rowsOfSnow; y++) {
            for (int x = 0; x <= MAPSIZE; x++) {
                constructResourceTile(ResourceTypes.SNOW, new Coordinates(x, y));
            }
        }

        for (int y = MAPSIZE; y >= MAPSIZE - rowsOfSnow; y--) {
            for (int x = 0; x <= MAPSIZE; x++) {
                constructResourceTile(ResourceTypes.SNOW, new Coordinates(x, y));
            }
        }
    }

    private void addResource(ResourceTypes resourceType, int amountOfTiles) {
        Coordinates randomCoordinates;
        while (amountOfTiles > 0) {
            do {
                randomCoordinates = randomValues.getRandomCoordinates(MAPSIZE);
            }
            while (map[randomCoordinates.x][randomCoordinates.y].getResource().getResourceType() == ResourceTypes.WATER);
            randomValues.setIntensity(1, 20);
            amountOfTiles -= generateTilesInDirections(randomCoordinates, resourceType, amountOfTiles);
        }
    }

    private int generateTilesInDirections(Coordinates randomCoordinates, ResourceTypes resourceType, int amountOfTiles) {
        int numberOfTilesGenerated = 0;
        int loopPrevention = 0;
        do {
            randomCoordinates = randomValues.getRandomDirection(randomCoordinates);
            if (coordinatesOnMap(randomCoordinates)) {
                if (map[randomCoordinates.x][randomCoordinates.y].getResource().getResourceType() == ResourceTypes.GRASS) {
                    constructResourceTile(resourceType, randomCoordinates);
                    amountOfTiles--;
                    numberOfTilesGenerated++;
                } else {
                    loopPrevention++;
                }
                if (loopPrevention == 10)
                    return numberOfTilesGenerated;
            }
        } while (numberOfTilesGenerated < randomValues.getIntensity() && amountOfTiles > 0);
        return numberOfTilesGenerated;
//        System.out.println(numberOfTilesGenerated + " " + randomValues.getIntensity() + " " + amountOfTiles);
    }

    private void addLand(int totalLandTiles) {
        Coordinates coordinates;
        while (totalLandTiles > 0) {
            randomValues.setIntensity(MAPSIZE * 5, MAPSIZE * 10);
            coordinates = randomValues.getRandomCoordinates(MAPSIZE);
            generateTilesInBodies(coordinates, ResourceTypes.GRASS, totalLandTiles);
            totalLandTiles -= randomValues.getIntensity();
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
