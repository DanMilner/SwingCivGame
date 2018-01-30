package main;

public enum ResourceTypes {
    WOOD, IRON, GOLD, COAL, COPPER, STONE, FOOD, WATER, DIAMONDS, GRASS, SAND, SNOW;

    public static int getNumberOfResourceTypes() {
        return ResourceTypes.values().length;
    }
}