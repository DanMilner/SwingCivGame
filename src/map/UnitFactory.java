package map;

import main.Player;
import units.*;

class UnitFactory {

    Unit BuildUnit(String type, Player owner){
        switch (type){
            case "Settler":
                return new Settler(owner);
            case "Builder":
                return new Builder(owner);
            case "Swordsman":
                return new Swordsman(owner);
            case "Archer":
                return new Knight(owner);
            case "Knight":
                return new Knight(owner);
        }
        return null;
    }
}
