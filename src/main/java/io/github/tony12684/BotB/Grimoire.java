package io.github.tony12684.BotB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.github.tony12684.BotB.Role.Affiliation;
import io.github.tony12684.BotB.Role.Team;
import net.md_5.bungee.api.ChatColor;

public class Grimoire {
    private StorytellerPerformer storyteller;
    private Game game;
    public final int TIMEOUT_SECONDS = 15;
    private boolean firstNightSetupMode = false;
    private final Map<UUID, CompletableFuture<Integer>> pendingNumResponses = new ConcurrentHashMap<>();
    private final Map<UUID, CompletableFuture<Boolean>> pendingBoolResponses = new ConcurrentHashMap<>();
    private final Map<UUID, CompletableFuture<Team>> pendingTeamResponses = new ConcurrentHashMap<>();
    private final Map<UUID, CompletableFuture<Affiliation>> pendingAffiliationResponses = new ConcurrentHashMap<>();

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

    public Map<UUID, CompletableFuture<Integer>> getPendingNumResponses() {
        return pendingNumResponses;
    }
    public Map<UUID, CompletableFuture<Boolean>> getPendingBoolResponses() {
        return pendingBoolResponses;
    }
    public Map<UUID, CompletableFuture<Team>> getPendingTeamResponses() {
        return pendingTeamResponses;
    }
    public Map<UUID, CompletableFuture<Affiliation>> getPendingAffiliationResponses() {
        return pendingAffiliationResponses;
    }

    public StorytellerPerformer getStoryteller() {
        return storyteller;
    }

    public boolean getFirstNightSetupMode() {
        return firstNightSetupMode;
    }

    public List<Role> getBluffRoles() {
        // Build and return a list of Role objects representing available bluff roles
        List<Role> bluffRoles = new java.util.ArrayList<>();
        // TODO initiate bluff 
        //bluffRoles.add(new Washerwoman(null));
        //TODO retrieve list of bluff roles from storyteller
        return bluffRoles;
    }

