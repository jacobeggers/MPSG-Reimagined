package hungrygamespackage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.round;

public class GameStats implements Listener {
    private static Objective objective;
    private static Random r = new Random();
    private static double time = 0;
    private static int playersAlive = Bukkit.getServer().getOnlinePlayers().size();
    private static double chestTime = 7;
    private static Score[] scores = new Score[16];
    private static String[] values = {"§a§lTime", (int)time + " Seconds", "§e§lTributes", "" + playersAlive, "§6§lChest Refill", chestTime + " Minutes "};
    private static int[] pos = {14, 13, 11, 10, 8, 7};

    public static void createScoreBoard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        objective = board.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lBADPLEX");
        initiateScoreboard();
        player.setScoreboard(board);
    }

    public static void initiateScoreboard() {
        int count = 0;
        for (int i = 15; i >= 7; i--) {
            if (i == pos[count]) {
                System.out.println(pos.length);
                scores[i] = objective.getScore(values[count]);
                scores[i].setScore(i);
                count++;
            } else {
                String filler = "";
                for (int j = 0; j < i; j++) {
                    filler += " ";
                }
                scores[i] = objective.getScore(filler);
                scores[i].setScore(i);
            }
        }
    }

    public static void updateGameTime(World w, int t) {
        if (t % 20 == 0 && t < 1200 && Main.day == 0) {
                objective.getScoreboard().resetScores((int) time + " Seconds");
                time = t / 20;
                scores[2] = objective.getScore((int) time + " Seconds");
                scores[2].setScore(13);
                objective.getScoreboard().resetScores(0 + " Seconds");

        } else if (t == 1200 && Main.day == 0) {
            objective.getScoreboard().resetScores((int) time + " Seconds");
            time = 1.0;
            scores[2] = objective.getScore(time + " Minutes");
            scores[2].setScore(13);
        } else if (t % 120 == 0) {
            objective.getScoreboard().resetScores(0 + " Seconds");
            objective.getScoreboard().resetScores(time + " Minutes");
            time += 0.1;
            time = round(10 * time);
            time /= 10;
            scores[2] = objective.getScore(time + " Minutes");
            scores[2].setScore(13);
        }
    }

    public static void updateChestTime(World w, int t) {
        if (t == 2100 && Main.day == 1) {
            objective.getScoreboard().resetScores(chestTime + " Minutes ");
            chestTime = 60;
        } else if ((t > 2100 && t < 3300 && Main.day == 1) || (t >= 600 && Main.day == 2)) {
            if (t % 20 == 0) {
                objective.getScoreboard().resetScores((int)chestTime + " Seconds ");
                chestTime--;
                scores[2] = objective.getScore((int)chestTime + " Seconds ");
                scores[2].setScore(7);
            }
        } else if (t % 120 == 0) {
            objective.getScoreboard().resetScores("1 Seconds ");
            objective.getScoreboard().resetScores(chestTime + " Minutes ");
            chestTime -= 0.1;
            chestTime = round(10 * chestTime);
            chestTime /= 10;
            scores[2] = objective.getScore(chestTime + " Minutes ");
            scores[2].setScore(7);
        }
    }

    public static void updateDeathmatchTime(World w, int t) {
        if (t == 3300 && Main.day == 1) {
            objective.getScoreboard().resetScores("1 Seconds ");
            objective.getScoreboard().resetScores("§6§lChest Refill");
            scores[2] = objective.getScore("§c§lDeathmatch");
            scores[2].setScore(8);
            chestTime = 2.5;
        }
    }
}
