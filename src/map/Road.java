package map;

import main.Player;
import units.Unit;

import javax.swing.*;

class Road extends Tile {
    Road(int Xcoord, int Ycoord, Player owner, Unit unit) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("textures/roads/Road.png");
        this.type = "Road";
        this.setUnit(unit);
    }

    public ImageIcon getImage(boolean North, boolean South, boolean East, boolean West){
        if(North && South && East && West){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\CrossRoad.jpg");
        }else if(North && South && East){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\TriRight.jpg");
        }else if(North && South && West){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\TriLeft.jpg");
        }else if(North && East && West){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\TriTop.jpg");
        }else if(South && East && West){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\TriBottom.jpg");
        }else if(South && West){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\BottomLeft.jpg");
        }else if(South && East){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\BottomRight.jpg");
        }else if(North && West){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\TopLeft.jpg");
        }else if(North && East){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\TopRight.jpg");
        }else if(North || South){
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\RoadVertical.jpg");
        }else{
            return new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\roads\\RoadHorizontal.jpg");
        }
    }
}

