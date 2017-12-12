package map;

import main.Player;
import units.*;

class UnitFactory {

    static Unit buildUnit(String type, Player owner){
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
        return null;
    }
}
