package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Performer;
import io.github.tony12684.BotB.Role;

/*
 * Spy - Minion
 * "Each night, you see the Grimoire. You might register as good & as a Townsfolk or Outsider, even if dead."
 * No special setup.
 * First night action.
 * Other night action.
 * TODO implement Spy logic in game engine.
 * // maybe create with the grimoire object?
 */

public class Spy extends Role {
    public Spy(Performer performer) {
        super(performer, "Spy", Affiliation.MINION, Team.EVIL);
    }
}
