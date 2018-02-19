package game.map.mapModel;

import game.gameModel.Player;
import game.map.Coordinates;
import game.map.buildings.Building;

public class TileOwnerHandler {

    private Map map;

    public TileOwnerHandler(Map map) {
        this.map = map;
    }

    public void setTileOwner(Building newBuilding, Coordinates coordinates, Player owner) {
        int borderSize = newBuilding.getBorderSize();
        int startX = coordinates.x - borderSize;
        int startY = coordinates.y - borderSize;
        int endX = coordinates.x + borderSize;
        int endY = coordinates.y + borderSize;

        Tile tempTile = map.getTile(coordinates);
        claimTile(newBuilding, tempTile, owner);

        Coordinates tempCoordinates = new Coordinates(0, 0);
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                tempCoordinates.setCoordinates(x, y);
                if (map.coordinatesOnMap(tempCoordinates)) {
                    tempTile = map.getTile(tempCoordinates);
                    if (!tempTile.hasOwner())
                        claimTile(newBuilding, tempTile, owner);
                }
            }
        }
    }

    private void claimTile(Building newBuilding, Tile tile, Player owner){
        if(tile.hasOwner())
            tile.getClaimedBy().releaseClaimedTile(tile);
        tile.setOwner(owner);
        newBuilding.claimTile(tile);
        tile.setClaimedBy(newBuilding);
    }

    public boolean borderRequired(Coordinates coordinates, Coordinates adjacentCoordinates) {
        return !map.coordinatesOnMap(adjacentCoordinates) || map.getTile(coordinates).getOwner() != map.getTile(adjacentCoordinates).getOwner();
    }
}
