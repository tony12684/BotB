package io.github.tony12684.BotB;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.tony12684.BotB.Role.Affiliation;
import io.github.tony12684.BotB.Role.Team;
import net.md_5.bungee.api.ChatColor;
import io.github.tony12684.BotB.Roles.Imp;
import io.github.tony12684.BotB.Roles.Washerwoman;

public class Grimoire {
    private StorytellerPerformer storyteller;
    private Game game;
    /*
     * handles storing, updating, and displaying game information to players
     * prompts storyteller and players for inputs and actions
     * It is the grimoire's responsibility handle reminder tokens visibility
     */
    public Grimoire(StorytellerPerformer storyteller, Game game) {
        // Constructor for Grimoire class
        this.storyteller = storyteller;
        this.game = game;
    }

    public StorytellerPerformer getStoryteller() {
        return storyteller;
    }

    public List<Role> getBluffRoles() {
        // Build and return a list of Role objects representing available bluff roles
        List<Role> bluffRoles = new java.util.ArrayList<>();
        // TODO initiate bluff 
        bluffRoles.add(new Washerwoman(null));
        //TODO retrieve list of bluff roles from storyteller
        return bluffRoles;
    }

    public void minionInfo(List<PlayerPerformer> minions, PlayerPerformer demon) {
    // Send minion and demon information to each minion player
    //TODO upgrade this to use our UI when we find one
        for (PlayerPerformer minion : minions) {
            Player player = Bukkit.getPlayer(minion.getUUID());
            if (player != null && player.isOnline()) {
                StringBuilder info = new StringBuilder(ChatColor.DARK_RED + "Your fellow Minions are:\n");
                for (PlayerPerformer fellowMinion : minions) {
                    if (!fellowMinion.getUUID().equals(minion.getUUID())) {
                        Player fellowPlayer = Bukkit.getPlayer(fellowMinion.getUUID());
                        if (fellowPlayer != null) {
                            info.append("- ").append(fellowPlayer.getName()).append("\n");
                            //TODO modify this for nickname plugin support
                        }
                    }
                }
                player.sendMessage(info.toString());
            } else {
                game.crashGame("Minion player not found or offline in Grimoire.minionInfo(): " + minion.getUUID(), storyteller.getUUID());
            }
        }
    }
    
    public ActionLog demonInfo(PlayerPerformer trueDemon, PlayerPerformer falseDemon, List<PlayerPerformer> minions, List<Role> bluffRoles) {
        // if true demon and false demon then notify 
        // Send demon information to the demon player
        //TODO upgrade this to use our UI when we find one
        Player player = Bukkit.getPlayer(trueDemon.getUUID());
        if (player != null && player.isOnline()) {
            StringBuilder info = new StringBuilder(ChatColor.DARK_RED + "You are the Demon.\n");
            if (!minions.isEmpty()) {
                info.append("Your Minions are:\n");
                for (PlayerPerformer minion : minions) {
                    Player minionPlayer = Bukkit.getPlayer(minion.getUUID());
                    if (minionPlayer != null) {
                        info.append("- ").append(minionPlayer.getName()).append("\n");
                        //TODO modify this for nickname plugin support
                    }
                }
            } else {
                info.append("You have no Minions.\n");
                // This should never happen in a standard game
            }
            if (!bluffRoles.isEmpty()) {
                info.append("Available Bluff Roles:\n");
                for (Role bluff : bluffRoles) {
                    info.append("- ").append(bluff.getRoleNameActual()).append("\n");
                }
            } else {
                // This should never happen in a standard game
                info.append("No Bluff Roles available.\n");
            }
            player.sendMessage(info.toString());
        } else {
            game.crashGame("Demon player not found or offline in Grimoire.demonInfo(): " + trueDemon.getUUID(), storyteller.getUUID());
        }
        return null; // Placeholder return
    }

    public List<ActionLog> setupLoop(Game game) {
        // Method to perform setup for all roles in the game
        // For each player, call their setup() method and collect the resulting ActionLogs
        // allow for storyteller to iterate through completed setup actions and confirm completion
        // be very careful that no setup actions are skipped, no setup actions are double performed
        return null; // Placeholder return;
    }

    public List<ActionLog> firstNightSetupLoop(Game game) {
        // Method to perform first night setup for all roles in the game
        // For each player, call their firstNightSetupMode() method and collect the resulting ActionLogs
        // allow for storyteller to iterate through completed first night setup actions and confirm completion
        return null; // Placeholder return;
    }

    public boolean getFirstNightSetupMode() {
        // return true to prep the first night info for roles like chef, librarian, investigator, etc.
        //     that have no player input on first night
        if (game.getPlugin().debugMode) {
            Bukkit.getLogger().info("First night setup mode FALSE.");
        }
        return false; // Placeholder return;
    }

    public List<Role> buildRoleList(Game game, int numberOfPlayers) {
        // Method to build a list of roles based on the number of players
        List<Role> roles = new ArrayList<>();
        roles.add(new Imp(game.getPlayers().getFirst()));
        return roles; // Placeholder return;
    }

    public void basicMessage(Performer performer, String message) {
        // Method to show basic messages to a performer
    }

    public void errorMessage(Performer performer, String message) {
        // Method to show error messages to a performer
    }

    public List<PlayerPerformer> getFreeTargetsFromPerformer(Performer actingPerformer, int numberOfTargets, String message) {
        // Method to get some number of unrestricted targets from a performer
        return null; // Placeholder return;
    }

    public int getNumberFromPerformer(Performer performer, String promptMessage) {
        // Method to get a number input from a performer
        return 0; // Placeholder return;
    }

    public boolean getBooleanFromPerformer(Performer performer, String promptMessage) {
        // Method to get a boolean input from a performer
        return false; // Placeholder return;
    }

    public Team getTeamFromPerformer(Performer performer, String promptMessage) {
        // Method to get the team of a performer
        return null; // Placeholder return;
    }

    public Affiliation getAffiliationFromPerformer(Performer performer, String promptMessage) {
        // Method to get the affiliation of a performer
        return null; // Placeholder return;
    }
}
