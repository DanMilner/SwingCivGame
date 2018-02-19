package game.gameModel;

import game.map.Coordinates;
import game.map.mapModel.Map;
import game.map.mapModel.Tile;
import game.map.buildings.Building;
import game.map.units.Unit;

public class AttackHandler {

    private Map gameMap;

    AttackHandler(Map gameMap) {
        this.gameMap = gameMap;
    }

    public boolean attackIsPossible(Coordinates originCoordinates, Coordinates targetCoordinates) {
        if (!gameMap.coordinatesOnMap(targetCoordinates))
            return false;

        Unit currentUnit = gameMap.getUnit(originCoordinates);
        if (UnitMovementHandler.tileIsOutOfRange(originCoordinates, targetCoordinates, currentUnit.getAttackRange()))
            return false;

        Tile targetTile = gameMap.getTile(targetCoordinates);
        if (targetTile.hasUnit())
            return targetTile.getUnit().getOwner() != currentUnit.getOwner();
        if (targetTile.hasBuilding())
            return targetTile.getOwner() != currentUnit.getOwner();

        return false;
    }

    public void performAttack(Coordinates originCoordinates, Coordinates targetCoordinates) {
        Unit currentUnit = gameMap.getUnit(originCoordinates);

        if (gameMap.getTile(targetCoordinates).hasUnit()) {
            Unit targetUnit = gameMap.getUnit(targetCoordinates);
            targetUnit.reduceCurrentHealthBy(currentUnit.getAttackDamage());
            if (targetUnit.getCurrentHealth() <= 0)
                gameMap.killUnitAndRefundCost(targetCoordinates);
        } else {
            Building targetBuilding = gameMap.getTile(targetCoordinates).getBuilding();
            targetBuilding.reduceCurrentHealthBy(currentUnit.getAttackDamage());
            if (targetBuilding.getCurrentHealth() <= 0) {
                Player player = gameMap.getTile(targetCoordinates).getOwner();
                gameMap.destroyBuildingAndRefundCost(targetCoordinates);
                PlayerResourceHandler.calculateResources(player);
            }
        }
        currentUnit.setRemainingMoves(0);
    }
}
