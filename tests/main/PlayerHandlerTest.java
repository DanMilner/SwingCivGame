package main;

import map.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerHandlerTest {
    private PlayerHandler playerHandler;
    private Map gameMap;
    @Before
    public void setUp(){
        playerHandler = new PlayerHandler();
        gameMap = new Map(true, 30);
    }

    @Test
    public void addPlayerTest() {
        String playerName = "Daniel";
        playerHandler.addPlayer(playerName);
        playerHandler.setUpPlayers(gameMap);

        Assert.assertTrue(playerHandler.getCurrentPlayer().getName().equals(playerName));
    }

    @Test
    public void setUpPlayersTest() {
        String[] players = {"Daniel", "Joe", "Jane", "Al", "Mary"};
        for (String playerName: players) {
            playerHandler.addPlayer(playerName);
        }

        playerHandler.setUpPlayers(gameMap);

        for(String ignored : players) {
            Assert.assertFalse(playerHandler.getCurrentPlayer().getBuildings().isEmpty());
            Assert.assertTrue(playerHandler.getCurrentPlayer().getUnits().isEmpty());
            Assert.assertTrue(playerHandler.getCurrentPlayer().getColour() != null);
            playerHandler.incrementCurrentPlayer();
        }
    }

    @Test
    public void incrementCurrentPlayerTest() {
        playerHandler.addPlayer("Daniel");
        playerHandler.addPlayer("Alastair");

        playerHandler.setUpPlayers(gameMap);

        Assert.assertTrue(playerHandler.getCurrentPlayer().getName().equals("Daniel"));
        playerHandler.incrementCurrentPlayer();
        Assert.assertTrue(playerHandler.getCurrentPlayer().getName().equals("Alastair"));
    }
}