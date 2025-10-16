package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Drunk - Outsider
 * "You do not know you are the Drunk. You think you are a Townsfolk character, but you are not."
 * No special setup. Drunk is given a false role on creation.
 * No first night action.
 * No other night action.
 * Check imbedded false role for all relevant kinds of actions in game logic.
 */

public class Drunk extends Role {
    public Drunk(Role falseRole) {
        super("Drunk", "Outsider");
        this.setFalseRole(falseRole);
    }
}
