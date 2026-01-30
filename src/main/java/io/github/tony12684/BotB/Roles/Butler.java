package io.github.tony12684.BotB.Roles;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;

import io.github.tony12684.BotB.ActionLog;
import io.github.tony12684.BotB.Game;
import io.github.tony12684.BotB.Performer;
import io.github.tony12684.BotB.PlayerPerformer;
import io.github.tony12684.BotB.Role;

/*
 * Butler - Outsider
 * "Each night, choose a player (not yourself): tomorrow, you may only vote if they are voting too."
 * No special setup.
 * First night action.
 * Other night action.
 * Vote action restriction based on master's vote.
 */

public class Butler extends Role {
    private PlayerPerformer master = null;

    public Butler(Performer performer) {
        super(performer,"Butler", Affiliation.OUTSIDER, Team.GOOD);
    }

    @Override
    public CompletableFuture<List<ActionLog>> firstNightAction(Game game) {
        // prompt butler to select a master
        return pickMaster(game);
    }

    @Override
    public CompletableFuture<List<ActionLog>> otherNightAction(Game game) {
        // prompt butler to select a master
        return pickMaster(game);
    }

    @Override
    public CompletableFuture<List<ActionLog>> falseFirstNightAction(Game game) {
        // TODO build
        // prompt butler to select a target
        //  then prompt storyteller to provide a target
        return null;
    }
    
    @Override
    public CompletableFuture<List<ActionLog>> falseOtherNightAction(Game game) {
        // TODO build
        // prompt butler to select a target
        //  then prompt storyteller to provide a target
        return null;
    }

    private CompletableFuture<List<ActionLog>> pickMaster(Game game) {
        // get master choice from Butler async
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();

        // setup timeout task to avoid blocking
        // TODO adjust this to supply a random master if timeout occurs
        Bukkit.getScheduler().runTaskLater(game.getPlugin(), () -> {
            if (!future.isDone()) {
                future.completeExceptionally(new Exception("Butler master selection timed out."));
            }
        }, game.getGrimoire().TIMEOUT_SECONDS * 20L); // convert seconds to ticks

        // Get master selection from performer - only attempt once
        game.getGrimoire().getTargetsFromPerformer(
            this.getPerformer(), 
            1, 
            "Choose your master. You cannot choose yourself."
        ).thenAccept(targets -> {
            if (targets == null || targets.isEmpty()) {
                future.completeExceptionally(new Exception("No targets selected for Butler master"));
                return;
            }

            Performer candidate = targets.get(0);
            
            // Validate that player didn't select themselves
            if (candidate.getUUID().equals(this.getPerformer().getUUID())) {
                future.completeExceptionally(new Exception("Butler cannot choose themselves as master"));
            } else {
                // Valid selection - cast to PlayerPerformer and complete
                if (candidate instanceof PlayerPerformer) {
                    master = (PlayerPerformer) candidate; //cast to PlayerPerformer and save
                    List<Performer> targetList = List.of(master); //place into list for logging
                    ActionLog log = new ActionLog( //create action log
                        this.getPerformer(),
                        "butler_master_choice",
                        false,
                        null,
                        targetList);
                    future.complete(List.of(log));
                } else {
                    future.completeExceptionally(new Exception("Selected target is not a PlayerPerformer"));
                }
            }
        }).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }
    

    @Override
    public CompletableFuture<List<ActionLog>> voteAction(Game game) {
        // TODO implement vote restriction based on master's vote
        return null;
    }
}
