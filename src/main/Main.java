package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.menu.Controller;
import main.menu.PlayerRowComponents;

import javax.swing.*;
import java.util.ArrayList;

public class Main extends Application {
    private ArrayList<PlayerRowComponents> playerRowComponents;
    private MapData mapData;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("main/menu/Menu.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Civ Swing Game");
        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("main/menu/Menu.css");
        Controller controller = fxmlLoader.getController();
        primaryStage.show();
        playerRowComponents = controller.getPlayers();
        mapData = controller.getMapData();
    }

    @Override
    public void stop() {
        ArrayList<PlayerData> playersToCreate = new ArrayList<>();
        PlayerData playerData;

        for (PlayerRowComponents playerRowComponent : playerRowComponents) {
            playerData = new PlayerData(playerRowComponent.getPlayerNameString(), playerRowComponent.getPlayerColor());
            playersToCreate.add(playerData);
        }

        GameController gameController = new GameController(playersToCreate, mapData);
        SwingUtilities.invokeLater(() -> new GuiManager(gameController, mapData.getMapsize()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
