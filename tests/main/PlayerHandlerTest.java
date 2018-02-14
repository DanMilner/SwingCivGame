package main;

import map.Map;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerHandlerTest {
    private PlayerHandler playerHandler;
    private Map gameMap;

    @Before
    public void setUp() {
        playerHandler = new PlayerHandler();
        MapData mapData = new MapData();
        mapData.setMapData(1,1,1,1,30, 1, true);
        gameMap = new Map(true, mapData);
    }

    @Test
    public void addPlayerTest() {
        ArrayList<PlayerData> playersToCreate = new ArrayList<>();

        String playerName = "Daniel";
        playersToCreate.add(new PlayerData("Daniel", Color.red));
        playerHandler.setUpPlayers(gameMap, playersToCreate);

        assertTrue(playerHandler.getCurrentPlayer().getName().equals(playerName));
    }

    @Test
    public void setUpPlayersTest() {
        ArrayList<PlayerData> playersToCreate = new ArrayList<>();
        playersToCreate.add(new PlayerData("Daniel", Color.red));
        playersToCreate.add(new PlayerData("A", Color.green));
        playersToCreate.add(new PlayerData("B", Color.yellow));
        playersToCreate.add(new PlayerData("C", Color.black));
        playersToCreate.add(new PlayerData("D", Color.blue));


        playerHandler.setUpPlayers(gameMap,playersToCreate);

        for (PlayerData ignored : playersToCreate) {
            assertFalse(playerHandler.getCurrentPlayer().getBuildings().isEmpty());
            assertTrue(playerHandler.getCurrentPlayer().getUnits().isEmpty());
            assertTrue(playerHandler.getCurrentPlayer().getColour() != null);
            playerHandler.incrementCurrentPlayer();
        }
    }

    @Test
    public void incrementCurrentPlayerTest() {
        ArrayList<PlayerData> playersToCreate = new ArrayList<>();
        playersToCreate.add(new PlayerData("Daniel", Color.red));
        playersToCreate.add(new PlayerData("Alastair", Color.green));

        playerHandler.setUpPlayers(gameMap, playersToCreate);

        assertTrue(playerHandler.getCurrentPlayer().getName().equals("Daniel"));
        playerHandler.incrementCurrentPlayer();
        assertTrue(playerHandler.getCurrentPlayer().getName().equals("Alastair"));
    }
}