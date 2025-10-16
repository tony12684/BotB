package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Mayor - Townsfolk
 * "If only 3 players live & no execution occurs, your team wins. If you die at night, another player might die instead."
 * No special setup required.
 * No first night action.
 * No other night action.
 * TODO implement Mayor logic in game engine.
 */

public class Mayor extends Role {
    public Mayor() {
        super("Mayor", "Townsfolk");
    }
}