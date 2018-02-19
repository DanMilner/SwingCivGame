package menu;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import java.awt.*;

public class PlayerRowComponents {
    private TextField playerName;
    private ColorPicker playerColor;
    private Button removePlayer;

    PlayerRowComponents(TextField playerName, ColorPicker playerColor, Button removePlayer) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.removePlayer = removePlayer;
    }

    public TextField getNameTextField() {
        return playerName;
    }

    public ColorPicker getColorPicker() {
        return playerColor;
    }

    public Button getRemovePlayer() {
        return removePlayer;
    }

    public String getPlayerNameString() {
        return playerName.getText();
    }

    public Color getPlayerColor() {
        javafx.scene.paint.Color javafxColor = playerColor.getValue();
        float r = (float) javafxColor.getRed();
        float g = (float) javafxColor.getGreen();
        float b = (float) javafxColor.getBlue();

        return new Color(r, g, b);
    }
}
