package map;

import exceptions.TypeNotFound;
import map.buildings.*;
import map.resources.*;

class TileFactory {

    static Building buildBuildingTile(String type) throws TypeNotFound {
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
        throw new TypeNotFound("There is no building of type: " + type);
    }

    static Resource buildResourceTile(String type) throws TypeNotFound {
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
            case "Sand":
                return new Sand();
            case "Iron":
                return new Iron();
            case "Gold":
                return new Gold();
            case "Copper":
                return new Copper();
            case "Coal":
                return new Coal();
            case "Diamonds":
                return new Diamonds();
        }
        throw new TypeNotFound("There is no resource of type: " + type);
    }
}
