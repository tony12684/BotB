package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
 * Reculse - Outsider
 * "You might register as evil & as a Minion or Demon, even if dead."
 * No special setup.
 * No first night action.
 * No other night action.
 * //probably just show recluse players role when verifying with storyteller.
 */

public class Recluse extends Role {
    public Recluse() {
        super("Recluse", Affiliation.OUTSIDER, Team.GOOD);
    }
}
