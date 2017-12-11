package map;

import main.Player;
import map.buildings.*;
import map.resources.*;

class TileFactory {

    Tile buildBuildingTile(String type, int x, int y, Player owner) {
        switch (type){
            case "Tower":
                return new Tower(x,y,owner);
            case "Mine":
                return new Mine(x,y,owner);
            case "Lumber Mill":
                return new LumberMill(x,y,owner);
            case "Farm":
                return new Farm(x,y,owner);
            case "Aqueduct":
                return new Aqueduct(x,y,owner);
            case "Dock":
                return new Dock(x,y,owner);
            case "City":
                return new City(x,y,owner);
            case "Wheat":
                return new Wheat(x, y, owner);
            case "Road":
                return new Road(x, y, owner, null);
        }
        return null;
    }

    Tile buildResourceTile(String type, int x, int y) {
        switch (type){
            case "Forest":
                return new Forest(x, y, null);
            case "Mountain":
                return new Mountain(x, y, null);
            case "Water":
                return new Water(x, y, null);
            case "Grass":
                return new Grass(x, y, null);
            case "Iron":
                return new Iron(x, y, null);
            case "Gold":
                return new Gold(x, y, null);
            case "Copper":
                return new Copper(x, y, null);
            case "Coal":
                return new Coal(x, y, null);
        }
        return null;
    }
}
