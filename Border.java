package hungrygamespackage;

import net.minecraft.server.v1_8_R3.EntityFireball;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static java.lang.Math.round;

public class Border {
    public static void shrinkBorder(World w, int t) {
        if (t % 40 == 0 && GameStats.deathmatchTime > 0 && GameStats.gameEndTime == -1) {
            w.getWorldBorder().setSize(w.getWorldBorder().getSize() - 1.132, 1);
        } else if (t % 40 == 0 && GameStats.gameEndTime <= 120 && w.getWorldBorder().getSize() > 8) {
            w.getWorldBorder().setSize(w.getWorldBorder().getSize() - 1, 1);
        }
    }
    public static void setBorder(World w, int t, int size) {
        if (t == -295 || GameStats.deathmatchCountDown == 10) {
            w.getWorldBorder().setSize(size);
        }
    }
    public static void playBorderSound(World w, int t) {
        if (t % 10 == 0 && GameStats.deathmatchCountDown == -1 && GameStats.gameEndTime == -1) {
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                if ((w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getX() - 4) < 30 &&
                        (w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getX() - 4) > 15) {
                    player.playSound(player.getLocation(), Sound.PORTAL, (float)Math.abs(1 / (((Math.abs(player.getLocation().getX() - 4)) + 15) - (w.getWorldBorder().getSize()) / 2)), 2f);
                    //System.out.println((float)Math.abs(1 / (((player.getLocation().getX()) + 10) - w.getWorldBorder().getSize() / 2)));
                    //System.out.println();
                } else if ((w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getX() - 4) <= 15) {
                    player.playSound(player.getLocation(), Sound.PORTAL, 1, 2f);
                }

            }
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                if ((w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getZ() + 25) < 30 &&
                        (w.getWorldBorder().getSize() / 2 ) - Math.abs(player.getLocation().getZ() + 25) > 15) {
                    player.playSound(player.getLocation(), Sound.PORTAL, (float)Math.abs(1 / (((Math.abs(player.getLocation().getZ() + 25)) + 15) - (w.getWorldBorder().getSize()) / 2)), 2f);
                    System.out.println();
                    //System.out.println(Math.abs(player.getLocation().getZ() + 25));
                    //System.out.println(Math.abs(w.getWorldBorder().getSize() / 2));
                    //System.out.println();
                } else if ((w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getZ() + 25) <= 15) {
                    player.playSound(player.getLocation(), Sound.PORTAL, 1, 2f);
                }

            }
        }
    }

    public static void shootFire(World w, int t) {
        if (t % 10 == 0) {
            if (GameStats.gameEndTime == -1 && GameStats.deathmatchCountDown == -1) {
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    if ((w.getWorldBorder().getSize() / 2) - Math.abs(player.getLocation().getX() - 4) <= 15) {
                        BorderFireBall fb = new BorderFireBall(player.getLocation(), w.getWorldBorder().getSize(), -4, 'x');
                        //System.out.println(fb.summonFireBall().getX() + "," + fb.summonFireBall().getY() + "," + fb.summonFireBall().getX());
                        Entity f = w.spawnEntity(fb.summonFireBall(), EntityType.PRIMED_TNT);
                        f.setVelocity(new Vector(-0.5, 0, 0));
                    }
                }
            }

        }
    }
}
