package be.shark_zekrom;


import org.bukkit.Location;
import org.bukkit.World;

public class Cuboid {
    private final int minX, maxX, minY, maxY, minZ, maxZ;

    public Cuboid(Location loc1, Location loc2) {
        this(loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
    }

    public Cuboid(int x1, int y1, int z1, int x2, int y2, int z2) {

        minX = Math.min(x1, x2);
        minY = Math.min(y1, y2);
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxY = Math.max(y1, y2);
        maxZ = Math.max(z1, z2);
    }


    public boolean contains(Location location) {
        return location.getBlockX() >= minX && location.getBlockX() <= maxX &&
                location.getBlockY() >= minY && location.getBlockY() <= maxY &&
                location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ;
    }
}