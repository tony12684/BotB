package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

import java.util.List;

import io.github.tony12684.BotB.ActionLog;
import io.github.tony12684.BotB.Game;
import io.github.tony12684.BotB.Performer;
import io.github.tony12684.BotB.PlayerPerformer;

/*
 * Fortune Teller - Townsfolk
 * "Each night, choose 2 players: you learn if either is a Demon. There is a good player that registers as a Demon to you."
 * No special setup.
 * First night action.
 * Other night action required.
 */

public class Fortune_Teller extends Role {
    PlayerPerformer redHerring = null;
    public Fortune_Teller() {
        super("Fortune Teller", Affiliation.TOWNSFOLK, Team.GOOD);
    }

    @Override
    public ActionLog setup(Game game) {
        redHerring = null;
        while (redHerring == null) {
            this.redHerring = game.getGrimoire().getFreeTargetsFromPlayer(game.getStoryteller(), 1, "Select a good player to be the Red Herring for Fortune Teller.").getFirst();
            if (redHerring.getRole().getTeam().equals(Team.EVIL)) {
                game.getGrimoire().errorMessage(game.getStoryteller(), "Red Herring must be a good player. Please select again.");
                redHerring = null;
            }
        }
        List<Performer> targets = List.of(redHerring);
        return new ActionLog(game.getStoryteller(), "fortune_teller_red_herring", false, null, targets);
    }

    @Override
    public ActionLog otherNightAction(Game game) {
        // TODO implement default random time lag to reduce fortune teller metagaming
        // TODO implement Fortune Teller logic to choose 2 players and learn if either is a Demon
        return null;
    }
}