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
            road.setVisited(false);
        }
    }

    private void calculateRoadConnections(Tile origin) {
        int x = origin.Xcoord;
        int y = origin.Ycoord;
        origin.setVisited(true);

        for (Tile road : Roads) {
            checkConnection(road,x,y-1); //north
            checkConnection(road,x,y+1); //south
            checkConnection(road,x+1,y); //east
            checkConnection(road,x-1,y); //west
        }
    }

    private void checkConnection(Tile road, int x, int y){
        if (road.Xcoord == x && road.Ycoord == y) {
            road.setHasCityConnection(true);
            if (!road.isVisited()) {
                calculateRoadConnections(road);
            }
        }
    }

    boolean roadAdjacent(int x, int y) {
        for (Tile road: Roads) {
            if(road.Xcoord == x && road.Ycoord == y)
                return true;
        }
        for (Tile city : Cities) {
            if(city.Xcoord == x && city.Ycoord == y)
                return true;
        }
        return false;
    }
}
