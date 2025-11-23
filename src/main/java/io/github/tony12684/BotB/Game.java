package io.github.tony12684.BotB;
import io.github.tony12684.BotB.Role.Affiliation;
import io.github.tony12684.BotB.Role.Team;
import io.github.tony12684.BotB.Roles.Storyteller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

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
    
    public Game(Main plugin, UUID storytellerUUID, List<UUID> playerUUIDs) {
        //Constructor for Game class
        //Responsible for game setup sequencing
        this.gameState = "setup";
        this.dayCount = 0;
        if (plugin.debugMode) {
            Bukkit.getLogger().info("Initializing new game setup.");
            Bukkit.getLogger().info("gamestate set to 'setup'.");
            Bukkit.getLogger().info("dayCount set to 0.");
        }
        this.plugin = plugin;
        try {
            this.gameId = plugin.insertGameStart();
            Bukkit.getLogger().info("Game started with ID: " + gameId);
        } catch (Exception e) {
            crashGame("Database error on game start: " + e.getMessage(), storytellerUUID);
        }
        // TODO update this to accept fabled storytellers
        // TODO update this to accept storyteller nicknames
        this.storyteller = new StorytellerPerformer(storytellerUUID, null, Bukkit.getPlayer(storytellerUUID).getName());
        storyteller.setRole(new Storyteller(storyteller));
        this.grimoire = new Grimoire(storyteller, this);


        // Build players list
        this.players = new ArrayList<>();
        for (UUID uuid : playerUUIDs) {
            players.add(new PlayerPerformer(uuid, null, Bukkit.getPlayer(uuid).getName())); // Role to be assigned later
            if (plugin.debugMode) {
                Bukkit.getLogger().info("Added player to game: " + Bukkit.getPlayer(uuid).getName());
            }
            // TODO adjust for nickname plugin support
        }
        // TODO log players to game_users

        //TODO probably move all this to Game.startGame()
        assignSeats(players);
        
        linkNeighbors(players);


        List<Role> roleList = grimoire.buildRoleList(this, players.size());
        assignActionPriorityToRoles(roleList);
        assignRoles(players, roleList);

        setupPhase();

        firstNight(); // Proceed to the first night phase
        //TODO build daytime, voting and subsequent night phases loop
        //TODO build drunk false role handling into Game class action logic

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

    public Main getPlugin() {
        return plugin;
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
        players.sort((p1, p2) -> Integer.compare(p1.getRole().getActionPriority(), p2.getRole().getActionPriority()));
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
            if (plugin.debugMode) {
                Bukkit.getLogger().info("Assigned seat " + player.getSeat() + " to player " + player.getName());
            }
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

    private void assignActionPriorityToRoles(List<Role> roleList) {
        // Assign action priority to each role in the role list
        // get role action priority from actionPriority.yaml
        Yaml yaml = new Yaml();
        try (InputStream in = Main.class.getResourceAsStream("/actionPriority.yaml")) {
            if (in == null) {
                crashGame("actionPriority.yaml not found!", storyteller.getUUID());
                return;
            }
            Map<Integer, String> actionPriorityMap = yaml.load(in);
            for (Role role : roleList) {
                for (Map.Entry<Integer, String> entry : actionPriorityMap.entrySet()) {
                    if (entry.getValue().equals(role.getRoleNameActual())) {
                        role.setActionPriority(entry.getKey());
                        if (plugin.debugMode) {
                            Bukkit.getLogger().info("Assigned action priority " + entry.getKey() + " to role " + role.getRoleNameActual());
                        }
                        break;
                    }
                }
                // If role not found in actionPriority.yaml, assign default priority 0
                role.setActionPriority(0);
            }
        } catch (Exception e) {
            crashGame("Error loading actionPriority.yaml: " + e.getMessage(), storyteller.getUUID());
        }
    }

    private void assignRoles(List<PlayerPerformer> players, List<Role> roleList) {
        //TODO build to require storyteller validation
        // Randomly assign roles to players
        Collections.shuffle(roleList);
        for (PlayerPerformer player : players) {
            if (!roleList.isEmpty()) {
                player.setRole(roleList.remove(0));
                if (plugin.debugMode) {
                    Bukkit.getLogger().info("Assigned role " + player.getRole().getRoleNameActual() + " to player " + player.getName());
                }
            } else {
                crashGame("Not enough roles for players!", storyteller.getUUID());
            }
        }
        if (!roleList.isEmpty()) {crashGame("Unassigned roles remain!", storyteller.getUUID());}
        // TODO validate with storyteller
    }

    private void setupPhase() {
        // perform all setup()'s in roles
        List<ActionLog> setupLogs = this.grimoire.setupLoop(this);
        // if storyteller wants to do first night setup mode, do that now
        if (this.grimoire.getFirstNightSetupMode()) {
            List<ActionLog> firstNightSetupLogs = this.grimoire.firstNightSetupLoop(this);
        }
    }

    private List<PlayerPerformer> getAllMinions(List<PlayerPerformer> players) {
        List<PlayerPerformer> minions = new ArrayList<>();
        for (PlayerPerformer player : players) {
            if (player.getRole().getFalseRole() != null) {
                // Contiginency to add "drunk minions" though they don't yet exist
                if (player.getRole().getFalseRole().getAffiliationActual().equals(Affiliation.MINION)) {
                    minions.add(player);
                }
            } else {
                if (player.getRole().getAffiliationActual().equals(Affiliation.MINION)) {
                    minions.add(player);
                }
            }
        }
        return minions;
    }

    private List<PlayerPerformer> getDemons(List<PlayerPerformer> players) {
        // Return a list of all Demon players in the game
        // Pit Hag can make more than one Demon
        // ALSO includes drunk demons like lunatic
        List<PlayerPerformer> demons = new ArrayList<>();
        for (PlayerPerformer player : players) {
            if (player.getRole().getFalseRole() != null) {
                // Contiginency to add "drunk demons" though they don't yet exist
                if (player.getRole().getFalseRole().getAffiliationActual().equals(Affiliation.DEMON)) {
                    demons.add(player);
                }
            } else {
                if (player.getRole().getAffiliationActual().equals(Affiliation.DEMON)) {
                    demons.add(player);
                }
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

    private void firstNight() {
        // Handle the first night phase of the game
        this.gameState = "night";
        this.dayCount = 1;
        if (plugin.debugMode) {
            Bukkit.getLogger().info("First night phase started.");
            Bukkit.getLogger().info("gamestate set to 'night'.");
            Bukkit.getLogger().info("dayCount set to 1.");
        }
        notifyPlayersOfRoles();
        List<PlayerPerformer> minions = getAllMinions(players);
        List<PlayerPerformer> demons = getDemons(players);
        // notify minions of each other and true demon
        for (PlayerPerformer demon : demons) {
            // ignore lunatic among demons for minion info
            if (demon.getRole().getAffiliationActual().equals(Affiliation.DEMON)) {
                // only one demon should be true demon
                // minions do not know about lunatic demons
                grimoire.minionInfo(minions, demon);
            }
        }
        List<Role> bluffs = grimoire.getBluffRoles();
        for (PlayerPerformer demon : demons) {
            // check for false demon
            if (demon.getRole().getFalseRole() != null) {
                PlayerPerformer trueDemon = null;
                // find true demon
                for (PlayerPerformer demonY : demons) {
                    if (demonY.getRole().getAffiliationActual().equals(Affiliation.DEMON)) {
                        trueDemon = demonY;
                    }
                }
                // notify lunatic of minions and bluff roles
                grimoire.demonInfo(null, demon, minions, bluffs);
                // notify true demon of lunatic and minions and bluff roles
                grimoire.demonInfo(trueDemon, demon, minions, bluffs);
            } else {
                // notify true demon of minions and bluff roles
                grimoire.demonInfo(demon, null, minions, bluffs);
            }
        }
        // TODO add storyteller to action list to accomidate fabled roles
        sortPlayersByActionPriority(players);
        for (PlayerPerformer player : players) {
            if (player.getRole().getFalseRole() != null) {
                // for false role players do their false roles false first night action
                try {
                    player.getRole().getFalseRole().falseFirstNightAction(this);
                } catch (Exception e) {
                    // Catch any exceptions thrown during the false first night action
                    crashGame("Error during false first night action for player " + player.getUUID() + ": " + e.getMessage(), storyteller.getUUID());
                }
            } else if (player.getDrunk() || player.getPoisoned()) {
                // for drunk or poisoned players do their false first night action
                try {
                    player.getRole().falseFirstNightAction(this);
                } catch (Exception e) {
                    // Catch any exceptions thrown during the false first night action
                    crashGame("Error during false first night action for player " + player.getUUID() + ": " + e.getMessage(), storyteller.getUUID());
                }
            } else {
                // for normal players do their normal first night action
                try {
                    player.getRole().firstNightAction(this);
                } catch (Exception e) {
                    // Catch any exceptions thrown during the first night action
                    crashGame("Error during first night action for player " + player.getUUID() + ": " + e.getMessage(), storyteller.getUUID());
                }
            }
        }
    }

    
    private void notifyPlayersOfRoles() {
        // Notify each player of their assigned role
        // TODO send the messages to the grimoire to display in a better UI
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

    public void crashGame(String reason, UUID storytellerUUID) {
        // Handle game crash scenario
        // TODO build a proper crash handler that effects game state without server reboot
        // TODO handle exceptions to try to keep the game running if possible
        Bukkit.getLogger().warning("Game crashed: " + reason);
        Bukkit.getPlayer(storytellerUUID).sendMessage(ChatColor.RED + reason);
    }


}
