package main;

public class MapData {
    private double trees;
    private double mountains;
    private double water;
    private double resources;
    private double deserts;
    private boolean arcticRegions;


    private Double mapsize;

    public void setMapData(double trees, double mountains, double water, double resources, double mapsize, double deserts, boolean arcticRegions) {
        this.trees = trees;
        this.mountains = mountains;
        this.water = water;
        this.resources = resources;
        this.mapsize = mapsize;
        this.deserts = deserts;
        this.arcticRegions = arcticRegions;
    }

    public double getTrees() {
        return trees;
    }

    public double getMountains() {
        return mountains;
    }

    public double getWater() {
        return water;
    }

    public double getResources() {
        return resources;
    }

    public int getMapsize() {
        return mapsize.intValue();
    }

    public double getDeserts() {
        return deserts;
    }

    public boolean isArcticRegions() {
        return arcticRegions;
    }
}
