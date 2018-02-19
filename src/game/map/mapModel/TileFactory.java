package game.map.mapModel;

import exceptions.TypeNotFound;
import game.map.Constructable;
import game.map.resources.ResourceTypes;
import game.map.buildings.*;
import game.map.resources.*;

public class TileFactory {
    public static Building buildBuildingTile(Constructable type) throws TypeNotFound {
        switch (type) {
            case TOWER:
                return new Tower();
            case MINE:
                return new Mine();
            case LUMBERMILL:
                return new LumberMill();
            case FARM:
                return new Farm();
            case AQUEDUCT:
                return new Aqueduct();
            case DOCK:
                return new Dock();
            case CITY:
                return new City();
            case WHEAT:
                return new Wheat();
            case BARRACKS:
                return new Barracks();
            case WORKSHOP:
                return new Workshop();
            case ARCHERYRANGE:
                return new ArcheryRange();
            case STABLE:
                return new Stable();
            case ROAD:
                return new Road();
        }
        throw new TypeNotFound("There is no building of type: " + type);
    }

    public static Resource buildResourceTile(ResourceTypes resourceType) throws TypeNotFound {
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
