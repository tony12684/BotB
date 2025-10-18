package io.github.tony12684.BotB;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.protobuf.Message;

import net.md_5.bungee.api.ChatColor;



/*
 * This class is a placeholder for future game logic and management.
 */
public class Game extends JavaPlugin {
    private String startTime; // ISO-8601 format yyyy-MM-ddTHH:mm:ss
    private String gameState;// e.g. "setup", "daytime", "voting", "nighttime", "ended"
    private int dayCount; // Tracks what day it is in the game
    private StorytellerPerformer storyteller; // Performer object of the storyteller player
    private Grimoire grimoire; // Grimoire object for the storyteller
    private List<PlayerPerformer> players; // List of non storyteller PlayerPerformers
    private List<Role> publicRoles; // List of Role objects for available "bluff" roles
    
    public Game(String storytellerUUID, List<String> playerUUIDs) {
        // Constructor for Game class
        this.startTime = getTime();
        this.gameState = "setup";
        this.dayCount = 0;

        this.storyteller = new StorytellerPerformer(storytellerUUID, new Role("Storyteller", "Storyteller"));
        this.grimoire = new Grimoire(storytellerUUID);

        for (String uuid : playerUUIDs) {
            players.add(new PlayerPerformer(uuid, null)); // Role to be assigned later
        }

        //TODO probably move all this to Game.startGame()
        assignSeats(players);

        List<Role> roleList = getRoleList();
        assignRoles(players, roleList, storyteller.getUUID());

        //TODO log game starttime and player info to DB

        firstNight(players); // Proceed to the first night phase
    }


