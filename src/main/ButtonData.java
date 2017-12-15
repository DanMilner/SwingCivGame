package main;

public class ButtonData {
    private boolean unitSelected;
    private int CurrentX;
    private int CurrentY;
    private String text;

    ButtonData(boolean unitSelected, int x, int y, String text){
        this.unitSelected = unitSelected;
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

    public boolean isUnitSelected() {
        return unitSelected;
    }
}
