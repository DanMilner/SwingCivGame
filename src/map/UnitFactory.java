package map;

import exceptions.TypeNotFound;
import main.Player;
import map.units.*;

public class UnitFactory {
    public static Unit buildUnit(Constructable type, Player owner) throws TypeNotFound {
        switch (type) {
            case SETTLER:
                return new Settler(owner);
            case BUILDER:
                return new Builder(owner);
            case SWORDSMAN:
                return new Swordsman(owner);
            case ARCHER:
                return new Archer(owner);
            case KNIGHT:
                return new Knight(owner);
            case CATAPULT:
                return new Catapult(owner);
        }
        throw new TypeNotFound("There is no unit of type: " + type);
    }
}
