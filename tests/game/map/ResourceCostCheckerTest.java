package game.map;

import game.gameModel.Player;
import game.map.mapModel.ResourceCostChecker;
import game.map.resources.ResourceTypes;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResourceCostCheckerTest {
    private Player player;

    @Before
    public void setUp() {
        player = new Player("Daniel", Color.yellow);
    }

    @Test
    public void checkCostTestBuildingHasEnoughResources() {
        Boolean buildingCostCheck = true;
        givePlayerResources(50);

        assertTrue(ResourceCostChecker.checkCost(Constructable.CITY, player, buildingCostCheck));
    }

    @Test
    public void checkCostTestBuildingNotEnoughResources() {
        Boolean buildingCostCheck = true;
        givePlayerResources(10);

        assertFalse(ResourceCostChecker.checkCost(Constructable.CITY, player, buildingCostCheck));
    }

    @Test
    public void checkCostTestUnitHasEnoughResources() {
        Boolean buildingCostCheck = false;
        givePlayerResources(40);

        assertTrue(ResourceCostChecker.checkCost(Constructable.BUILDER, player, buildingCostCheck));
    }

    @Test
    public void checkCostTestUnitNotEnoughResources() {
        Boolean buildingCostCheck = false;
        givePlayerResources(1);

        assertFalse(ResourceCostChecker.checkCost(Constructable.BUILDER, player, buildingCostCheck));
    }

    private void givePlayerResources(int amount) {
        for (ResourceTypes resourceType : ResourceTypes.values()) {
            player.setResource(resourceType, amount);
        }
    }
}