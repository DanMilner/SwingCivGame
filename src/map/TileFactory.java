package map;

import exceptions.TypeNotFound;
import main.ResourceTypes;
import map.buildings.*;
import map.resources.*;

class TileFactory {

    static Building buildBuildingTile(String type) throws TypeNotFound {
        switch (type) {
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
            case "Barracks":
                return new Barracks();
            case "Workshop":
                return new Workshop();
            case "ArcheryRange":
                return new ArcheryRange();
            case "Stable":
                return new Stable();
            case "Road":
                return new Road();
        }
        throw new TypeNotFound("There is no building of type: " + type);
    }

    static Resource buildResourceTile(ResourceTypes resourceType) throws TypeNotFound {
        switch (resourceType) {
            case WOOD:
                return new Forest();
            case STONE:
                return new Mountain();
            case WATER:
                return new Water();
            case GRASS:
                return new Grass();
            case SNOW:
                return new Snow();
            case SAND:
                return new Sand();
            case IRON:
                return new Iron();
            case GOLD:
                return new Gold();
            case COPPER:
                return new Copper();
            case COAL:
                return new Coal();
            case DIAMONDS:
                return new Diamonds();
        }
        throw new TypeNotFound("There is no resource of type: " + resourceType.toString());
    }
}
