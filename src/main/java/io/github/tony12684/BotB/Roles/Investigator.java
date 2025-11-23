package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Performer;
import io.github.tony12684.BotB.Role;

/*
 * Investigator - Townsfolk
 * "You start knowing that 1 of 2 players is a particular Minion."
 * No special setup.
 * First night action.
 * No other night action.
 */

public class Investigator extends Role {
    public Investigator(Performer performer) {
        super(performer, "Investigator", Affiliation.TOWNSFOLK, Team.GOOD);
    }
}
