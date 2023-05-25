package hungrygamespackage;

import com.sun.org.apache.bcel.internal.generic.LOR;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.LoginListener;
import net.minecraft.server.v1_8_R3.Vector3f;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.Chest;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {
    public static int ticksSinceStarted = -500;
    private static int ticksUntilReset = 200;
    private double percent = 0.001;
    public static int day = 0;
    private static Random r = new Random();
    public static boolean alerted = false;

    private static World world;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("dm").setExecutor(new DMCommand());
        this.getCommand("start").setExecutor(new StartCommand());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (ticksSinceStarted >= -300) {

                    if (ticksSinceStarted == -295) {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            GameStats.createScoreBoard(player);
                        }
                    }
                    if (ticksSinceStarted == -300) {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3, 1);
                        }
                    }
                    ticksSinceStarted++;
                    PlayerManager.forcePlayersAtSpawn(world, ticksSinceStarted);
                    PlayerManager.releasePlayersFromSpawn(world, ticksSinceStarted);
                    PlayerManager.setPlayerSpawn(world, new Location(world, -200, 10, -200), new Location(world, 200, 150, 200), ticksSinceStarted);
                    Chests.setChests(world, new Location(world, -200, 10, -200), new Location(world, 200, 86, 200), ticksSinceStarted);
                    Chests.removeChests(world, new Location(world, -200, 10, -200), new Location(world, 200, 86, 200), ticksSinceStarted);
                    Chests.fillChests(world, ticksSinceStarted, false);
                    SupplyDrop.setSupplyDrop(world, new Location(world, -200, 10, -200), new Location(world, 200, 86, 200), ticksSinceStarted);
                    GameStats.gameCountdown(ticksSinceStarted);
                    Border.setBorder(world, ticksSinceStarted, 400);

                    if (ticksSinceStarted > 0) {

                        GameStats.updateGameTime(world, ticksSinceStarted);
                        GameStats.updateChestTime(world, ticksSinceStarted);
                        GameStats.updateDeathmatchTime(world, ticksSinceStarted);
                        SupplyDrop.updateSupplyDrop(world, ticksSinceStarted);
                        Border.shrinkBorder(world, ticksSinceStarted);
                        Border.playBorderSound(world, ticksSinceStarted);
                        //Border.shootFire(world, ticksSinceStarted);
                        adjustDayLightCycle(world, ticksSinceStarted);

                        GameStats.updateDeathMatchCountDown(world, ticksSinceStarted);
                        GameStats.updateGameEnd(ticksSinceStarted);

                        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                            if (!PlayerManager.checkIfPlayerIsAlive(player)) {
                                player.setGameMode(GameMode.SPECTATOR);
                            }
                        }
                    }

                    if (alerted == false && PlayerManager.players.size() <= 4 && GameStats.deathmatchTime == 257) {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            world.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 1);
                            player.sendMessage("§a§lType §r§l/dm §a§lto initiate deathmatch");
                        }
                        alerted = true;
                    }

                    if (ticksSinceStarted >= 5100) {
                        ticksSinceStarted = 0;
                        day++;
                    }
                }

                if ((PlayerManager.players.size() == 0 && ticksSinceStarted > 0) || ticksSinceStarted == -499) {
                    System.out.println("weagawegaweg");
                    endGame();
                }
            }
        }, 1, 1);

    }

    public static void beginGame() {
        world = Bukkit.getServer().getWorld("world");
        world.setTime(0);
        world.setSpawnLocation(4, 101, -25);
        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
            player.setGameMode(GameMode.ADVENTURE);
        }
    }

    public static void endGame() {
        ticksSinceStarted = -499;
        if (ticksUntilReset == 0) {
            ticksSinceStarted = -500;
            ticksUntilReset = 200;
            GameStats.countDown = 15;
            GameStats.time = 0;
            GameStats.chestTime = 419;
            GameStats.deathmatchTime = 599;
            GameStats.deathmatchCountDown = -1;
            GameStats.gameEndTime = -1;
            day = 0;
            if (PlayerManager.players.size() > 0) {
                for (int i = PlayerManager.players.size() - 1; i >= 0; i++) {
                    PlayerManager.players.remove(i);
                }
            }
        } else {
            ticksUntilReset--;
            System.out.println(ticksUntilReset);
            int setFirework = r.nextInt(10);
            if (setFirework == 0) {
                Firework f = (Firework) world.spawnEntity(new Location(world, r.nextInt(50) - 25,r.nextInt(10) + 90,r.nextInt(50) - 50), EntityType.FIREWORK);
                FireworkMeta fwm = f.getFireworkMeta();
                fwm.addEffect(FireworkEffect.builder().withColor(Color.BLUE).trail(false).with(FireworkEffect.Type.BALL_LARGE).build());
                fwm.setPower(0);
                f.setFireworkMeta(fwm);
            }
        }
    }

    public void adjustDayLightCycle(World w, int t) {
        //System.out.println(w.getTime());
        if (t <= 3900 && t > 1) {
            if (t % 39 == 0) {
                w.setTime((int) (13500 * percent));
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
                w.setTime((int) (13500 + ((24050 - 13500) * percent)));
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

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        e.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 10));
        for (int i = 0; i < PlayerManager.players.size(); i++) {
            if (PlayerManager.players.get(i).getName().equals(PlayerManager.players.get(i).getName())) {
                PlayerManager.players.remove(i);
                GameStats.updateTributesLeft();
            }
        }
        for (int i = 0; i < 3; i++) {
            Firework f = (Firework) world.spawnEntity(new Location(world, e.getEntity().getLocation().getX() + r.nextDouble() - 0.5,
                            e.getEntity().getLocation().getY(),
                            e.getEntity().getLocation().getZ() + r.nextDouble() - 0.5),
                    EntityType.FIREWORK);
            FireworkMeta fwm = f.getFireworkMeta();
            fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).trail(false).with(FireworkEffect.Type.BALL_LARGE).build());
            fwm.setPower(3 + (i / 5));
            f.setFireworkMeta(fwm);
        }
    }
}