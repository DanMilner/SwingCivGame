package map;

import javax.swing.*;
import java.util.ArrayList;

class RoadManager {
    private ArrayList<Tile> roads = new ArrayList<>();
    private ArrayList<Tile> cities = new ArrayList<>();

    private void addRoad(Tile road) {
        roads.add(road);
        updateRoadConnections();
    }

    private void addCity(Tile city) {
        cities.add(city);
        updateRoadConnections();
    }

    private void updateRoadConnections() {
        for (Tile road : roads) {
            road.getBuilding().setHasCityConnection(false);
        }
        for (Tile city : cities) {
            calculateRoadConnections(city);
        }
        for (Tile road : roads) {
            road.getBuilding().setVisited(false);
        }
    }

    private void calculateRoadConnections(Tile origin) {
        Coordinates originCoordinates = origin.getCoordinates();
        origin.getBuilding().setVisited(true);

        for (Tile road : roads) {
            checkConnection(road, originCoordinates.x, originCoordinates.y - 1); //north
            checkConnection(road, originCoordinates.x, originCoordinates.y + 1); //south
            checkConnection(road, originCoordinates.x + 1, originCoordinates.y); //east
            checkConnection(road, originCoordinates.x - 1, originCoordinates.y); //west
        }
    }

    private void checkConnection(Tile road, int x, int y) {
        Coordinates roadCoordinates = road.getCoordinates();
        if (roadCoordinates.x == x && roadCoordinates.y == y) {
            road.getBuilding().setHasCityConnection(true);
            if (!road.getBuilding().isVisited()) {
                calculateRoadConnections(road);
            }
        }
    }

    private boolean roadAdjacent(int x, int y) {
        Coordinates coordinates;
        for (Tile road : roads) {
            coordinates = road.getCoordinates();
            if (coordinates.x == x && coordinates.y == y)
                return true;
        }
        for (Tile city : cities) {
            coordinates = city.getCoordinates();
            if (coordinates.x == x && coordinates.y == y)
                return true;
        }
        return false;
    }

    public void addConnectableTile(Tile newBuilding) {
        if (newBuilding.getBuilding().getType() == Constructable.CITY) {
            addCity(newBuilding);
        } else {
            addRoad(newBuilding);
        }
    }

    public ImageIcon getRoadImage(Tile roadTile) {
        Coordinates roadTileCoordinates = roadTile.getCoordinates();
        boolean North = roadAdjacent(roadTileCoordinates.x, roadTileCoordinates.y - 1); //north
        boolean South = roadAdjacent(roadTileCoordinates.x, roadTileCoordinates.y + 1); //south
        boolean East = roadAdjacent(roadTileCoordinates.x + 1, roadTileCoordinates.y); //east
        boolean West = roadAdjacent(roadTileCoordinates.x - 1, roadTileCoordinates.y); //west

        return roadTile.getBuilding().getImage(North, South, East, West);
    }

    public void removeRoad(Tile tile) {
        if (tile.getBuilding().getType().equals(Constructable.CITY)) {
            cities.remove(tile);
        } else {
            roads.remove(tile);
        }
        updateRoadConnections();
    }
}
