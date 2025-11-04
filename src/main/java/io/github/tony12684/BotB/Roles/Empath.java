package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
* Empath - Townsfolk
* "Each night, you learn how many of your 2 alive neighbors are evil."
* No special setup.
* First night action.
* Other night action.
 */

public class Empath extends Role {
    public Empath() {
        super("Empath", "Townsfolk");
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