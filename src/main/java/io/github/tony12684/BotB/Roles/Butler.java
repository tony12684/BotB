package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;
import io.github.tony12684.BotB.Game;
import io.github.tony12684.BotB.PlayerPerformer;

/*
 * Butler - Outsider
 * "Each night, choose a player (not yourself): tomorrow, you may only vote if they are voting too."
 * No special setup.
 * First night action.
 * Other night action.
 */

public class Butler extends Role {
    private PlayerPerformer master = null;

    public Butler() {
        super("Butler", "Outsider");
    }

    @Override
    public boolean firstNightAction(Game game) {
        return pickMaster(game);
    }

    @Override
    public boolean otherNightAction(Game game) {
        return pickMaster(game);
    }

    private boolean pickMaster(Game game) {
        // TODO get input from the Butler player to choose their master
        // REMOVE THIS LINE WHEN IMPLEMENTING
        PlayerPerformer master = game.getPlayerByRole("Butler");
        return true;
    }

    @Override
    public boolean voteAction(Game game) {
        // TODO implement vote restriction based on master's vote
        return true;
    }
}
