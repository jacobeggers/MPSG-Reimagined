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
    public static ArrayList<Location> chests = new ArrayList<Location>();

    private static Material[] loot = {Material.STONE_SWORD, Material.STONE_AXE, Material.WOOD_SWORD, Material.WOOD_AXE,
        Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.PORK, Material.COOKED_MUTTON, Material.APPLE, Material.FISHING_ROD, Material.BREAD,
        Material.STICK, Material.IRON_INGOT, Material.GOLD_INGOT, Material.ARROW, Material.BOW, Material.GOLD_HELMET, Material.GOLD_CHESTPLATE,
        Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS,
        Material.CHAINMAIL_BOOTS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};

    private static Material[] midLoot = {Material.STONE_SWORD, Material.STONE_AXE, Material.WOOD_SWORD, Material.WOOD_AXE,
            Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.PORK, Material.COOKED_MUTTON, Material.APPLE, Material.FISHING_ROD, Material.BREAD,
            Material.STICK, Material.IRON_INGOT, Material.GOLD_INGOT, Material.ARROW, Material.BOW, Material.GOLD_HELMET, Material.GOLD_CHESTPLATE,
            Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS,
            Material.CHAINMAIL_BOOTS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.DIAMOND};

    private static int numOfLoot[] = {1, 1, 1, 1, 3, 3, 3, 3, 3, 1, 3, 2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private static int numOfMidLoot[] = {1, 1, 1, 1, 3, 3, 3, 3, 3, 1, 3, 2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    public static void removeChests(World w, Location sl, Location el, int t) {
        if (t == -299 && Main.day == 0) {
            for (int i = chests.size() - 1; i >= 0; i--) {
                chests.remove(i);
            }
            System.out.println(chests.size());
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
        if (t == -298 && Main.day == 0) {
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
                        } else if (w.getBlockAt((int)x, (int)y, (int)z).getType() == Material.ENDER_PORTAL_FRAME) {
                            if (r.nextBoolean() == true) {
                                w.getBlockAt((int)x, (int)y + 2, (int)z).setType(Material.CHEST);
                                chests.add(new Location(w, x, y + 2, z));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void fillChests(World w, int t, boolean refill) {
        if ((t == -296 && Main.day == 0) || refill == true) {
            for (int i = 0; i < chests.size(); i++) {
                if (w.getBlockAt((int)chests.get(i).getX(), (int)chests.get(i).getY() - 2, (int)chests.get(i).getZ()).getType() == Material.ENDER_PORTAL_FRAME) {
                    Chest chest = (Chest) w.getBlockAt((int)chests.get(i).getX(), (int)chests.get(i).getY(), (int)chests.get(i).getZ()).getState();
                    Inventory chestinv = chest.getBlockInventory();
                    for (int j = 0; j < r.nextInt(3) + 2; j++) {
                        int selector = r.nextInt(midLoot.length);
                        chestinv.setItem(r.nextInt(27), new ItemStack(midLoot[selector], r.nextInt(numOfMidLoot[selector]) + 1));
                    }
                }
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
