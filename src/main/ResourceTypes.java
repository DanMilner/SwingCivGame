package main;

public class ResourceTypes {
    public static final int WOOD = 0;
    public static final int IRON = 1;
    public static final int GOLD = 2;
    public static final int COAL = 3;
    public static final int COPPER = 4;
    public static final int STONE = 5;
    public static final int FOOD = 6;
    public static final int WATER = 7;
    public static final int DIAMONDS = 8;


    public static int getNumberOfResourceTypes() {
        return 9;
    }

    public static int getResourceTypeIndex(String type) {
        switch (type) {
            case "Wood":
                return WOOD;
            case "Iron":
                return IRON;
            case "Gold":
                return GOLD;
            case "Coal":
                return COAL;
            case "Copper":
                return COPPER;
            case "Mountain":
                return STONE;
            case "Food":
                return FOOD;
            case "Water":
                return WATER;
            case "Diamonds":
                return DIAMONDS;
            default:
                return 0;
        }
    }
}