package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Washerwoman - Townsfolk
 * "You start knowing that 1 of 2 players is a particular Townsfolk."
 * No special setup.
 * First night action.
 * No other night action.
 */

public class Washerwoman extends Role {
    public Washerwoman() {
        super("Washerwoman", Affiliation.TOWNSFOLK, Team.GOOD);
    }
}
