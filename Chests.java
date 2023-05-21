package hungrygamespackage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Random;

public class Chests {
    private static Random r = new Random();
    private static ArrayList<Location> chests = new ArrayList<Location>();

    private static Material[] loot = {Material.STONE_SWORD, Material.STONE_AXE, Material.WOOD_SWORD, Material.WOOD_AXE,
    Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.PORK, Material.COOKED_MUTTON, Material.APPLE, Material.FISHING_ROD, Material.BREAD,
    Material.STICK, Material.IRON_INGOT, Material.GOLD_INGOT, Material.ARROW, Material.BOW, Material.GOLD_HELMET, Material.GOLD_CHESTPLATE,
    Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS,
    Material.CHAINMAIL_BOOTS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};

    private static int numOfLoot[] = {1, 1, 1, 1, 3, 3, 3, 3, 3, 1, 3, 2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    public static void removeChests(World w, Location sl, Location el, int t) {
        if (t == 1 && Main.day == 0) {
            for (double x = sl.getX(); x <= el.getX(); x++) {
                for (double y = sl.getY(); y <= el.getY(); y++) {
                    for (double z = sl.getZ(); z <= el.getZ(); z++) {
                        if (w.getBlockAt((int) x, (int) y, (int) z).getType() == Material.CHEST) {
                            w.getBlockAt((int) x, (int) y, (int) z).setType(Material.AIR);
                        }
                    }
                }
            }
            for (Entity entity: w.getEntities()) {
                if (entity instanceof Item) {
                    entity.remove();
                }
            }
        }

    }
    public static void setChests(World w, Location sl, Location el, int t) {
        if (t == 2 && Main.day == 0) {
            for (double x = sl.getX(); x <= el.getX(); x++) {
                for (double y = sl.getY(); y <= el.getY(); y++) {
                    for (double z = sl.getZ(); z <= el.getZ(); z++) {
                        if (w.getBlockAt((int)x, (int)y, (int)z).getType() == Material.AIR &&
                                w.getBlockAt((int)x, (int)y + 1, (int)z).getType() == Material.AIR &&
                                w.getBlockAt((int)x, (int)y - 1, (int)z).getType() != Material.AIR &&
                                w.getBlockAt((int)x, (int)y - 1, (int)z).getType() != Material.STATIONARY_WATER &&
                                w.getBlockAt((int)x, (int)y - 1, (int)z).getType() != Material.STATIONARY_LAVA) {
                            int chance = r.nextInt(350);
                            if (chance == 0) {
                                w.getBlockAt((int)x, (int)y, (int)z).setType(Material.CHEST);
                                chests.add(new Location(w, x, y, z));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void fillChests(World w, int t) {
        if ((t == 4 && Main.day == 0) || (t == 3300 && Main.day == 1)) {
            for (int i = 0; i < chests.size(); i++) {
                Chest chest = (Chest) w.getBlockAt((int)chests.get(i).getX(), (int)chests.get(i).getY(), (int)chests.get(i).getZ()).getState();
                Inventory chestinv = chest.getBlockInventory();
                for (int j = 0; j < r.nextInt(3) + 2; j++) {
                    int selector = r.nextInt(loot.length);
                    chestinv.setItem(r.nextInt(27), new ItemStack(loot[selector], r.nextInt(numOfLoot[selector]) + 1));
                }

            }
        }
    }


    // Chest chest = (Chest) w.getBlockAt(locX.get(select), locY.get(select) + 1, locZ.get(select)).getState();
}
