package game.map;

import java.util.Random;

public class RandomValues {
    private int intensity;
    private Random random;

    public RandomValues() {
        random = new Random();
    }

    public void setIntensity(int min, int max) {
        this.intensity = random.nextInt(max - min + 1) + min;
    }

    public int getIntensity() {
        return intensity;
    }

    public int randomInt(int bound) {
        return random.nextInt(bound);
    }

    public Coordinates getRandomCoordinates(int mapsize) {
        return new Coordinates(random.nextInt(mapsize), random.nextInt(mapsize));
    }

    public Coordinates getRandomDirection(Coordinates coordinates) {
        int direction = random.nextInt(5) + 1;

        if (direction == 1) { //North
            coordinates.setCoordinates(coordinates.x, coordinates.y + 1);
        } else if (direction == 2) { //South
            coordinates.setCoordinates(coordinates.x, coordinates.y - 1);
        } else if (direction == 3) { //East
            coordinates.setCoordinates(coordinates.x + 1, coordinates.y);
        } else if (direction == 4) { //West
            coordinates.setCoordinates(coordinates.x - 1, coordinates.y);
        }
        return coordinates;
    }
}
