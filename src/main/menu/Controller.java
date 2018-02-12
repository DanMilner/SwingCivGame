package main.menu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Controller {

    @FXML
    private Pane menuPane;
    @FXML
    private TextField treeValue;
    @FXML
    private TextField mountainValue;
    @FXML
    private TextField waterValue;
    @FXML
    private TextField resourcesValue;
    @FXML
    private TextField mapsizeValue;
    @FXML
    private Slider treeSlider;
    @FXML
    private Slider mountainSlider;
    @FXML
    private Slider waterSlider;
    @FXML
    private Slider resourcesSlider;
    @FXML
    private Slider mapsizeSlider;
    @FXML
    private Button addPlayerButton;

    private ArrayList<PlayerRowComponents> playerRowComponents;

    public void initialize() {
        treeSlider.valueProperty().addListener((ov, old_val, new_val) -> treeValue.setText(Integer.toString(new_val.intValue())));
        mountainSlider.valueProperty().addListener((ov, old_val, new_val) -> mountainValue.setText(Integer.toString(new_val.intValue())));
        waterSlider.valueProperty().addListener((ov, old_val, new_val) -> waterValue.setText(Integer.toString(new_val.intValue())));
        resourcesSlider.valueProperty().addListener((ov, old_val, new_val) -> resourcesValue.setText(Integer.toString(new_val.intValue())));
        mapsizeSlider.valueProperty().addListener((ov, old_val, new_val) -> mapsizeValue.setText(Integer.toString(new_val.intValue())));

        treeValue.textProperty().addListener((ov, old_val, new_val) -> treeSlider.setValue(Double.parseDouble(new_val)));
        mountainValue.textProperty().addListener((ov, old_val, new_val) -> mountainSlider.setValue(Double.parseDouble(new_val)));
        waterValue.textProperty().addListener((ov, old_val, new_val) -> waterSlider.setValue(Double.parseDouble(new_val)));
        resourcesValue.textProperty().addListener((ov, old_val, new_val) -> resourcesSlider.setValue(Double.parseDouble(new_val)));
        mapsizeValue.textProperty().addListener((ov, old_val, new_val) -> mapsizeSlider.setValue(Double.parseDouble(new_val)));

        playerRowComponents = new ArrayList<>();
    }

    public void addPlayer() {
        Button removePlayer = new Button("-");
        removePlayer.setId("removePlayer");
        removePlayer.setOnAction((event) -> removePlayer(removePlayer));
        TextField playerName = new TextField();
        ColorPicker playerColor = new ColorPicker();

        PlayerRowComponents newPlayer = new PlayerRowComponents(playerName, playerColor, removePlayer);
        playerRowComponents.add(newPlayer);

        int multiplier = playerRowComponents.size();

        playerName.setLayoutX(50);
        playerName.setLayoutY(50 + (50 * multiplier));
        playerColor.setLayoutX(250);
        playerColor.setLayoutY(50 + (50 * multiplier));
        removePlayer.setLayoutX(10);
        removePlayer.setLayoutY(50 + (50 * multiplier));
        addPlayerButton.setLayoutX(50);
        addPlayerButton.setLayoutY(100 + (50 * multiplier));

        setAddPlayerButtonVisibility();

        menuPane.getChildren().add(playerName);
        menuPane.getChildren().add(playerColor);
        menuPane.getChildren().add(removePlayer);
    }

    private void removePlayer(Button button) {
        PlayerRowComponents playerToRemove = null;

        for (PlayerRowComponents playerData : playerRowComponents) {
            if (playerData.getRemovePlayer() == button) {
                playerToRemove = playerData;
            }
            if (playerToRemove != null) {
                playerData.getNameTextField().setLayoutY(playerData.getNameTextField().getLayoutY() - 50);
                playerData.getColorPicker().setLayoutY(playerData.getColorPicker().getLayoutY() - 50);
                playerData.getRemovePlayer().setLayoutY(playerData.getRemovePlayer().getLayoutY() - 50);
            }
        }
        addPlayerButton.setLayoutY(addPlayerButton.getLayoutY() - 50);

        if (playerToRemove == null)
            return;
        playerRowComponents.remove(playerToRemove);
        menuPane.getChildren().remove(button);
        menuPane.getChildren().remove(playerToRemove.getNameTextField());
        menuPane.getChildren().remove(playerToRemove.getColorPicker());
        setAddPlayerButtonVisibility();
    }

    private void setAddPlayerButtonVisibility() {
        if (playerRowComponents.size() == 10) {
            addPlayerButton.setVisible(false);
        } else {
            addPlayerButton.setVisible(true);
        }
    }

    public void startGame() {
        Platform.exit();
    }

    public ArrayList<PlayerRowComponents> getPlayers() {
        return playerRowComponents;
    }
}
