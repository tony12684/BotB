package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;
import io.github.tony12684.BotB.Game;

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
        return pickMaster();
    }

    @Override
    public boolean otherNightAction(Game game) {
        return pickMaster();

    }

    private boolean pickMaster() {
        // TODO implement Butler pick master logic
        return true;
    }
}
