package io.github.tony12684.BotB;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBOTBStartGame implements CommandExecutor{
    //Called when someone uses that /startgame command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player storyteller = (Player) sender;
        String storytellerUUID = storyteller.getUniqueId().toString();
        if (sender instanceof Player) {
            storytellerUUID = storyteller.getUniqueId().toString();
        } else {
            sender.sendMessage("Only players can start a game.");
            return false;
        }
        // Start a new game with the specified storyteller UUID
        java.util.List<String> playerUUIDs = new java.util.ArrayList<>();
        for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
            playerUUIDs.add(p.getUniqueId().toString());
        }
        playerUUIDs.remove(storytellerUUID); //remove storyteller from player list
        // get instance of Main plugin
        Main plugin = Main.getPlugin(Main.class);
        Game game = new Game(plugin, storytellerUUID, playerUUIDs);
        sender.sendMessage("Game starting with you as the storyteller!");
        return true;
    }
}
