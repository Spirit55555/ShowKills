package dk.spirit55555.showkills;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;

public class ShowKills extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(cmd.getName().equalsIgnoreCase("showkills") && sender instanceof Player))
			return false;
		
		if (args.length == 0 && sender.hasPermission("showkills.own")) {
			Player player = (Player) sender;

			int kills = getPlayerKills(player);
			
			String killsMessage = String.format(getConfig().getString("messages.own"), kills);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', killsMessage));
			
			return true;
		}
		
		else if (args.length == 1 && sender.hasPermission("showkills.other")) {
			@SuppressWarnings("deprecation")
			Player player = getServer().getPlayer(args[0]);
			
			if (player == null)
				return false;
			
			int kills = getPlayerKills(player);
			
			String killsMessage = String.format(getConfig().getString("messages.other"), player.getName(), kills);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', killsMessage));
			
			return true;
		}
		
		return false;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		int kills = getPlayerKills(event.getPlayer());
		String format = event.getFormat();
		
		format = format.replace("{KILLS}", Integer.toString(kills));
		event.setFormat(format);
	}
	
	@SuppressWarnings("deprecation")
	private int getPlayerKills(Player player) {
		int kills = 0;
		
		Objective objective = player.getScoreboard().getObjective(getConfig().getString("objective"));
		
		if (objective != null)
			kills = objective.getScore(player).getScore();
		
		return kills;
	}
}