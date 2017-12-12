package map;

import map.buildings.*;
import map.resources.*;

class TileFactory {

    static Building buildBuildingTile(String type) {
        switch (type){
            case "Tower":
                return new Tower();
            case "Mine":
                return new Mine();
            case "Lumber Mill":
                return new LumberMill();
            case "Farm":
                return new Farm();
            case "Aqueduct":
                return new Aqueduct();
            case "Dock":
                return new Dock();
            case "City":
                return new City();
            case "Wheat":
                return new Wheat();
            case "Road":
                return new Road();
        }
        return null;
    }

    static Resource buildResourceTile(String type) {
        switch (type){
            case "Forest":
                return new Forest();
            case "Mountain":
                return new Mountain();
            case "Water":
                return new Water();
            case "Grass":
                return new Grass();
            case "Snow":
                return new Snow();
            case "Iron":
                return new Iron();
            case "Gold":
                return new Gold();
            case "Copper":
                return new Copper();
            case "Coal":
                return new Coal();
        }
        return null;
    }
}
