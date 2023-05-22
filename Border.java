package hungrygamespackage;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static java.lang.Math.round;

public class Border {
    public static void shrinkBorder(World w, int t) {
        if (t % 40 == 0) {
            w.getWorldBorder().setSize(w.getWorldBorder().getSize() - 0.1, 1);
        }
    }
    public static void setBorder(World w, int t, int size) {
        if (t == -295) {
            w.getWorldBorder().setSize(size);
        }
    }
    public static void playBorderSound(World w, int t) {
        if (t % 10 == 0) {
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                if ((w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getX()) < 30 &&
                        (w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getX()) > 15) {
                    player.playSound(player.getLocation(), Sound.PORTAL, (float)Math.abs(1 / (((Math.abs(player.getLocation().getX())) + 15) - w.getWorldBorder().getSize() / 2)), 2f);
                    //System.out.println((float)Math.abs(1 / (((player.getLocation().getX()) + 10) - w.getWorldBorder().getSize() / 2)));
                    //System.out.println();
                } else if ((w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getX()) <= 15) {
                    player.playSound(player.getLocation(), Sound.PORTAL, 1, 2f);
                }
                if ((w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getZ()) < 30 &&
                        (w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getZ()) > 15) {
                    player.playSound(player.getLocation(), Sound.PORTAL, (float)Math.abs(1 / (((Math.abs(player.getLocation().getZ())) + 15) - w.getWorldBorder().getSize() / 2)), 2f);
                    //System.out.println((float)Math.abs(1 / (((player.getLocation().getX()) + 10) - w.getWorldBorder().getSize() / 2)));
                    //System.out.println();
                } else if ((w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getZ()) <= 15) {
                    player.playSound(player.getLocation(), Sound.PORTAL, 1, 2f);
                }
            }
        }
    }
}
