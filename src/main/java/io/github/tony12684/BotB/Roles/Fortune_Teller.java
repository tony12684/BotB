package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

import io.github.tony12684.BotB.Game;
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
        super("Fortune_Teller", Affiliation.TOWNSFOLK, Team.GOOD);
    }

    @Override
    public boolean setup(Game game) {
        redHerring = null;
        while (redHerring == null) {
            this.redHerring = game.getGrimoire().getFreeTargetsFromPlayer(game.getStoryteller(), 1, "Select a good player to be the Red Herring for Fortune Teller.").getFirst();
            if (redHerring.getRole().getTeam().equals(Team.EVIL)) {
                game.getGrimoire().errorMessage(game.getStoryteller(), "Red Herring must be a good player. Please select again.");
                redHerring = null;
            }
        }
        return true;
    }

    @Override
    public boolean otherNightAction(Game game) {
        // TODO implement default random time lag to reduce fortune teller metagaming
        // TODO implement Fortune Teller logic to choose 2 players and learn if either is a Demon
        return true;
    }
}