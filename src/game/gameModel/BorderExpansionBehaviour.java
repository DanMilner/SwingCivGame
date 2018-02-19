package game.gameModel;

import game.map.*;
import game.map.buildings.Building;
import game.map.mapModel.Map;
import game.map.mapModel.Tile;

import java.util.ArrayList;
import java.util.Random;

public class BorderExpansionBehaviour {
    private Map map;
    private Player currentPlayer;

    BorderExpansionBehaviour(Map map) {
        this.map = map;
    }

    public void expandBorders(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        ArrayList<Building> buildings = currentPlayer.getBuildings();
        for (Building building : buildings) {
            if (building.getType() == Constructable.CITY) {
                expandBuildingBorders(building);
            }
        }
    }

    private void expandBuildingBorders(Building building) {
        RandomValues randomValues = new RandomValues();
        ArrayList<Tile> frontLineTiles = new ArrayList<>();
        for (Tile tile : building.getClaimedTiles()) {
            if (tileHasNatureAdjacent(tile)) {
                frontLineTiles.add(tile);
            }
        }

        Random random = new Random();
        if (frontLineTiles.isEmpty())
            return;
        int index = random.nextInt(frontLineTiles.size());
        Tile tileToExpandFrom = frontLineTiles.get(index);

        Coordinates coordinates = tileToExpandFrom.getCoordinates();

        Coordinates tempCoordinates = new Coordinates(coordinates.x, coordinates.y);
        do {
            randomValues.getRandomDirection(tempCoordinates);
        } while (!isNatureTile(tempCoordinates));

        tileToExpandFrom = map.getTile(tempCoordinates);
        tileToExpandFrom.setOwner(currentPlayer);
        building.claimTile(tileToExpandFrom);
        System.out.println("Claimed " + coordinates.x + " " + coordinates.y);
    }

    private boolean tileHasNatureAdjacent(Tile tile) {
        Coordinates coord = tile.getCoordinates();
        Coordinates tempCoords = new Coordinates(0, 0);

        tempCoords.setCoordinates(coord.x, coord.y - 1);
        if (isNatureTile(tempCoords))
            return true;

        tempCoords.setCoordinates(coord.x, coord.y + 1);
        if (isNatureTile(tempCoords))
            return true;

        tempCoords.setCoordinates(coord.x - 1, coord.y);
        if (isNatureTile(tempCoords))
            return true;

        tempCoords.setCoordinates(coord.x + 1, coord.y);
        return isNatureTile(tempCoords);
    }

    private boolean isNatureTile(Coordinates coordinates) {
        return map.coordinatesOnMap(coordinates) && map.getTile(coordinates).getOwner() == null;
    }
}