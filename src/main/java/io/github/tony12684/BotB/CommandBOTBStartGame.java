package io.github.tony12684.BotB;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandBOTBStartGame implements CommandExecutor{
    //Called when someone uses that /startgame command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // assign command sender as storyteller
        Player storyteller = (Player) sender;
        UUID storytellerUUID;
        if (sender instanceof Player) {
            storytellerUUID = storyteller.getUniqueId();
        } else {
            sender.sendMessage("Only players can start a game.");
            return false;
        }
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        // de op everyone
        Bukkit.dispatchCommand(console, "deop @a");
        // op storyteller
        Bukkit.dispatchCommand(console, "op " + storyteller.getName());

        
        // Start a new game with the specified storyteller UUID
        List<UUID> playerUUIDs = new java.util.ArrayList<>();
        for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
            // TODO remove logging
            org.bukkit.Bukkit.getLogger().info("Adding player UUID: " + p.getUniqueId().toString());
            playerUUIDs.add(p.getUniqueId());
        }
        // TODO remove logging
        Bukkit.getLogger().info("Removing storyteller UUID: " + storytellerUUID);
        Bukkit.getPlayer(storytellerUUID).sendMessage("Storyteller UUID: " + storytellerUUID);
        playerUUIDs.remove(storytellerUUID); //remove storyteller from player list
        sender.sendMessage("Game starting with you as the storyteller!");
        // get instance of Main plugin
        Main plugin = Main.getPlugin(Main.class);
        // start the game
        new Game(plugin, storytellerUUID, playerUUIDs);
        return true;
    }
}
