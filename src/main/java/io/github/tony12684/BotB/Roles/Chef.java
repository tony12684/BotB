package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.PlayerPerformer;
import io.github.tony12684.BotB.Role;
import io.github.tony12684.BotB.ActionLog;
import io.github.tony12684.BotB.Game;
import io.github.tony12684.BotB.Performer;

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
    int evilPairsSetup = -1;
    public Chef(Performer performer) {
        super(performer, "Chef", Affiliation.TOWNSFOLK, Team.GOOD);
    }
    
    @Override
    public ActionLog firstNightSetupMode(Game game) {
        evilPairsSetup = countEvilPairs(game);
        return new ActionLog(game.getStoryteller(), "chef_setup", false, null, null);
    }

    @Override
    public ActionLog firstNightAction(Game game) {
        Integer evilPairs = countEvilPairs(game);
        game.getGrimoire().basicMessage(this.getPerformer(), "There are " + evilPairs + " pairs of evil players sitting next to each other.");
        return new ActionLog(this.getPerformer(), "chef", false, evilPairs.toString(), null);
    }

    @Override
    public ActionLog falseFirstNightAction(Game game) {
        // prompt storyteller to provide some number
        // TODO refactor to use new async number input method
        return null;
        //int evilPairs = game.getGrimoire().getNumberFromPerformer(game.getStoryteller(), "Provide a number of evil pairs to show the drunk/poisoned Chef.");
        //game.getGrimoire().basicMessage(this.getPerformer(), "There are " + evilPairs + " pairs of evil players sitting next to each other.");
        //return new ActionLog(game.getStoryteller(), "chef", true, Integer.toString(evilPairs), null);
    }

    private Integer countEvilPairs(Game game) {
        // for each sequential pair of players in seating order, check if both are evil
        // TODO redo with linked list implementation?
        if (evilPairsSetup != -1) {
            // only fires if chef isn't poisoned/drunk and already did setup
            return evilPairsSetup; // return cached value calculated during setup
        }
        PlayerPerformer lastPlayer = null;
        int evilPairs = 0;
        List<PlayerPerformer> sortedPlayers = game.getPlayers();
        sortedPlayers = game.sortPlayersBySeatOrder(sortedPlayers);
        if (game.getInfoOverrideInGame(sortedPlayers)) { // if recluse or similar is in game, ask storyteller
            // TODO refactor to use new async number input method
            //return game.getGrimoire().getNumberFromPerformer(game.getStoryteller(), "How many evil pairs does the Chef see? \nConsider roles such as Recluse that may affect this count.");
            return 0;
        } // TODO improve this so that storyteller is only prompted when necessary
        for (PlayerPerformer player : sortedPlayers) {
            if (lastPlayer != null) {
                // TODO fix this to work better with false team roles like recluse
                if ((lastPlayer.getRole().getTeam(
                    game.getGrimoire(),
                    this.getPerformer().getName(),
                    "Chef",
                    lastPlayer.getName())
                    .equals(Team.EVIL))
                && (player.getRole().getTeam(
                    game.getGrimoire(),
                    this.getPerformer().getName(),
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
            this.getPerformer().getName(),
            "Chef",
            sortedPlayers.getFirst().getName())
            .equals(Team.EVIL))
        && (lastPlayer.getRole().getTeam(
            game.getGrimoire(),
            this.getPerformer().getName(),
            "Chef",
            lastPlayer.getName())
            .equals(Team.EVIL))) {
            evilPairs++;
        }
        return evilPairs;
    }
}