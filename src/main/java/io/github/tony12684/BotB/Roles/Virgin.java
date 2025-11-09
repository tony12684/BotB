package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Virgin - Townsfolk
 * "The 1st time you are nominated, if the nominator is a Townsfolk, they are executed immediately."
 * No special setup.
 * No first night action.
 * No other night action.
 * TODO implement Virgin logic in game engine.
 * // maybe create a default function for nomination and execution actions?
 */

public class Virgin extends Role {
    public Virgin() {
        super("Virgin", Affiliation.TOWNSFOLK, Team.GOOD);
    }
}