package main;

public class ButtonData {
    private String type;
    private int CurrentX;
    private int CurrentY;
    private String text;

    ButtonData(String type, int x, int y, String text){
        this.type = type;
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

    public String getType() {
        return type;
    }

    String getText() {
        return text;
    }
}