    private String getTime() {
        return java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public List<PlayerPerformer> getPlayers() {
        // Return a copy of the players list to prevent external modification
        List<PlayerPerformer> copy = new ArrayList<>();
        for (PlayerPerformer player : players) {
            copy.add(player);
        }
        return copy;
    }

    public PlayerPerformer getPlayerByRole(String roleName) {
        for (PlayerPerformer player : players) {
            if (player.getRole().getRoleName().equals(roleName)) {
                return player;
            }
        }
        return null; // Player with specified role not found
    }

    public StorytellerPerformer getStoryteller() {
        // Return a copy of the storyteller performer to prevent external modification
        StorytellerPerformer storyteller = new StorytellerPerformer(this.storyteller.getUUID(), this.storyteller.getRole());
        return storyteller;
    }

    private void sortPlayersByActionPriority(List<PlayerPerformer> players) {
        // Sort players based on their role's action priority for nighttime actions
        //TODO implement this
    }

    public List<PlayerPerformer> sortPlayersBySeatOrder(List<PlayerPerformer> players) {
        List<PlayerPerformer> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((p1, p2) -> Integer.compare(p1.getSeat(), p2.getSeat()));
        return sortedPlayers;
    }

    private void assignSeats(List<PlayerPerformer> players) {
        // Randomly assign seats to players
        Collections.shuffle(players);
        for (PlayerPerformer player : players) {
            player.setSeat(players.indexOf(player) + 1); // Seats numbered from 1
        } // Unless players list is shuffled, order should match seat number.
        //TODO validate with storyteller
        //TODO manual seat assignment option
    }

    private List<Role> getRoleList() {
        // Build and return a list of Role objects representing available roles
        // Returned role list should account for team count modifiers e.g. +1 Townsfolk, -1 Outsider
        List<Role> roles = new ArrayList<>();
        //TODO build gui for role list selection
        //TODO retrieve list of roles from storyteller
        //TODO build automatic role list based on player count, validate with storyteller
        return roles;
    }

    private void assignRoles(List<PlayerPerformer> players, List<Role> roleList, String storytellerUUID) {
        //TODO build to require storyteller validation
        // Randomly assign roles to players
        Collections.shuffle(roleList);
        for (PlayerPerformer player : players) {
            if (!roleList.isEmpty()) {
                player.setRole(roleList.remove(0));
            } else {
                crashGame("Not enough roles for players!", storytellerUUID);
            }
        }
        if (!roleList.isEmpty()) {crashGame("Unassigned roles remain!", storytellerUUID);}
        // TODO validate with storyteller
        for (PlayerPerformer player : players) {
            player.getRole().setup(this);
        }
        updateGrimoire(players, storytellerUUID);
    }

    private List<Role> getBluffRoles() {
        // Build and return a list of Role objects representing available bluff roles
        List<Role> bluffRoles = new ArrayList<>();
        //TODO retrieve list of bluff roles from storyteller
        return bluffRoles;
    }

    private List<PlayerPerformer> getAllMinions(List<PlayerPerformer> players) {
        List<PlayerPerformer> minions = new ArrayList<>();
        for (PlayerPerformer player : players) {
            if (player.getRole().getTeam().equals("Minion")) {
                minions.add(player);
            }
        }
        return minions;
    }

    private PlayerPerformer getDemon(List<PlayerPerformer> players) {
        for (PlayerPerformer player : players) {
            if (player.getRole().getTeam().equals("Demon")) {
                return player;
            }
        }
        crashGame("No demon found in Game.getDemon()!", storyteller.getUUID());
        return null; // This line should never be reached
    }

    private void firstNight(List<PlayerPerformer> players) {
        // Handle the first night phase of the game
        this.gameState = "nighttime";
        this.dayCount = 1;
        //TODO build this
        notifyPlayersOfRoles(players);
        List<PlayerPerformer> minions = getAllMinions(players);
        PlayerPerformer demon = getDemon(players);
        minionInfo(minions, demon);
        List<Role> bluffs = getBluffRoles();
        demonInfo(demon, minions, bluffs);
        sortPlayersByActionPriority(players);
        for (PlayerPerformer player : players) {
            if (player.isDrunk() || player.isPoisoned()) {
                //TODO implement drunk and poisoned logic
            } else {
                try {
                    player.getRole().firstNightAction(this);
                } catch (Exception e) {
                    // Catch any exceptions thrown during the first night action
                    crashGame("Error during first night action for player " + player.getUUID() + ": " + e.getMessage(), storyteller.getUUID());
                }
            }
        }
    }

    
    private void notifyPlayersOfRoles(List<PlayerPerformer> players) {
        // Notify each player of their assigned role
        //TODO upgrade this to use our UI when we find one
        for (PlayerPerformer player : players) {
            Player bukkitPlayer = Bukkit.getPlayer(player.getUUID());
            if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
                StringBuilder message;
                if (player.getRole().getFalseRole() != null) { // If the role has a false role, show that instead
                    message = new StringBuilder(ChatColor.GRAY + "You are the " + player.getRole().getFalseRole().getRoleName() + ".\n");
                    message.append("Your team is: ").append(player.getRole().getFalseRole().getTeam()).append("\n");
                    message.append(player.getRole().getFalseRole().getStartingMessage());
                } else {
                    message = new StringBuilder(ChatColor.GRAY + "You are the " + player.getRole().getRoleName() + ".\n");
                    message.append("Your team is: ").append(player.getRole().getTeam()).append("\n");
                    message.append(player.getRole().getStartingMessage());
                }
                bukkitPlayer.sendMessage(message.toString());
            } else {
                crashGame("Player not found or offline in Game.notifyPlayersOfRoles(): " + player.getUUID(), storyteller.getUUID());
            }
        }
    }

    private void minionInfo(List<PlayerPerformer> minions, PlayerPerformer demon) {
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
                crashGame("Minion player not found or offline in Game.minionInfo(): " + minion.getUUID(), storyteller.getUUID());
            }
        }
    }

    private void demonInfo(PlayerPerformer demon, List<PlayerPerformer> minions, List<Role> bluffRoles) {
        // Send demon information to the demon player
        //TODO upgrade this to use our UI when we find one
        Player player = Bukkit.getPlayer(demon.getUUID());
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
                    info.append("- ").append(bluff.getRoleName()).append("\n");
                }
            } else {
                info.append("No Bluff Roles available.\n");
            }
            player.sendMessage(info.toString());
        } else {
            crashGame("Demon player not found or offline in Game.demonInfo(): " + demon.getUUID(), storyteller.getUUID());
        }
    }

    private void updateGrimoire(List<PlayerPerformer> players, String storytellerUUID) {
        // Update the storyteller's grimoire with current game information
        //TODO build this
    }

    public void crashGame(String reason, String storytellerUUID) {
        // Handle game crash scenario
        // TODO build a proper crash handler that effects game state without server reboot
        // TODO handle exceptions to try to keep the game running if possible
        getLogger().warning("Game crashed: " + reason);
        Bukkit.getPlayer(storytellerUUID).sendMessage(ChatColor.RED + reason);
    }
}
