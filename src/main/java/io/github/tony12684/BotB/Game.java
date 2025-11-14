package io.github.tony12684.BotB;
import io.github.tony12684.BotB.Role.Affiliation;
import io.github.tony12684.BotB.Roles.Storyteller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Game {
    // Game class to manage the state and logic of a Blood on the Blocktower game
    private String gameState;// e.g. "setup", "daytime", "voting", "nighttime", "ended"
    private int dayCount; // Tracks what day it is in the game
    private StorytellerPerformer storyteller; // Performer object of the storyteller player
    private Grimoire grimoire; // Grimoire object for the storyteller
    private List<PlayerPerformer> players; // List of non storyteller PlayerPerformers
    private Main plugin;
    private int gameId;
    
    public Game(Main plugin, String storytellerUUID, List<String> playerUUIDs) {
        //Constructor for Game class
        //Responsible for game setup sequencing

        this.gameState = "setup";
        this.dayCount = 0;
        this.plugin = plugin;
        try {
            this.gameId = plugin.insertGameStartToDB();
            Bukkit.getLogger().info("Game started with ID: " + gameId);
        } catch (Exception e) {
            crashGame("Database error on game start: " + e.getMessage(), storytellerUUID);
        }

        // TODO update this to accept fabled storytellers
        this.storyteller = new StorytellerPerformer(storytellerUUID, new Storyteller(), Bukkit.getPlayer(storytellerUUID).getName());
        this.grimoire = new Grimoire(storyteller, this);


        // Build players list
        for (String uuid : playerUUIDs) {
            players.add(new PlayerPerformer(uuid, null, Bukkit.getPlayer(uuid).getName())); // Role to be assigned later
            // TODO adjust for nickname plugin support
        }
        // TODO log players to game_users

        //TODO probably move all this to Game.startGame()
        assignSeats(players);
        linkNeighbors(players);

        List<Role> roleList = grimoire.buildRoleList(players.size());
        assignRoles(players, roleList);

        // setupPhase();

        firstNight(players); // Proceed to the first night phase
        //TODO build daytime, voting and subsequent night phases loop
        //TODO build drunk false role handling into Game class action logic

    }

    private int saveGameStart() {
        //Add a new game entry to the database and return the game ID
        
        return 0;
    }

    public List<PlayerPerformer> getPlayers() {
        // Return a copy of the players list to prevent external modification
        // TODO pass the actual list reference?
        List<PlayerPerformer> copy = new ArrayList<>();
        for (PlayerPerformer player : players) {
            copy.add(player);
        }
        return copy;
    }

    public Grimoire getGrimoire() {
        return grimoire;
    }

    public PlayerPerformer getPlayerByRole(String roleName) {
        // Expects that only one player has the specified role
        // TODO make this more efficent with a map?
        for (PlayerPerformer player : players) {
            if (player.getRole().getRoleNameActual().equals(roleName)) {
                return player;
            }
        }
        return null; // Player with specified role not found
    }

    public StorytellerPerformer getStoryteller() {
        // Return the storyteller performer
        // aliasing not an issue since StorytellerPerformer is final
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

    private void linkNeighbors(List<PlayerPerformer> players) {
        // Link each player to their left and right neighbors based on seat order
        List<PlayerPerformer> sortedPlayers = sortPlayersBySeatOrder(players);
        int playerCount = sortedPlayers.size();
        for (int i = 0; i < playerCount; i++) {
            PlayerPerformer currentPlayer = sortedPlayers.get(i);
            PlayerPerformer rightNeighbor;
            PlayerPerformer leftNeighbor;
            // first persons right neighbor is last person
            if (i == 0) {
                rightNeighbor = players.get(playerCount - 1);
            } else {
                rightNeighbor = players.get(i - 1);
            }
            // last persons left neighbor is first person
            if (i == playerCount - 1) {
                leftNeighbor = players.get(0);
            } else {
                leftNeighbor = players.get(i + 1);
            }
            currentPlayer.setRightNeighbor(rightNeighbor);
            currentPlayer.setLeftNeighbor(leftNeighbor);
        }
    }

    private void assignRoles(List<PlayerPerformer> players, List<Role> roleList) {
        //TODO build to require storyteller validation
        // Randomly assign roles to players
        Collections.shuffle(roleList);
        for (PlayerPerformer player : players) {
            if (!roleList.isEmpty()) {
                player.setRole(roleList.remove(0));
            } else {
                crashGame("Not enough roles for players!", storyteller.getUUID());
            }
        }
        if (!roleList.isEmpty()) {crashGame("Unassigned roles remain!", storyteller.getUUID());}
        // TODO validate with storyteller
        for (PlayerPerformer player : players) {
            player.getRole().setup(this);
        }
        updateGrimoire(this);
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
            if (player.getRole().getAffiliationActual().equals(Affiliation.MINION)) {
                minions.add(player);
            }
        }
        return minions;
    }

    private List<PlayerPerformer> getDemons(List<PlayerPerformer> players) {
        // Return a list of all Demon players in the game
        // Pit Hag can make more than one Demon
        List<PlayerPerformer> demons = new ArrayList<>();
        for (PlayerPerformer player : players) {
            if (player.getRole().getAffiliationActual().equals(Affiliation.DEMON)) {
                demons.add(player);
            }
        }
        return demons;
    }

    public boolean getInfoOverrideInGame(List<PlayerPerformer> players) {
        for (PlayerPerformer player : players) {
            if (player.getRole().getInfoOverride()) {
                return true;
            }
        }
        return false;
    }

    private void firstNight(List<PlayerPerformer> players) {
        // Handle the first night phase of the game
        this.gameState = "nighttime";
        this.dayCount = 1;
        //TODO build this
        notifyPlayersOfRoles(players);
        List<PlayerPerformer> minions = getAllMinions(players);
        List<PlayerPerformer> demons = getDemons(players);
        minionInfo(minions, demons.getFirst()); // Assumes 1 demon at start of game
        List<Role> bluffs = getBluffRoles();
        demonInfo(demons.getFirst(), minions, bluffs);
        // TODO add storyteller to action list to accomidate fabled roles
        sortPlayersByActionPriority(players);
        for (PlayerPerformer player : players) {
            if (player.getDrunk() || player.getPoisoned()) {
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
                    message = new StringBuilder(ChatColor.GRAY + "You are the " + player.getRole().getFalseRole().getRoleNameActual() + ".\n");
                    message.append("Your team is: ").append(player.getRole().getFalseRole().getTeamActual().toString()).append("\n");
                    message.append(player.getRole().getFalseRole().getStartingMessage());
                } else {
                    message = new StringBuilder(ChatColor.GRAY + "You are the " + player.getRole().getRoleNameActual() + ".\n");
                    message.append("Your team is: ").append(player.getRole().getTeamActual().toString()).append("\n");
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
                    info.append("- ").append(bluff.getRoleNameActual()).append("\n");
                }
            } else {
                // This should never happen in a standard game
                info.append("No Bluff Roles available.\n");
            }
            player.sendMessage(info.toString());
        } else {
            crashGame("Demon player not found or offline in Game.demonInfo(): " + demon.getUUID(), storyteller.getUUID());
        }
    }

    private void updateGrimoire(Game game) {
        // Update the storyteller's grimoire with current game information
        //TODO build this
    }

    public void crashGame(String reason, String storytellerUUID) {
        // Handle game crash scenario
        // TODO build a proper crash handler that effects game state without server reboot
        // TODO handle exceptions to try to keep the game running if possible
        Bukkit.getLogger().warning("Game crashed: " + reason);
        Bukkit.getPlayer(storytellerUUID).sendMessage(ChatColor.RED + reason);
    }


}
