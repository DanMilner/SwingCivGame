package game.map.mapModel;

import exceptions.TypeNotFound;
import game.MapData;
import game.map.Coordinates;
import game.map.RandomValues;
import game.map.resources.Resource;
import game.map.resources.ResourceTypes;

import java.util.ArrayList;

class MapBuilder {
    private Tile[][] map;
    private final int MAPSIZE;
    private RandomValues randomValues;
    private MapData mapData;
    private ArrayList<Tile> grassTiles;

    MapBuilder(Tile[][] currentMap, MapData mapData) {
        this.map = currentMap;
        this.MAPSIZE = mapData.getMapsize();
        this.mapData = mapData;
        this.randomValues = new RandomValues();
    }

    public void setUpMap() {
        for (int x = 0; x < MAPSIZE; x++) {
            for (int y = 0; y < MAPSIZE; y++) {
                Coordinates coordinates = new Coordinates(x, y);
                map[x][y] = new Tile(coordinates, null);
                constructResourceTile(ResourceTypes.WATER, coordinates);
            }
        }
    }

    public void setUpTerrain() {
        int totalNumberOfTiles = (MAPSIZE) * (MAPSIZE);

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

        addResource(ResourceTypes.STONE, Math.round(mountainsAmount.floatValue()), 5, 15);
        addResource(ResourceTypes.IRON, Math.round(resourcesAmount.floatValue() * 0.40f), 3, 6);
        addResource(ResourceTypes.GOLD, Math.round(resourcesAmount.floatValue() * 0.15f), 2, 4);
        addResource(ResourceTypes.COPPER, Math.round(resourcesAmount.floatValue() * 0.25f), 3, 6);
        addResource(ResourceTypes.COAL, Math.round(resourcesAmount.floatValue() * 0.15f), 5, 10);
        addResource(ResourceTypes.DIAMONDS, Math.round(resourcesAmount.floatValue() * 0.05f), 1, 3);
        addResource(ResourceTypes.WOOD, Math.round(treesAmount.floatValue()), 1, 5);
    }

    private void addLand(int totalLandTiles) {
        Coordinates coordinates;
        grassTiles = new ArrayList<>();
        while (totalLandTiles > 0) {
            randomValues.setIntensity(MAPSIZE * 2, MAPSIZE * 10);
            coordinates = randomValues.getRandomCoordinates(MAPSIZE);
            generateTilesInBodies(coordinates, ResourceTypes.GRASS, totalLandTiles);
            totalLandTiles -= randomValues.getIntensity();
        }
    }

    private void addSand(int totalLandTiles) {
        int quarterMapSize = MAPSIZE / 4;
        Coordinates originCoordinates;
        do {
            originCoordinates = getRandomGrassTileCoordinates();
        }
        while (originCoordinates.y <= quarterMapSize || originCoordinates.y >= (MAPSIZE - quarterMapSize));
        grassTiles.remove(map[originCoordinates.x][originCoordinates.y]);

        generateTilesInBodies(originCoordinates, ResourceTypes.SAND, totalLandTiles);
    }

    private void addSnow() {
        int rowsOfSnow = MAPSIZE / 20;

        for (int y = 0; y < rowsOfSnow; y++) {
            placeRowOfSnowOrIce(y);
        }
        for (int y = MAPSIZE - 1; y >= MAPSIZE - rowsOfSnow; y--) {
            placeRowOfSnowOrIce(y);
        }
    }

    private void placeRowOfSnowOrIce(int y){
        for (int x = 0; x < MAPSIZE; x++) {
            if(grassTiles.contains(map[x][y]))
                grassTiles.remove(map[x][y]);
            if(map[x][y].getResource().getResourceType() == ResourceTypes.WATER){
                constructResourceTile(ResourceTypes.ICE, new Coordinates(x, y));
            }else{
                constructResourceTile(ResourceTypes.SNOW, new Coordinates(x, y));
            }
        }
    }

