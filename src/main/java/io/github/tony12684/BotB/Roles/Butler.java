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
        // Prompt the Butler to pick a master player
        // TODO get user input that returns an in game player alive or dead except themselves
        // this.master = chosenPlayer;
        return true;
    }
}
