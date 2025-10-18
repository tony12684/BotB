package io.github.tony12684.BotB.Roles;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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


 //TODO implement action logging via SQL
public class Chef extends Role {
    public Chef() {
        super("Chef", "Townsfolk");
    }
    
    @Override
    public boolean firstNightAction(Game game) {
        PlayerPerformer chef = game.getPlayerByRole("Chef");
        if (chef == null) {
            throw new IllegalStateException("Chef player not found in game during first night action.");
        }
        try {
            Integer evilPairs = countEvilPairs(game);
            Player chefPlayer = Bukkit.getPlayer(chef.getUUID());
            chefPlayer.sendMessage("There are " + evilPairs + " pairs of evil players sitting next to each other.");
            return true;
        } catch (Exception e) {
            throw e; // rethrow after logging
        }
    }

    private Integer countEvilPairs(Game game) {
        // for each sequential pair of players in seating order, check if both are evil
        PlayerPerformer lastPlayer = null;
        int evilPairs = 0;
        List<PlayerPerformer> sortedPlayers = game.getPlayers();
        sortedPlayers = game.sortPlayersBySeatOrder(sortedPlayers);
        for (PlayerPerformer player : sortedPlayers) {
            if (lastPlayer != null) {
                if ((lastPlayer.getRole().getTeam().equals("Minion") || lastPlayer.getRole().getTeam().equals("Demon"))
                && (player.getRole().getTeam().equals("Minion") || player.getRole().getTeam().equals("Demon"))) {
                    evilPairs++;
                }
            }
            lastPlayer = player;
        }
        if (lastPlayer == null) {
            throw new IllegalStateException("Unexpected Null Player in Chef.countEvilPairs()");
        }
        if ((sortedPlayers.getFirst().getRole().getTeam().equals("Minion") || sortedPlayers.getFirst().getRole().getTeam().equals("Demon"))
        && (lastPlayer.getRole().getTeam().equals("Minion") || lastPlayer.getRole().getTeam().equals("Demon"))) {
            evilPairs++;
        }
        return evilPairs;
    }
}