package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;
import io.github.tony12684.BotB.Game;
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
    public boolean firstNightAction(Game game) {
        return empathy(game);
    }
    @Override
    public boolean otherNightAction(Game game) {
        return empathy(game);
    }

    private boolean empathy(Game game) {
        // TODO implement Empath logic to count alive evil neighbors
        // get alive neighbors of the Empath player
        List<PlayerPerformer> neighbors = getLivingNeighbors(game);
        // count how many are evil
        int evilCount = 0;
        for (PlayerPerformer neighbor : neighbors) {
            if (neighbor.getRole().getTeam().equals(Team.EVIL)) {
                evilCount++;
            }
        }
        game.getGrimoire().basicMessage(game.getPlayerByRole("Empath"), "Your alive neighbors contain " + evilCount + " evil player(s).");
        return true;
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
            } else if (player.getRole().getRoleName().equals("Empath")) {
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
            } else if (player.getRole().getRoleName().equals("Empath")) {
                // wrapped around, no alive neighbor found
                // safe stop if all players are dead somehow
                break;
            }
        }

        return neighbors;
    }
}