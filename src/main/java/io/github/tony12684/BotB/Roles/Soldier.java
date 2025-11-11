package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Soldier - Townsfolk
 * "You are safe from the Demon."
 * No special setup.
 * No first night action.
 * No other night action.
 * TODO implement Soldier logic in game engine.
 * // maybe create a protected boolean variable in Role class?
 */

public class Soldier extends Role {
    public Soldier() {
        super("Soldier", Affiliation.TOWNSFOLK, Team.GOOD);
    }
}