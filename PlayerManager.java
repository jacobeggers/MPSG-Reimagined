package hungrygamespackage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class PlayerManager {
    private static Random r = new Random();
    public static ArrayList<Player> players = new ArrayList<Player>();
    private static ArrayList<Location> potentialLocations = new ArrayList<Location>();
    private static ArrayList<Location> podiumLocations = new ArrayList<Location>();
    public static void setPlayerSpawn(World w,  Location sl, Location el, int t) {
        if (t == -294) {
            for (double x = sl.getX(); x <= el.getX(); x++) {
                for (double y = sl.getY(); y <= el.getY(); y++) {
                    for (double z = sl.getZ(); z <= el.getZ(); z++) {
                        if (w.getBlockAt((int)x, (int)y, (int)z).getType() == Material.GOLD_PLATE &&
                                w.getBlockAt((int)x, (int)y - 1, (int)z).getType() == Material.WOOL) {
                            potentialLocations.add(new Location(w, (int)x, (int)y + 4, (int)z));
                        }
                    }
                }
            }
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                players.add(player);
                int selector = r.nextInt(potentialLocations.size());
                podiumLocations.add(potentialLocations.get(selector));
                potentialLocations.remove(selector);
            }

        }
    }
    public static void forcePlayersAtSpawn(World w, int t) {
        if (t == -293 || GameStats.deathmatchCountDown == 10) {
            for (int i = 0; i < players.size(); i++) {
                players.get(i).teleport(new Location(w, podiumLocations.get(i).getX() + 0.5,
                        podiumLocations.get(i).getY(),
                        podiumLocations.get(i).getZ() + 0.5));
                w.getBlockAt((int) podiumLocations.get(i).getX() + 1,
                        (int) podiumLocations.get(i).getY() + 1,
                        (int) podiumLocations.get(i).getZ()).setType(Material.BARRIER);
                w.getBlockAt((int) podiumLocations.get(i).getX() - 1,
                        (int) podiumLocations.get(i).getY() + 1,
                        (int) podiumLocations.get(i).getZ()).setType(Material.BARRIER);
                w.getBlockAt((int) podiumLocations.get(i).getX(),
                        (int) podiumLocations.get(i).getY() + 1,
                        (int) podiumLocations.get(i).getZ() + 1).setType(Material.BARRIER);
                w.getBlockAt((int) podiumLocations.get(i).getX(),
                        (int) podiumLocations.get(i).getY() + 1,
                        (int) podiumLocations.get(i).getZ() - 1).setType(Material.BARRIER);
                w.getBlockAt((int) podiumLocations.get(i).getX(),
                        (int) podiumLocations.get(i).getY() + 2,
                        (int) podiumLocations.get(i).getZ()).setType(Material.BARRIER);
            }
        }
    }
    public static void releasePlayersFromSpawn(World w, int t) {
        if (t == -1 || (GameStats.deathmatchCountDown == -1 && GameStats.chestTime == -1)) {
            for (int i = 0; i < players.size(); i++) {
                w.getBlockAt((int)podiumLocations.get(i).getX() + 1,
                        (int)podiumLocations.get(i).getY() + 1,
                        (int)podiumLocations.get(i).getZ()).setType(Material.AIR);
                w.getBlockAt((int)podiumLocations.get(i).getX() - 1,
                        (int)podiumLocations.get(i).getY() + 1,
                        (int)podiumLocations.get(i).getZ()).setType(Material.AIR);
                w.getBlockAt((int)podiumLocations.get(i).getX(),
                        (int)podiumLocations.get(i).getY() + 1,
                        (int)podiumLocations.get(i).getZ() + 1).setType(Material.AIR);
                w.getBlockAt((int)podiumLocations.get(i).getX(),
                        (int)podiumLocations.get(i).getY() + 1,
                        (int)podiumLocations.get(i).getZ() - 1).setType(Material.AIR);
                w.getBlockAt((int)podiumLocations.get(i).getX(),
                        (int)podiumLocations.get(i).getY() + 2,
                        (int)podiumLocations.get(i).getZ()).setType(Material.AIR);
            }
        }
    }
}
