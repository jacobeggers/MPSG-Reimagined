package hungrygamespackage;

import com.sun.org.apache.bcel.internal.generic.LOR;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.LoginListener;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Main extends JavaPlugin implements Listener {
    public int ticksSinceStarted = -300;
    private double percent = 0.001;
    public static int day = 0;

    World world = Bukkit.getWorld("world");


    @Override
    public void onEnable() {
        world.setTime(0);
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("dm").setExecutor(new DMCommand());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (ticksSinceStarted >= -300) {
                    if (ticksSinceStarted <= 0) {
                        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                            GameStats.createScoreBoard(player);
                        }
                    }
                    if (ticksSinceStarted == -300) {
                        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3, 1);
                        }
                    }
                    ticksSinceStarted++;
                    Chests.setChests(world, new Location(world, -200, 25, -200), new Location(world, 200, 150, 200), ticksSinceStarted);
                    Chests.removeChests(world, new Location(world, -200, 25, -200), new Location(world, 200, 150, 200), ticksSinceStarted);
                    Chests.fillChests(world, ticksSinceStarted, false);
                    SupplyDrop.setSupplyDrop(world, new Location(world, -200, 25, -200), new Location(world, 200, 150, 200), ticksSinceStarted);
                    GameStats.gameCountdown(ticksSinceStarted);
                    Border.setBorder(world, ticksSinceStarted, 250);
                }
                if (ticksSinceStarted > 0) {

                    GameStats.updateGameTime(world, ticksSinceStarted);
                    GameStats.updateChestTime(world, ticksSinceStarted);
                    GameStats.updateDeathmatchTime(world, ticksSinceStarted);
                    SupplyDrop.updateSupplyDrop(world, ticksSinceStarted);
                    Border.shrinkBorder(world, ticksSinceStarted);
                    Border.playBorderSound(world, ticksSinceStarted);
                    adjustDayLightCycle(world, ticksSinceStarted);
                }

                if (ticksSinceStarted >= 5100) {
                    ticksSinceStarted = 0;
                    day++;
                }

            }
        }, 1, 1);

    }

    public void adjustDayLightCycle(World w, int t) {
        //System.out.println(w.getTime());
        if (t <= 3900 && t > 1) {
            if (t % 39 == 0) {
                w.setTime((int)(13500 * percent));
                percent += 0.01;
            } else if (t % 2 == 0) {
                w.setTime(w.getTime() + 4);
            } else {
                w.setTime(w.getTime() + 3);
            }
        }
        if (t == 3900) {
            percent = 0;
        }
        if (t > 3900) {
            if (t % 12 == 0) {
                w.setTime((int)(13500 + ((24050 - 13500) * percent)));
                percent += 0.01;
            } else if (t % 2 == 0) {
                w.setTime(w.getTime() + 17);
            }
        }
        if (t == 1) {
            w.setTime(0);
            percent = 0;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        GameStats.createScoreBoard(e.getPlayer());
        if (e.getPlayer().getName().equals("L0rdthepvp")) {
            e.getPlayer().setPlayerListName("§c§lADMIN §r" + e.getPlayer().getName());
        }
    }

}