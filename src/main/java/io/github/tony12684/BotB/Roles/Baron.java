package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Performer;
import io.github.tony12684.BotB.Role;

/*
 * Baron - Minion
 * "There are extra Outsiders in play. [+2 Outsiders]"
 * No special setup as we expect that the role count is managed externally.
 * No first night action. 
 * No other night action.
 */


public class Baron extends Role {
    // Assumes external management of extra Outsiders
    public Baron(Performer performer) {
        super(performer, "Baron", Affiliation.MINION, Team.EVIL);
    }
}
