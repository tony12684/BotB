package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Saint - Outsider
 * "If you die by execution, your team loses."
 * No special setup.
 * No first night action.
 * No other night action.
 * //TODO implement Saint logic in game engine.
 */

public class Saint extends Role {
    public Saint() {
        super("Saint", Affiliation.OUTSIDER, Team.GOOD);
    }
}
