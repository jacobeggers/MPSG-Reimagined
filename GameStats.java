package hungrygamespackage;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;
import org.bukkit.entity.Player;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.round;

public class GameStats implements Listener {
    private static Objective objective;
    private static Random r = new Random();
    private static double time = 0;
    private static int playersAlive = Bukkit.getServer().getOnlinePlayers().size();
    private static double chestTime = 419;
    private static double deathmatchTime = 599;
    private static Score[] scores = new Score[16];
    private static String[] values = {"§a§lTime", (int)time + " Seconds", "§e§lTributes", "" + playersAlive, "§6§lChest Refill", (double) round(((chestTime - 1) / 60) * 10) / 10 + " Minutes "};
    private static int[] pos = {14, 13, 11, 10, 8, 7};
    private static int countDown = 15;

    public static void gameCountdown(int t) {
        if (t % 20 == 0 && countDown > 0) {
            countDown--;
            if (countDown <= 15 && countDown > 0) {
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.NOTE_STICKS, 3, 1);
                    player.sendTitle("§e" + countDown, "Prepare to fight!");
                }
            }
            if (countDown == 0) {
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.NOTE_STICKS, 3, 1.5f);
                    player.sendTitle("§6FIGHT!", "The game has begun!");
                }
            }
        }
    }
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
        if (t % 20 == 0) {
            objective.getScoreboard().resetScores("0 Seconds");
            time++;
            if (time < 60) {
                objective.getScoreboard().resetScores((int)(time - 1) + " Seconds");
                scores[1] = objective.getScore((int)time + " Seconds");
                scores[1].setScore(13);
            } else {
                objective.getScoreboard().resetScores((int)(time - 1) + " Seconds");
                objective.getScoreboard().resetScores((double) round(((time - 1) / 60) * 10) / 10 + " Minutes");
                scores[1] = objective.getScore((double)round(((time) / 60) * 10) / 10 + " Minutes");
                scores[1].setScore(13);
            }

        }
    }

    public static void updateChestTime(World w, int t) {
        if (t % 20 == 0) {
            objective.getScoreboard().resetScores("7.0 Minutes ");
            if (chestTime > 0) {
                chestTime--;
            }
            if (chestTime < 60 && chestTime >= 0) {
                objective.getScoreboard().resetScores((double) round(((chestTime + 1) / 60) * 10) / 10 + " Minutes ");
                objective.getScoreboard().resetScores((int)(chestTime + 1) + " Seconds ");
                scores[5] = objective.getScore((int)chestTime + " Seconds ");
                scores[5].setScore(7);
            } else if (chestTime >= 0) {
                objective.getScoreboard().resetScores((double) round(((chestTime + 1) / 60) * 10) / 10 + " Minutes ");
                scores[5] = objective.getScore((double) round(((chestTime) / 60) * 10) / 10 + " Minutes ");
                scores[5].setScore(7);
            }

            if (chestTime / 60 <= 5 && chestTime / 60 > 0 && chestTime % 60 == 0) {
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    player.sendMessage("§6§lThe chests will be refilled in " + (int)(chestTime / 60) + " minutes");
                }
            }
        }
        if (chestTime == 0) {
            Chests.fillChests(w, t, true);
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.IRONGOLEM_DEATH, 3, 0.5f);
                player.sendMessage("§6§lThe chests have been refilled!");
            }
            chestTime = -1;
        }
        if (chestTime == -1) {
            objective.getScoreboard().resetScores(scores[5].getEntry());
        }
    }

    public static void updateDeathmatchTime(World w, int t) {
        if (t % 20 == 0) {
            if (deathmatchTime > 0) {
                deathmatchTime--;
            }
            if (deathmatchTime <= 180) {

                chestTime = -1;
                objective.getScoreboard().resetScores("§6§lChest Refill");
                scores[4] = objective.getScore("§c§lDeathmatch");
                scores[4].setScore(8);
                if (deathmatchTime <= 60 && deathmatchTime >= 0) {
                    objective.getScoreboard().resetScores((double) round(((deathmatchTime + 1) / 60) * 10) / 10 + " Minutes ");
                    objective.getScoreboard().resetScores((int)(deathmatchTime + 1) + " Seconds ");
                    scores[6] = objective.getScore((int)deathmatchTime + " Seconds ");
                    scores[6].setScore(7);
                } else if (deathmatchTime >= 0) {
                    objective.getScoreboard().resetScores((double) round(((deathmatchTime + 1) / 60) * 10) / 10 + " Minutes ");
                    scores[6] = objective.getScore((double) round(((deathmatchTime) / 60) * 10) / 10 + " Minutes ");
                    scores[6].setScore(7);
                }
            }
            if (deathmatchTime == 60) {
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    player.sendMessage("§a§lDeathmatch starting in " + (int)deathmatchTime + " seconds...");
                }
            }
            if (deathmatchTime == 30) {
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    w.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 1);
                    player.sendMessage("§a§lDeathmatch starting in " + (int)deathmatchTime + " seconds...");
                }
            }
            if (deathmatchTime == 10) {
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    w.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 1);
                    player.sendMessage("§a§lDeathmatch starting in " + (int)deathmatchTime + " seconds...");
                }
            }
            if (deathmatchTime <= 5 && deathmatchTime > 0) {
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    w.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 1);
                    player.sendMessage("§a§lDeathmatch starting in " + (int)deathmatchTime + " seconds...");
                }
            }
            if (deathmatchTime == 0) {
                objective.getScoreboard().resetScores((int)(deathmatchTime) + " Seconds ");
                deathmatchTime = -1;
            }
        }
    }

    public static void setDeathmatchTime(int time) {
        deathmatchTime = time;
    }
}
