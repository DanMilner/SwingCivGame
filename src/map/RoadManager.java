package map;

import java.util.ArrayList;

class RoadManager {
    private ArrayList<Tile> Roads = new ArrayList<>();
    private ArrayList<Tile> Cities = new ArrayList<>();

    void addRoad(Tile road) {
        Roads.add(road);
        updateRoadConnections();
    }

    void addCity(Tile city) {
        Cities.add(city);
        updateRoadConnections();
    }

    private void updateRoadConnections() {
        for (Tile city : Cities) {
            calculateRoadConnections(city);
        }
        for (Tile road : Roads) {
            road.getBuilding().setVisited(false);
        }
    }

    private void calculateRoadConnections(Tile origin) {
        int x = origin.xCoord;
        int y = origin.yCoord;
        origin.getBuilding().setVisited(true);

        for (Tile road : Roads) {
            checkConnection(road,x,y-1); //north
            checkConnection(road,x,y+1); //south
            checkConnection(road,x+1,y); //east
            checkConnection(road,x-1,y); //west
        }
    }

    private void checkConnection(Tile road, int x, int y){
        if (road.xCoord == x && road.yCoord == y) {
            road.getBuilding().setHasCityConnection(true);
            if (!road.getBuilding().isVisited()) {
                calculateRoadConnections(road);
            }
        }
    }

    boolean roadAdjacent(int x, int y) {
        for (Tile road: Roads) {
            if(road.xCoord == x && road.yCoord == y)
                return true;
        }
        for (Tile city : Cities) {
            if(city.xCoord == x && city.yCoord == y)
                return true;
        }
        return false;
    }
}
