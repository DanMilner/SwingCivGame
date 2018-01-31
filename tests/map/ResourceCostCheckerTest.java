package map;

import main.Player;
import main.ResourceTypes;
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
        String buildType = "City";
        Boolean buildingCostCheck = true;
        givePlayerResources(50);

        assertTrue(ResourceCostChecker.checkCost(buildType, player, buildingCostCheck));
    }

    @Test
    public void checkCostTestBuildingNotEnoughResources() {
        String buildType = "City";
        Boolean buildingCostCheck = true;
        givePlayerResources(10);

        assertFalse(ResourceCostChecker.checkCost(buildType, player, buildingCostCheck));
    }

    @Test
    public void checkCostTestUnitHasEnoughResources() {
        String unitType = "Builder";
        Boolean buildingCostCheck = false;
        givePlayerResources(40);

        assertTrue(ResourceCostChecker.checkCost(unitType, player, buildingCostCheck));
    }

    @Test
    public void checkCostTestUnitNotEnoughResources() {
        String unitType = "Builder";
        Boolean buildingCostCheck = false;
        givePlayerResources(1);

        assertFalse(ResourceCostChecker.checkCost(unitType, player, buildingCostCheck));
    }

    private void givePlayerResources(int amount) {
        for (ResourceTypes resourceType : ResourceTypes.values()) {
            player.setResource(resourceType, amount);
        }
    }
}