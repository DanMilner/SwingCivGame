package main;

public class ButtonData {
    private int CurrentX;
    private int CurrentY;
    private String text;

    ButtonData(int x, int y, String text) {
        this.CurrentX = x;
        this.CurrentY = y;
        this.text = text;
    }

    int getCurrentX() {
        return CurrentX;
    }

    int getCurrentY() {
        return CurrentY;
    }

    String getText() {
        return text;
    }
}
