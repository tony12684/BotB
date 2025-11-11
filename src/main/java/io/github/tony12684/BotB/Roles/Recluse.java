package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Grimoire;
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
    @Override
    public Team getTeam(Grimoire grimoire, String requestingName, String requestingRole, String targetName) {
        String msg = requestingName + " the " + requestingRole + " is asking about " + targetName +  " the Recluse's team. \nPlease provide the team to show them.";
        return grimoire.getTeamFromPerformer(grimoire.getStoryteller(),msg);
    }
    @Override
    public Affiliation getAffiliation(Grimoire grimoire, String requestingName, String requestingRole, String targetName) {
        String msg = requestingName + " the " + requestingRole + " is asking about " + targetName +  " the Recluse's affiliation. \nPlease provide the affiliation to show them.";
        return grimoire.getAffiliationFromPerformer(grimoire.getStoryteller(), msg);
    }
}
