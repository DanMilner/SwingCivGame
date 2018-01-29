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
        int x = origin.xCoord;
        int y = origin.yCoord;
        origin.getBuilding().setVisited(true);

        for (Tile road : roads) {
            checkConnection(road, x, y - 1); //north
            checkConnection(road, x, y + 1); //south
            checkConnection(road, x + 1, y); //east
            checkConnection(road, x - 1, y); //west
        }
    }

    private void checkConnection(Tile road, int x, int y) {
        if (road.xCoord == x && road.yCoord == y) {
            road.getBuilding().setHasCityConnection(true);
            if (!road.getBuilding().isVisited()) {
                calculateRoadConnections(road);
            }
        }
    }

    private boolean roadAdjacent(int x, int y) {
        for (Tile road : roads) {
            if (road.xCoord == x && road.yCoord == y)
                return true;
        }
        for (Tile city : cities) {
            if (city.xCoord == x && city.yCoord == y)
                return true;
        }
        return false;
    }

    public void addConnectableTile(Tile newBuilding) {
        if (newBuilding.getBuilding().getType().equals("City")) {
            addCity(newBuilding);
        } else {
            addRoad(newBuilding);
        }
    }

    public ImageIcon getRoadImage(Tile roadTile) {
        int x = roadTile.xCoord;
        int y = roadTile.yCoord;
        boolean North = roadAdjacent(x, y - 1); //north
        boolean South = roadAdjacent(x, y + 1); //south
        boolean East = roadAdjacent(x + 1, y); //east
        boolean West = roadAdjacent(x - 1, y); //west

        return roadTile.getBuilding().getImage(North, South, East, West);
    }

    public void removeRoad(Tile tile) {
        if (tile.getBuilding().getType().equals("City")) {
            cities.remove(tile);
        } else {
            roads.remove(tile);
        }
        updateRoadConnections();
    }
}
