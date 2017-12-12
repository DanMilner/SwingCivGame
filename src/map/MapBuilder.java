package map;

import map.resources.Resource;

import java.util.concurrent.ThreadLocalRandom;

class MapBuilder {
    private Tile[][] Map;
    private int MAPSIZE;
    MapBuilder(Tile[][] currentMap, int size){
        Map = currentMap;
        MAPSIZE = size;
    }

    public void setUpMap(){
        for (int x = 0; x <= MAPSIZE; x++) {
            for (int y = 0; y <= MAPSIZE; y++) {
                Map[x][y] = new Tile(x,y,null);
                constructResourceTile("Grass", x, y);
                Map[x][y].setUnit(null);
            }
        }
    }

    public void setUpTerrain(){
        //add trees
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE * 12, MAPSIZE * 15), 0, "Forest");

        //add Water
        addWater(ThreadLocalRandom.current().nextInt(MAPSIZE / 10, MAPSIZE / 5), ThreadLocalRandom.current().nextInt(MAPSIZE / 2, MAPSIZE * 2));

        //add Mountains
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 11, MAPSIZE / 7), ThreadLocalRandom.current().nextInt(MAPSIZE / 4, MAPSIZE), "Mountain");

        //add Iron
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 4, MAPSIZE / 3), ThreadLocalRandom.current().nextInt(3, MAPSIZE / 10), "Iron");

        //add Gold
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 10, MAPSIZE / 7), ThreadLocalRandom.current().nextInt(2, MAPSIZE / 10), "Gold");

        //add Copper
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 7, MAPSIZE / 6), ThreadLocalRandom.current().nextInt(2, MAPSIZE / 10), "Copper");

        //add Coal
        addTerrain(ThreadLocalRandom.current().nextInt(MAPSIZE / 5, MAPSIZE / 4), ThreadLocalRandom.current().nextInt(2, MAPSIZE / 10), "Coal");
    }

    private void addTerrain(int amount, int intensity, String type) {

        int Xcoords;
        int Ycoords;

        for (int y = 0; y < amount; y++) {
            do {
                Xcoords = ThreadLocalRandom.current().nextInt(0, MAPSIZE);
                Ycoords = ThreadLocalRandom.current().nextInt(0, MAPSIZE);
            } while (Map[Xcoords][Ycoords].getResource().getType().equals("Water"));

            if (type.equals("Forest")) {
                constructResourceTile("Forest", Xcoords, Ycoords);
            } else {
                for (int i = 0; i < intensity; i++) {
                    int direction = ThreadLocalRandom.current().nextInt(1, 5);

                    if (direction == 1) { //North
                        Ycoords = Ycoords + 1;
                    } else if (direction == 2) { //South
                        Ycoords = Ycoords - 1;
                    } else if (direction == 3) { //East
                        Xcoords = Xcoords + 1;
                    } else if (direction == 4) { //West
                        Xcoords = Xcoords - 1;
                    }
                    if (Ycoords <= MAPSIZE && Xcoords <= MAPSIZE && Ycoords >= 0 && Xcoords >= 0) {
                        String currentResource = Map[Xcoords][Ycoords].getResource().getType();

                        if (currentResource.equals("Mountain") || currentResource.equals("Iron") || currentResource.equals("Gold") || currentResource.equals("Copper") || currentResource.equals("Coal")) {
                            i--;
                        } else {
                            constructResourceTile(type, Xcoords, Ycoords);
                        }
                    }
                }
            }
        }
    }

    private void addWater(int amount, int intensity) {

        int Xcoords;
        int Ycoords;

        for (int y = 0; y < amount; y++) {
            Xcoords = ThreadLocalRandom.current().nextInt(1, MAPSIZE);
            Ycoords = ThreadLocalRandom.current().nextInt(1, MAPSIZE);
            int originalX = Xcoords;
            int originalY = Ycoords;
            int direction;
            int i = 0;
            do {
                direction = ThreadLocalRandom.current().nextInt(1, 5);
                if (direction == 1) { //North
                    Ycoords = Ycoords + 1;
                } else if (direction == 2) { //South
                    Ycoords = Ycoords - 1;
                } else if (direction == 3) { //East
                    Xcoords = Xcoords + 1;
                } else if (direction == 4) { //West
                    Xcoords = Xcoords - 1;
                }
                if (Ycoords <= MAPSIZE && Xcoords <= MAPSIZE && Ycoords >= 0 && Xcoords >= 0) {
                    if (!Map[Xcoords][Ycoords].getResource().getType().equals("Water")) {
                        constructResourceTile("Water", Xcoords, Ycoords);
                        Xcoords = originalX;
                        Ycoords = originalY;
                        i++;
                    }
                } else {
                    Xcoords = originalX;
                    Ycoords = originalY;
                }
            } while (i < intensity);
        }
    }

    private void constructResourceTile(String type, int x, int y){
        Resource newResource = TileFactory.buildResourceTile(type);
        Map[x][y].setResource(newResource);
        System.out.println(type + " spawned at " + x + " " + y);
    }
}
