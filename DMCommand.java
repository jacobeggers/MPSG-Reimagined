package hungrygamespackage;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DMCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        GameStats.setDeathmatchTime(60);
        for (Player player: Bukkit.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 3, 1);
            player.sendMessage("§a§l" + sender.getName() + "§a§lhas initiated deathmatch!");
        }
        return true;
    }
}
