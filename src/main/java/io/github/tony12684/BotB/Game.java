package io.github.tony12684.BotB;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;



/*
 * This class is a placeholder for future game logic and management.
 */
public class Game extends JavaPlugin {
    private String startTime; // ISO-8601 format yyyy-MM-ddTHH:mm:ss
    private String endTime; // ISO-8601 format yyyy-MM-ddTHH:mm:ss
    private String gameState;// e.g. "setup", "daytime", "voting", "nighttime", "ended"
    private int dayCount; // Tracks what day it is in the game
    private StorytellerPerformer storyteller; // Performer object of the storyteller player
    private Grimoire grimoire; // Grimoire object for the storyteller
    private List<PlayerPerformer> players; // List of non storyteller PlayerPerformers
    private Team townsfolk; // Team object for Townsfolk
    private Team outsiders; // Team object for Outsiders
    private Team minions; // Team object for Minions
    private Team demons; // Team object for Demons
    private List<Role> publicRoles; // List of Role objects for available "bluff" roles
    
    public Game(String storytellerUUID, List<String> playerUUIDs) {
        // Constructor for Game class
        this.startTime = getTime();
        this.gameState = "setup";
        this.dayCount = 0;

        this.storyteller = new StorytellerPerformer(storytellerUUID, new Role("Storyteller"));
        this.grimoire = new Grimoire(storytellerUUID);

        for (String uuid : playerUUIDs) {
            players.add(new PlayerPerformer(uuid, null)); // Role to be assigned later
        }

        List<Role> roleList = getRoleList();
        //randomize role list and assign to players
        Collections.shuffle(roleList);
        assignRoles(players, roleList, grimoire.getStorytellerUUID());

        //get bluff roles for demon
        List<Role> bluffRoles = getBluffRoles();
        firstNight(); // Proceed to the first night phase
    }


    private String getTime() {
        return java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private List<Role> getRoleList() {
        // Build and return a list of Role objects representing available roles
        // Returned role list should account for team count modifiers e.g. +1 Townsfolk, -1 Outsider
        List<Role> roles = new java.util.ArrayList<>();
        //TODO retrieve list of roles from storyteller
        //TODO build automatic role list based on player count, validate with storyteller
        /*
        roles.add(new Role("Villager"));
        roles.add(new Role("Werewolf"));
        roles.add(new Role("Seer"));
        */
        return roles;
    }

    private void assignRoles(List<PlayerPerformer> players, List<Role> roleList, String storytellerUUID) {
        //TODO build so this can be run multiple times if needed
        //TODO assign teams based on roles
        for (PlayerPerformer player : players) {
            if (!roleList.isEmpty()) {
                player.setRole(roleList.remove(0));
            } else {
                crashGame("Not enough roles for players!", storytellerUUID);
            }
        }
        if (!roleList.isEmpty()) {crashGame("Unassigned roles remain!", storytellerUUID);}
        updateGrimoire(players, storytellerUUID);
    }

    private List<Role> getBluffRoles() {
        // Build and return a list of Role objects representing available bluff roles
        List<Role> bluffRoles = new java.util.ArrayList<>();
        //TODO retrieve list of bluff roles from storyteller
        /*
        bluffRoles.add(new Role("Minion"));
        bluffRoles.add(new Role("Demon"));
        */
        return bluffRoles;
    }

    private void updateGrimoire(List<PlayerPerformer> players, String storytellerUUID) {
        // Update the storyteller's grimoire with current game information
        //TODO build this
    }

    private void crashGame(String reason, String storytellerUUID) {
        // Handle game crash scenario
        // TODO build this
        getLogger().warning("Game crashed: " + reason);
        Bukkit.getPlayer(storytellerUUID).sendMessage(ChatColor.RED + reason);
    }
}
