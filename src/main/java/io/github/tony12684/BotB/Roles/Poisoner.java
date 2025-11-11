package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Poisoner - Minion
 * "Each night, choose a player: they are poisoned tonight and tomorrow day."
 * No special setup.
 * First night action.
 * Other night action.
 * TODO implement Poison effect in role logic.
 * // create a poison object?
 */

public class Poisoner extends Role {
    public Poisoner() {
        super("Poisoner", Affiliation.MINION, Team.EVIL);
    }
}
