package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;
import io.github.tony12684.BotB.ActionLog;
import io.github.tony12684.BotB.Game;
import io.github.tony12684.BotB.Performer;
import io.github.tony12684.BotB.PlayerPerformer;

import java.util.List;

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
    public ActionLog firstNightAction(Game game) {
        return pickMaster(game);
    }

    @Override
    public ActionLog otherNightAction(Game game) {
        return pickMaster(game);
    }

    @Override
    public ActionLog falseFirstNightAction(Game game) {
        // No modification required for drunk and poisoned butler.
        // prompt storyteller to provide some number
        return null;
    }
    
    @Override
    public ActionLog falseOtherNightAction(Game game) {
        // No modification required for drunk and poisoned butler.
        return pickMaster(game);
    }

    private ActionLog pickMaster(Game game) {
        // get master choice from Butler
        master = null;
        while (master == null) {
            master = game.getGrimoire().getFreeTargetsFromPerformer(this.getPerformer(), 1, "Choose your master. You cannot choose yourself.").getFirst();
            if (master.getUUID().equals(this.getPerformer().getUUID())) {
                game.getGrimoire().errorMessage(this.getPerformer(), "You cannot choose yourself as your master. Please select again.");
                master = null;
            }
        }
        List<Performer> targets = List.of(master);
        return new ActionLog(this.getPerformer(), "butler_master_choice", true, null, targets);
    }
    

    @Override
    public ActionLog voteAction(Game game) {
        // TODO implement vote restriction based on master's vote
        return null;
    }
}
