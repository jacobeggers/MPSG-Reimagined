package hungrygamespackage;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class SupplyDrop {
    private static Random r = new Random();
    private static ArrayList<Integer> locX = new ArrayList<Integer>();
    private static ArrayList<Integer> locY = new ArrayList<Integer>();
    private static ArrayList<Integer> locZ = new ArrayList<Integer>();
    private static Material[] supplyDropLoot = {Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_BOOTS,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.IRON_SWORD,
            Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET};


    private static int select = 0;
    private static int fireWorkY = -1;
    private static boolean dropping = false;
    private static int dropTime = 500;

    public static void supplyDrop(World w, Location sl, Location el, int t) {
        if (t == 3 && Main.day == 0) {
            if (locX.size() > 0) {
                for (int i = locX.size() - 1; i >= 0; i--) {
                    locX.remove(i);
                    locY.remove(i);
                    locZ.remove(i);
                }
            }
            for (double x = sl.getX(); x <= el.getX(); x++) {
                for (double y = sl.getY(); y <= el.getY(); y++) {
                    for (double z = sl.getZ(); z <= el.getZ(); z++) {
                        if (w.getBlockAt((int) x, (int) y, (int) z).getType() == Material.GLASS) {
                            if (w.getBlockAt((int) x, (int) y - 1, (int) z).getType() == Material.IRON_BLOCK) {
                                locX.add((int) x);
                                locY.add((int) y);
                                locZ.add((int) z);

                            }
                        }
                    }
                }
            }
        }
        if (t == 3900) {
            select = r.nextInt(locX.size());
            fireWorkY = locY.get(select) + 150;
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage("§e§lSupply Drop Incoming (§f§l" + (locX.get(select) + 1) + "§e§l,§f§l " + (locY.get(select) + 1) + "§e§l,§f§l " + (locZ.get(select) + 1) + "§e§l)");
            w.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 1);
            }
            w.getBlockAt(locX.get(select), locY.get(select), locZ.get(select)).setType(Material.BEACON);
        }

        if (t == 5100) {
            dropping = true;
        }
        if (t % 2 == 0) {
            if (dropping == true && fireWorkY > locY.get(select) + 2) {
                Firework f = (Firework) w.spawnEntity(new Location(w, (locX.get(select) + 0.5), fireWorkY, (locZ.get(select)) + 0.5), EntityType.FIREWORK);
                FireworkMeta fwm = f.getFireworkMeta();
                fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).trail(false).with(FireworkEffect.Type.BURST).build());
                try {
                    Field fe = fwm.getClass().getDeclaredField("power");
                    fe.setAccessible(true);
                    fe.set(fwm, -1);
                } catch (IllegalAccessException e) {
                    System.out.println("failed for some reason lol");
                } catch (NoSuchFieldException e) {
                    System.out.println("Failed for another reason lol");
                }
                f.setFireworkMeta(fwm);
                fireWorkY--;
            } else if (dropping == true) {
                w.getBlockAt(locX.get(select), locY.get(select), locZ.get(select)).setType(Material.GLASS);
                w.getBlockAt(locX.get(select), locY.get(select) + 1, locZ.get(select)).setType(Material.CHEST);
                Chest chest = (Chest) w.getBlockAt(locX.get(select), locY.get(select) + 1, locZ.get(select)).getState();
                Inventory chestInv = chest.getBlockInventory();
                int numberOfItems = r.nextInt(2) + 2;
                for (int i = 0; i < numberOfItems; i++) {

                    chestInv.setItem(r.nextInt(27), new ItemStack(supplyDropLoot[r.nextInt(supplyDropLoot.length)], 1));
                }
                chest.update();
                fireWorkY = -1;
                dropping = false;
                locX.remove(select);
                locY.remove(select);
                locZ.remove(select);
                select = 0;
            }
        }
    }
}