    private void addResource(ResourceTypes resourceType, int amountOfTiles, int min, int max) {
        Coordinates originCoordinates;
        while (amountOfTiles > 0) {
            originCoordinates = getRandomGrassTileCoordinates();
            randomValues.setIntensity(min, max);
            amountOfTiles -= generateTilesInDirections(originCoordinates, resourceType, amountOfTiles);
        }
    }

    private void generateTilesInBodies(Coordinates originCoordinates, ResourceTypes resourceType, int totalTiles) {
        int numberOfTilesGenerated = 0;
        Coordinates coordinates = new Coordinates(originCoordinates.x, originCoordinates.y);
        do {
            coordinates = randomValues.getRandomDirection(coordinates);
            if (!coordinatesOnMap(coordinates)) {
                coordinates.setCoordinates(originCoordinates.x, originCoordinates.y);
                continue;
            }

            if (map[coordinates.x][coordinates.y].getResource().getResourceType() != resourceType) {
                constructResourceTile(resourceType, coordinates);
                coordinates.setCoordinates(originCoordinates.x, originCoordinates.y);
                numberOfTilesGenerated++;
                totalTiles--;
            }
        } while (numberOfTilesGenerated < randomValues.getIntensity() && totalTiles > 0);
    }

    private int generateTilesInDirections(Coordinates coordinates, ResourceTypes resourceType, int totalToBeGenerated) {
        int numberOfTilesGenerated = 0;
        int infiniteLoopPrevention = 0;
        ResourceTypes tempType;

        Coordinates previousCoordinates = new Coordinates(coordinates.x,coordinates.y);
        do {
            coordinates = randomValues.getRandomDirection(coordinates);
            if (!coordinatesOnMap(coordinates)) {
                coordinates.setCoordinates(previousCoordinates.x, previousCoordinates.y);
                continue;
            }

            tempType = map[coordinates.x][coordinates.y].getResource().getResourceType();
            if (tempType == ResourceTypes.GRASS) {
                constructResourceTile(resourceType, coordinates);
                grassTiles.remove(map[coordinates.x][coordinates.y]);
                totalToBeGenerated--;
                numberOfTilesGenerated++;
                infiniteLoopPrevention = 0;
                previousCoordinates.setCoordinates(coordinates.x, coordinates.y);
            } else if (tempType != resourceType) {
                coordinates.setCoordinates(previousCoordinates.x, previousCoordinates.y);
            }
            infiniteLoopPrevention++;

            if (infiniteLoopPrevention == 20) {
                return numberOfTilesGenerated;
            }

        } while (numberOfTilesGenerated < randomValues.getIntensity() && totalToBeGenerated > 0);
        return numberOfTilesGenerated;
    }

    private boolean coordinatesOnMap(Coordinates coordinates) {
        return coordinates.x >= 0 && coordinates.x < MAPSIZE && coordinates.y >= 0 && coordinates.y < MAPSIZE;
    }

    private void constructResourceTile(ResourceTypes resourceType, Coordinates coordinates) {
        try {
            Resource newResource = TileFactory.buildResourceTile(resourceType);
            map[coordinates.x][coordinates.y].setResource(newResource);
            if (resourceType == ResourceTypes.GRASS)
                grassTiles.add(map[coordinates.x][coordinates.y]);
        } catch (TypeNotFound typeNotFound) {
            typeNotFound.printStackTrace();
        }
    }

    private Coordinates getRandomGrassTileCoordinates() {
        int index = randomValues.randomInt(grassTiles.size());
        Coordinates coordinates = grassTiles.get(index).getCoordinates();
        grassTiles.remove(index);
        return new Coordinates(coordinates.x, coordinates.y);
    }

    public Coordinates generateNewCityCoordinates() {
        final int CITY_BORDER_SIZE = 1;
        Coordinates coordinates;
        do {
            coordinates = getRandomGrassTileCoordinates();
            if (coordinates.y <= CITY_BORDER_SIZE || coordinates.y >= MAPSIZE - CITY_BORDER_SIZE)
                continue;
            if (coordinates.x <= CITY_BORDER_SIZE || coordinates.x >= MAPSIZE - CITY_BORDER_SIZE)
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
                return coordinates;
        } while (true);
    }
}