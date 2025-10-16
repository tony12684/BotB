package io.github.tony12684.BotB.Roles;

import java.util.List;

import io.github.tony12684.BotB.PlayerPerformer;
import io.github.tony12684.BotB.Role;
import io.github.tony12684.BotB.Game;

/*
 * Chef - Townsfolk
 * "You start knowing how many pairs of evil players there are."
 * No special setup required.
 * First night action.
 * No other night action.
 */

public class Chef extends Role {
    public Chef() {
        super("Chef", "Townsfolk");
    }
    
    @Override
    public boolean firstNightAction(Game game) {
        List<PlayerPerformer> players = game.getPlayers();
        PlayerPerformer chef = findChef(players);
        Integer evilPairs = countEvilPairs(game);
        return false;
    }
    private PlayerPerformer findChef(List<PlayerPerformer> players) {
        for (PlayerPerformer player : players) {
            if (player.getRole().getRoleName().equals("Chef")) {
                return player;
            }
        }
        // TODO: move this to game object as a universal function
        // TODO: throw error if chef not found
        return null; // Chef not found
    }
    private Integer countEvilPairs(Game game) {
        PlayerPerformer lastPlayer = null;
        int evilPairs = 0;
        List<PlayerPerformer> sortedPlayers = game.copyPlayers();
        sortedPlayers.sort((p1, p2) -> Integer.compare(p1.getSeat(), p2.getSeat()));
        // TODO: implement counting logic
        return evilPairs;
    }
}