package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Performer;
import io.github.tony12684.BotB.Role;

/*
 * Ravenkeeper - Townsfolk
 * "If you die at night, you are woken to choose a player: you learn their character."
 * No special setup.
 * No first night action.
 * Other night action.
 */

public class Ravenkeeper extends Role {
    public Ravenkeeper(Performer performer) {
        super(performer, "Ravenkeeper", Affiliation.TOWNSFOLK, Team.GOOD);
    }
}