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
        super("Butler", Affiliation.OUTSIDER, Team.GOOD);
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
        // get master choice from Butler
        master = null;
        while (master == null) {
            master = game.getGrimoire().getFreeTargetsFromPlayer(game.getPlayerByRole("Butler"), 1, "Choose your master. You cannot choose yourself.").getFirst();
            if (master.getUUID().equals(game.getPlayerByRole("Butler").getUUID())) {
                game.getGrimoire().errorMessage(game.getPlayerByRole("Butler"), "You cannot choose yourself as your master. Please select again.");
                master = null;
            }
        }

        return true;
    }

    @Override
    public boolean voteAction(Game game) {
        // TODO implement vote restriction based on master's vote
        return true;
    }
}
