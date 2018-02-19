package game.map.units;

import game.map.Constructable;
import game.gameModel.Player;
import game.map.resources.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Builder extends Unit {

    public Builder(Player owner) {
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Builder.png");
        this.type = Constructable.BUILDER;
        this.maxMoves = 4;
        this.remainingMoves = maxMoves;
        this.maxHealth = 20;
        this.currentHealth = maxHealth;
        this.resourceCost.put(ResourceTypes.FOOD, 2);
        this.buttonList = new ArrayList<>();
        buttonList.add(Constructable.FARM);
        buttonList.add(Constructable.TOWER);
        buttonList.add(Constructable.MINE);
        buttonList.add(Constructable.LUMBERMILL);
        buttonList.add(Constructable.AQUEDUCT);
        buttonList.add(Constructable.DOCK);
        buttonList.add(Constructable.ROAD);
        buttonList.add(Constructable.BARRACKS);
        buttonList.add(Constructable.WORKSHOP);
        buttonList.add(Constructable.ARCHERYRANGE);
        buttonList.add(Constructable.STABLE);
    }
}