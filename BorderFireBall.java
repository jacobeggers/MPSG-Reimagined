package hungrygamespackage;

import org.bukkit.Location;

import java.util.Random;

public class BorderFireBall {
    private Location location;
    private double size;
    private int offset;
    private char borderSide;

    private Random r = new Random();
    public BorderFireBall(Location location, double size, int offset, char borderSide) {
        this.location = location;
        this.size = size;
        this.offset = offset;
        this.borderSide = borderSide;
    }

    public Location summonFireBall() {
        double x = 0;
        if (borderSide == 'x') {
            if (location.getX() > 0) {
                x = (size / 2) - offset;
            } else {
                x = ((size / 2) + offset) * -1;
            }
        } else if (borderSide == 'z') {
            x = (location.getX() + r.nextInt(10)) - 5;
        }
        double y = location.getY() + r.nextInt(10);
        double z = 0;
        if (borderSide == 'z') {
            if (location.getZ() > 0) {
                z = (size / 2) + offset;
            } else {
                z = ((size / 2) + offset) * -1;
            }
        } else if (borderSide == 'x') {
            z = (location.getZ() + r.nextInt(10)) - 5;
        }
        return new Location(location.getWorld(), x, y, z);
    }

}
