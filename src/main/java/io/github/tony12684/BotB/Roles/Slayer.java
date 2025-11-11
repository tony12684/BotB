package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Slayer - Townsfolk
 * "Once per game, during the day, publicly choose a player: if they are the Demon, they die."
 * Special setup required. //TODO implement slayer actions in game if slayer is in the provided script.
 * No first night action.
 * No other night action.
 * //TODO implement Slayer logic in game engine.
 */

public class Slayer extends Role {
    public Slayer() {
        super("Slayer", Affiliation.TOWNSFOLK, Team.GOOD);
    }
}