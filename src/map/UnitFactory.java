package map;

import exceptions.TypeNotFound;
import main.Player;
import units.*;

class UnitFactory {

    static Unit buildUnit(String type, Player owner) throws TypeNotFound {
        switch (type){
            case "Settler":
                return new Settler(owner);
            case "Builder":
                return new Builder(owner);
            case "Swordsman":
                return new Swordsman(owner);
            case "Archer":
                return new Archer(owner);
            case "Knight":
                return new Knight(owner);
            case "Catapult":
                return new Catapult(owner);
        }
        throw new TypeNotFound("There is no unit of type: " + type);
    }
}
