package game.map.mapModel;

import exceptions.TypeNotFound;
import game.gameModel.Player;
import game.map.Constructable;
import game.map.units.*;

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
