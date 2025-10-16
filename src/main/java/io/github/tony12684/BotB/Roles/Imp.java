package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Imp - Demon
 * "Each night*, choose a player: they die. If you kill yourself this way, a Minion becomes the Imp."
 * No special setup. Demon info and bluffroles are handled by game logic.
 * No first night action.
 * Other night action.
 */

public class Imp extends Role {
    public Imp() {
        super("Imp", "Demon");
    }
}
