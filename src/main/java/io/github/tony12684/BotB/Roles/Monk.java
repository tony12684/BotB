package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Monk - Townsfolk
 * "Each night*, choose a player (not yourself): they are safe from the Demon tonight."
 * No special setup.
 * No first night action.
 * Other night action.
 */

public class Monk extends Role {
    public Monk() {
        super("Monk", Affiliation.TOWNSFOLK, Team.GOOD);
    }
}