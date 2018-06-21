package game.gui;

import game.map.Coordinates;
import game.map.mapModel.Map;
import game.map.resources.ResourceTypes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class BeachIconHandler {
    private final int MAPSIZE;
    private final Map map;

    BeachIconHandler(int MAPSIZE, Map map) {
        this.MAPSIZE = MAPSIZE;
        this.map = map;
    }

    public void setCorrectGrassIcons() {
        BufferedImage grass = null;
        try {
            grass = ImageIO.read(new File("textures\\terrain\\grass.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int x = 0; x < MAPSIZE; x++) {
            for (int y = 0; y < MAPSIZE; y++) {
                Coordinates coordinates = new Coordinates(x, y);
                if (map.getTile(coordinates).getResource().getResourceType() == ResourceTypes.GRASS ||
                        map.getTile(coordinates).getResource().getResourceType() == ResourceTypes.WOOD ) {

                    ImageIcon aa = map.getTile(coordinates).getResource().getImage();
                    BufferedImage bi = new BufferedImage(50,50, BufferedImage.TYPE_INT_RGB);
                    Graphics gs = bi.createGraphics();
// paint the Icon to the BufferedImage.
                    aa.paintIcon(null, gs, 0,0);
                    gs.dispose();

                    BufferedImage combined = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = combined.createGraphics();
                    g.drawImage(bi, 0, 0, null);
                    addBeachImages(coordinates, g);
                    addBeachCorners(coordinates, g);
                    map.getTile(coordinates).getResource().setIcon(new ImageIcon(combined));
                }
            }
        }
    }

    private void addBeachImages(Coordinates coordinates, Graphics2D g) {
        for (int rotation = 1; rotation < 5; rotation++) {
            drawBeachIfRequired(coordinates, g, rotation);
            g.rotate(Math.toRadians(90), 25, 25);
        }
    }

    private void drawBeachIfRequired(Coordinates coordinates, Graphics2D g, int corner) {
        BufferedImage beach = getBeachImageIfRequired(coordinates, corner);
        if (beach != null) {
            g.drawImage(beach, 0, 0, null);
        }
    }

    private BufferedImage getBeachImageIfRequired(Coordinates coordinates, int direction) {
        BufferedImage beach = null;
        try {
            beach = ImageIO.read(new File("textures\\terrain\\beach.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Coordinates tempCoordinates = new Coordinates(coordinates.x, coordinates.y);

        if (direction == 1) {
            tempCoordinates.setCoordinates(coordinates.x - 1, coordinates.y);
        } else if (direction == 2) {
            tempCoordinates.setCoordinates(coordinates.x, coordinates.y - 1);
        } else if (direction == 3) {
            tempCoordinates.setCoordinates(coordinates.x + 1, coordinates.y);
        } else {
            tempCoordinates.setCoordinates(coordinates.x, coordinates.y + 1);
        }
        if (isWater(tempCoordinates))
            return beach;
        return null;
    }

    private boolean isWater(Coordinates coordinates) {
        return map.coordinatesOnMap(coordinates) && map.getTile(coordinates).getResource().getResourceType() == ResourceTypes.WATER;
    }

    private void addBeachCorners(Coordinates coordinates, Graphics2D g) {
        for (int rotation = 1; rotation < 5; rotation++) {
            drawBeachCornerIfRequired(coordinates, g, rotation);
            g.rotate(Math.toRadians(90), 25, 25);
        }
    }

    private void drawBeachCornerIfRequired(Coordinates coordinates, Graphics2D g, int corner) {
        BufferedImage cornerImage = drawCornerImage(coordinates, corner);
        if (cornerImage != null)
            g.drawImage(cornerImage, 0, 0, null);
    }

    private BufferedImage drawCornerImage(Coordinates coordinates, int rotation) {
        Coordinates topLeftTile;
        Coordinates leftTile;
        Coordinates topTile;

        if (rotation == 1) {
            topLeftTile = new Coordinates(coordinates.x - 1, coordinates.y - 1);
            leftTile = new Coordinates(coordinates.x - 1, coordinates.y);
            topTile = new Coordinates(coordinates.x, coordinates.y - 1);
        } else if (rotation == 2) {
            topLeftTile = new Coordinates(coordinates.x + 1, coordinates.y - 1);
            leftTile = new Coordinates(coordinates.x, coordinates.y - 1);
            topTile = new Coordinates(coordinates.x + 1, coordinates.y);
        } else if (rotation == 3) {
            topLeftTile = new Coordinates(coordinates.x + 1, coordinates.y + 1);
            leftTile = new Coordinates(coordinates.x + 1, coordinates.y);
            topTile = new Coordinates(coordinates.x, coordinates.y + 1);
        } else {
            topLeftTile = new Coordinates(coordinates.x - 1, coordinates.y + 1);
            leftTile = new Coordinates(coordinates.x, coordinates.y + 1);
            topTile = new Coordinates(coordinates.x - 1, coordinates.y);
        }

        boolean topLeftHasWater = isWater(topLeftTile);
        boolean leftHasWater = isWater(leftTile); // 'Left' and 'Above' are relative to the current rotation
        boolean aboveHasWater = isWater(topTile);
        return getCornerImage(leftHasWater, aboveHasWater, topLeftHasWater);
    }

    private BufferedImage getCornerImage(boolean leftHasWater, boolean aboveHasWater, boolean topLeftHasWater) {
        if (leftHasWater && aboveHasWater) {
            try {
                return ImageIO.read(new File("textures\\terrain\\BeachCornerWater.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!leftHasWater && !aboveHasWater && topLeftHasWater) {
            try {
                return ImageIO.read(new File("textures\\terrain\\beachCorner.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}