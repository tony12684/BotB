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
    @Override
    public boolean firstNightAction(Game game) {
        boolean result = empathy(Game game);
        return true;
    }
    @Override
    public boolean otherNightAction(Game game) {
        boolean result = empathy(Game game);
        return true;
    }

    private boolean empathy(Game game) {
        // Returns the two neighbors of the given player based on seat order
        getLivingNeighbors(Game game, game.getPlayerByRole("Empath"));

        return true;
    }

    private getLivingNeighbors(Game game, PlayerPerformer empath) {
        List<PlayerPerformer> players = game.getPlayers();
        players = game.sortPlayersBySeatOrder(players);
        Int index = empath.getSeat(); // Seats are 1-indexed
        PlayerPerformer leftNeighbor;
        PlayerPerformer rightNeighbor;

        if (index == 1) {
            for (int i = 1; i =< players.size(); i++) {
                //check each players to the right of the empath from max to min
                PlayerPerformer player = players.get(players.size() - i);
                if (player.isAlive()) {
                    rightNeighbor = player;
                    break;
                }
            }
        } else {
            leftNeighbor = players.get(index - 2); // -2 for 0-indexed list
        }

        if (index == players.size()) {
            rightNeighbor = players.get(0); // Wrap around to first player
        } else {
            rightNeighbor = players.get(index); // index is already 0-indexed for right neighbor
        }

        List<PlayerPerformer> livingNeighbors = new ArrayList<>();
        if (leftNeighbor.isAlive()) {
            livingNeighbors.add(leftNeighbor);
        }
        if (rightNeighbor.isAlive()) {
            livingNeighbors.add(rightNeighbor);
        }
        return livingNeighbors;

    }


}