    public void minionInfo(List<PlayerPerformer> minions, PlayerPerformer demon) {
    // Send minion and demon information to each minion player
    //TODO upgrade this to use our UI when we find one
        for (PlayerPerformer minion : minions) {
            Player player = Bukkit.getPlayer(minion.getUUID());
            if (player != null && player.isOnline()) {
                StringBuilder info = new StringBuilder(ChatColor.GRAY + "Your fellow Minions are:\n");
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
            StringBuilder info = new StringBuilder(ChatColor.GRAY + "You are the Demon.\n");
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

    public CompletableFuture<List<ActionLog>> setupLoop(Game game) {
        // Method to perform setup for all roles in the game
        // For each player, call their setup() method and collect the resulting ActionLogs
        // allow for storyteller to iterate through completed setup actions and confirm completion
        // be very careful that no setup actions are skipped, no setup actions are double performed
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        // TODO implement setup loop
        return CompletableFuture.completedFuture(new ArrayList<>()); // Placeholder return;
    }

    public CompletableFuture<List<ActionLog>> firstNightSetupLoop(Game game) {
        // Perform setup AND first night setup for all roles in the game
        // For each player, call their firstNightSetupMode() method and collect the resulting ActionLogs
        // allow for storyteller to iterate through completed actions and confirm completion
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        // TODO implement first night setup loop
        return CompletableFuture.completedFuture(new ArrayList<>()); // Placeholder return;
    }

    public CompletableFuture<Boolean> getSetupParameters() {
        // return true to prep the first night info for roles like chef, librarian, investigator, etc.
        //     that have no player input on first night
        // return true if method completes successfully
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        // TODO get actual parameters from storyteller
        if (game.getPlugin().debugMode) {
            Bukkit.getLogger().info("First night setup mode " + firstNightSetupMode + ".");
        }
        return CompletableFuture.completedFuture(true); // Placeholder return;
    }

    public CompletableFuture<List<Role>> buildScript(Game game, int numberOfPlayers) {
        CompletableFuture<List<Role>> future = new CompletableFuture<>();
        // get the script the storyteller wants to run
        return CompletableFuture.completedFuture(new ArrayList<>()); // Placeholder return;
    }

    public CompletableFuture<List<Role>> buildSubScript(Game game, int numberOfPlayers) {
        CompletableFuture<List<Role>> future = new CompletableFuture<>();
        // get the list of roles from the storyteller for this game
        return CompletableFuture.completedFuture(new ArrayList<>()); // Placeholder return;
    }

    public void basicMessage(Performer performer, String message) {
        // Method to show basic messages to a performer
    }

    public void errorMessage(Performer performer, String message) {
        // Method to show error messages to a performer
        Player player = Bukkit.getPlayer(performer.getUUID());
        if (player != null && player.isOnline()) {
            player.sendMessage(ChatColor.RED + message);
        }
    }

    private CompletableFuture<Boolean> showDialog (Performer performer, String dialogJson, CompletableFuture<?> future) {
        // Method to show a custom dialog to a performer
        
        // show our dialog to the performer via console command
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, "dialog show " +  Bukkit.getPlayer(performer.getUUID()).getName() + " " + dialogJson);
        
        // set up timeout task
        Bukkit.getScheduler().runTaskLater(game.getPlugin(), () -> {
            if (!future.isDone()) {
                future.completeExceptionally(new TimeoutException("Input timed out."));
                pendingNumResponses.remove(performer.getUUID());
                Bukkit.getPlayer(performer.getUUID()).sendMessage(ChatColor.RED + "Input timed out after " + TIMEOUT_SECONDS + " seconds.");
            }
        }, TIMEOUT_SECONDS * 20L); // Convert seconds to ticks (20 ticks = 1 second)

        // wait for response and return the number
        future.thenAccept(result -> {
            Bukkit.getScheduler().runTask(game.getPlugin(), () -> {
                Bukkit.getPlayer(performer.getUUID()).sendMessage(ChatColor.GRAY + "You submitted the number: " + result);
                // TODO generalization for all input types
                // TODO error handling for invalid inputs
                // TODO pass the result back to the waiting method?
            });
        }).exceptionally(e -> {
            pendingNumResponses.remove(performer.getUUID());
            errorMessage(performer, "Error receiving number input: " + e.getMessage());
            return null;
        });

        return CompletableFuture.completedFuture(false); // Placeholder return;
    }

    public CompletableFuture<List<PlayerPerformer>> getTargetsFromPerformer(
        Performer actingPerformer, 
        int numberOfTargets, 
        String message, 
        List<Performer> targetWhiteOrBlackList,
        boolean isWhiteList) {
        // Method to get some number of unrestricted targets from a performer
        // 
        return null; // Placeholder return;
    }
    public CompletableFuture<List<PlayerPerformer>> getTargetsFromPerformer(
        Performer actingPerformer, 
        int numberOfTargets, 
        String message) {
        // Method to get some number of unrestricted targets from a performer
        // 
        return null; // Placeholder return;
    }

    public CompletableFuture<Integer> getNumberFromPerformer(Performer performer, String promptMessage) {
        // Method to get a number input from a performer
        // uses ID "num_sub" for the submission
        // uses key "num" for the number input
        // register our completableFuture
        // TODO update to allow return of negative numbers if needed
        CompletableFuture<Integer> future = new CompletableFuture<>();
        pendingNumResponses.put(performer.getUUID(), future);
        // TODO update this to work with with show dialog chain
        
        // Build dialog JSON with the prompt message
        String dialogJson = "";
        // Show dialog using showDialog method
        showDialog(performer, dialogJson, future);
        
        return future;
    }

    public CompletableFuture<Boolean> getBooleanFromPerformer(Performer performer, String promptMessage) {
        // Method to get a boolean input from a performer
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        return CompletableFuture.completedFuture(false); // Placeholder return;
    }

    public CompletableFuture<Team> getTeamFromPerformer(Performer performer, String promptMessage) {
        // Method to get the team of a performer
        return CompletableFuture.completedFuture(null); // Placeholder return;
    }

    public CompletableFuture<Affiliation> getAffiliationFromPerformer(Performer performer, String promptMessage) {
        // Method to get the affiliation of a performer
        return CompletableFuture.completedFuture(null); // Placeholder return;
    }
}
