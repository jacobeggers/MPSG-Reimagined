package hungrygamespackage;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Main.ticksSinceStarted == -500) {
            Main.ticksSinceStarted = -300;
            Main.beginGame();
            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                player.sendTitle("§eLoading...", "The game will start momentarily!");
            }
        } else {
            sender.sendMessage("The game has already started");
        }
        return true;
    }
}

