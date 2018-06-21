package game.gameModel;

import game.map.Constructable;
import game.map.Coordinates;
import game.map.RandomValues;
import game.map.buildings.Building;
import game.map.mapModel.Map;
import game.map.mapModel.Tile;

import java.util.ArrayList;
import java.util.Random;

class BorderExpansionBehaviour {
    private Map map;
    private Player currentPlayer;
    private RandomValues randomValues;
    private ArrayList<Tile> frontLineTiles;
    private Random random;

    BorderExpansionBehaviour(Map map) {
        this.map = map;
        randomValues = new RandomValues();
        frontLineTiles = new ArrayList<>();
        random = new Random();
    }

    void expandBorders(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        ArrayList<Building> buildings = currentPlayer.getBuildings();
        for (Building building : buildings) {
            if (building.getType() == Constructable.CITY) {
                expandBuildingBorders(building);
            }
        }
    }

    private void expandBuildingBorders(Building building) {
        findFrontLines(building);
        if (frontLineTiles.isEmpty())
            return;

        Tile tileToExpandFrom;
        Coordinates frontLineCoordinates;
        Coordinates natureTileCoordinates;
        do {
            int index = random.nextInt(frontLineTiles.size());
            tileToExpandFrom = frontLineTiles.get(index);

            frontLineCoordinates = tileToExpandFrom.getCoordinates();

            natureTileCoordinates = new Coordinates(frontLineCoordinates.x, frontLineCoordinates.y);
            natureTileCoordinates = randomValues.getRandomDirection(natureTileCoordinates);
        } while (!isNatureTile(natureTileCoordinates));

        Tile tileToClaim = map.getTile(natureTileCoordinates);
        tileToClaim.setOwner(currentPlayer);
        tileToClaim.setClaimedBy(building);
        building.claimTile(tileToClaim);
        System.out.println(currentPlayer.getName() + " Claimed " + frontLineCoordinates.x + " " + frontLineCoordinates.y);
    }

    private void findFrontLines(Building building){
        frontLineTiles.clear();
        for (Tile tile : building.getClaimedTiles()) {
            if (tileHasNatureAdjacent(tile)) {
                frontLineTiles.add(tile);
            }
        }
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