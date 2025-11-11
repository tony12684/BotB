package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.PlayerPerformer;
import io.github.tony12684.BotB.Role;
import io.github.tony12684.BotB.ActionLog;
import io.github.tony12684.BotB.Game;

import java.util.List;



/*
 * Chef - Townsfolk
 * "You start knowing how many pairs of evil players there are."
 * No special setup required.
 * First night action.
 * No other night action.
 */


 //TODO implement action logging via SQL
public class Chef extends Role {
    public Chef() {
        super("Chef", Affiliation.TOWNSFOLK, Team.GOOD);
    }
    
    @Override
    public ActionLog firstNightAction(Game game) {
        PlayerPerformer chef = game.getPlayerByRole("Chef");
        if (chef == null) {
            throw new IllegalStateException("Chef player not found in game during first night action.");
        }
        try {
            Integer evilPairs = countEvilPairs(game);
            game.getGrimoire().basicMessage(chef, "There are " + evilPairs + " pairs of evil players sitting next to each other.");
            return new ActionLog(chef, "chef", false, evilPairs.toString(), null);
        } catch (Exception e) {
            //TODO use better exception typing
            throw new IllegalStateException("Error during Chef first night action: " + e.getMessage());
        }
    }

    @Override
    public ActionLog falseFirstNightAction(Game game) {
        // prompt storyteller to provide some number
        int evilPairs = game.getGrimoire().getNumberFromPerformer(game.getStoryteller(), "Provide a number of evil pairs to show the drunk/poisoned Chef.");
        game.getGrimoire().basicMessage(game.getPlayerByRole("Chef"), "There are " + evilPairs + " pairs of evil players sitting next to each other.");
        return new ActionLog(game.getStoryteller(), "chef", true, Integer.toString(evilPairs), null);
    }

    private Integer countEvilPairs(Game game) {
        // for each sequential pair of players in seating order, check if both are evil
        // TODO redo with linked list implementation?
        PlayerPerformer lastPlayer = null;
        int evilPairs = 0;
        List<PlayerPerformer> sortedPlayers = game.getPlayers();
        sortedPlayers = game.sortPlayersBySeatOrder(sortedPlayers);
        for (PlayerPerformer player : sortedPlayers) {
            if (lastPlayer != null) {
                // TODO fix this to work better with false team roles like recluse
                if ((lastPlayer.getRole().getTeam(
                    game.getGrimoire(),
                    game.getPlayerByRole("Chef").getName(),
                    "Chef",
                    lastPlayer.getName())
                    .equals(Team.EVIL))
                && (player.getRole().getTeam(
                    game.getGrimoire(),
                    game.getPlayerByRole("Chef").getName(),
                    "Chef",
                    player.getName())
                    .equals(Team.EVIL))) {
                    evilPairs++;
                }
            }
            lastPlayer = player;
        }
        if (lastPlayer == null) {
            throw new IllegalStateException("Unexpected Null Player in Chef.countEvilPairs()");
        }
        if ((sortedPlayers.getFirst().getRole().getTeam(
            game.getGrimoire(),
            game.getPlayerByRole("Chef").getName(),
            "Chef",
            sortedPlayers.getFirst().getName())
            .equals(Team.EVIL))
        && (lastPlayer.getRole().getTeam(
            game.getGrimoire(),
            game.getPlayerByRole("Chef").getName(),
            "Chef",
            lastPlayer.getName())
            .equals(Team.EVIL))) {
            evilPairs++;
        }
        return evilPairs;
    }
}