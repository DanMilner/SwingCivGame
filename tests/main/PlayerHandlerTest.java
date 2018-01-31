package main;

import map.Map;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerHandlerTest {
    private PlayerHandler playerHandler;
    private Map gameMap;

    @Before
    public void setUp() {
        playerHandler = new PlayerHandler();
        gameMap = new Map(true, 30);
    }

    @Test
    public void addPlayerTest() {
        String playerName = "Daniel";
        playerHandler.addPlayer(playerName);
        playerHandler.setUpPlayers(gameMap);

        assertTrue(playerHandler.getCurrentPlayer().getName().equals(playerName));
    }

    @Test
    public void setUpPlayersTest() {
        String[] players = {"Daniel", "Joe", "Jane", "Al", "Mary"};
        for (String playerName : players) {
            playerHandler.addPlayer(playerName);
        }

        playerHandler.setUpPlayers(gameMap);

        for (String ignored : players) {
            assertFalse(playerHandler.getCurrentPlayer().getBuildings().isEmpty());
            assertTrue(playerHandler.getCurrentPlayer().getUnits().isEmpty());
            assertTrue(playerHandler.getCurrentPlayer().getColour() != null);
            playerHandler.incrementCurrentPlayer();
        }
    }

    @Test
    public void incrementCurrentPlayerTest() {
        playerHandler.addPlayer("Daniel");
        playerHandler.addPlayer("Alastair");

        playerHandler.setUpPlayers(gameMap);

        assertTrue(playerHandler.getCurrentPlayer().getName().equals("Daniel"));
        playerHandler.incrementCurrentPlayer();
        assertTrue(playerHandler.getCurrentPlayer().getName().equals("Alastair"));
    }
}