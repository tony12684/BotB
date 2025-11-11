package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;
import io.github.tony12684.BotB.ActionLog;
import io.github.tony12684.BotB.Game;
import io.github.tony12684.BotB.Performer;
import io.github.tony12684.BotB.PlayerPerformer;

import java.util.ArrayList;
import java.util.List;

/*
* Empath - Townsfolk
* "Each night, you learn how many of your 2 alive neighbors are evil."
* No special setup.
* First night action.
* Other night action.
 */

public class Empath extends Role {
    public Empath() {
        super("Empath", Affiliation.TOWNSFOLK, Team.GOOD);
    }

    @Override
    public ActionLog firstNightAction(Game game) {
        return empathy(game);
    }
    @Override
    public ActionLog otherNightAction(Game game) {
        return empathy(game);
    }
    
    @Override
    public ActionLog falseFirstNightAction(Game game) {
        return apathy(game);
    }
    @Override
    public ActionLog falseOtherNightAction(Game game) {
        return apathy(game);
    }

    private ActionLog apathy(Game game) {
        // prompt storyteller to provide some number
        int evilCount = game.getGrimoire().getNumberFromPerformer(game.getStoryteller(), "Provide a number of evil neighbors to show the drunk/poisoned Empath.");
        game.getGrimoire().basicMessage(game.getPlayerByRole("Empath"), "Your alive neighbors contain " + evilCount + " evil player(s).");
        return new ActionLog(game.getStoryteller(), "empath", true, Integer.toString(evilCount), null);
    }

    private ActionLog empathy(Game game) {
        // get alive neighbors of the Empath player
        List<PlayerPerformer> neighbors = getLivingNeighbors(game);
        // count how many are evil
        int evilCount = 0;
        for (PlayerPerformer neighbor : neighbors) {
            if (neighbor.getRole().getTeam(
                game.getGrimoire(),
                game.getPlayerByRole("Empath").getName(),
                "Empath",
                neighbor.getName()).equals(Team.EVIL)) {
                evilCount++;
            }
        }
        game.getGrimoire().basicMessage(game.getPlayerByRole("Empath"), "Your alive neighbors contain " + evilCount + " evil player(s).");
        List<Performer> targets = new ArrayList<Performer>();
        for (PlayerPerformer neighbor : neighbors) {
            targets.add(neighbor);
        }
        return new ActionLog(game.getPlayerByRole("Empath"), "empath", false, Integer.toString(evilCount), targets);
    }

    private List<PlayerPerformer> getLivingNeighbors(Game game) {
        // get the 2 living neighbors of the Empath player
        List<PlayerPerformer> neighbors = new ArrayList<PlayerPerformer>();
        PlayerPerformer player = game.getPlayerByRole("Empath");
        //find left neighbor
        while (true) {
            player = player.getLeftNeighbor();
            if (player.getAlive()) {
                neighbors.add(player);
                break;
            } else if (player.getRole().getRoleNameActual().equals("Empath")) {
                // wrapped around, no alive neighbor found
                // safe stop if all players are dead
                break;
            }
        }
        //find right neighbor
        player = game.getPlayerByRole("Empath");
        while (true) {
            player = player.getRightNeighbor();
            if (player.getAlive()) {
                neighbors.add(player);
                break;
            } else if (player.getRole().getRoleNameActual().equals("Empath")) {
                // wrapped around, no alive neighbor found
                // safe stop if all players are dead somehow
                break;
            }
        }

        return neighbors;
    }
}