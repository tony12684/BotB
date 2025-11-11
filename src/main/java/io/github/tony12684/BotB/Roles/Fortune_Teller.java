package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            this.redHerring = game.getGrimoire().getFreeTargetsFromPerformer(
                game.getStoryteller(),
                1,
                "Select a good player to be the Red Herring for Fortune Teller.").getFirst();
            if (redHerring.getRole().getTeamActual().equals(Team.EVIL)) {
                game.getGrimoire().errorMessage(game.getStoryteller(), "Red Herring must be a good player. Please select again.");
                redHerring = null;
            }
        }
        List<Performer> targets = List.of(redHerring);
        return new ActionLog(game.getStoryteller(), "fortune_teller_red_herring", false, null, targets);
    }

    @Override
    public ActionLog firstNightAction(Game game) {
        return crystalBall(game);
    }
    @Override
    public ActionLog otherNightAction(Game game) {
        return crystalBall(game);
    }

    private ActionLog crystalBall(Game game) {
        // prompt FT for 2 targets
        List<PlayerPerformer> targets = game.getGrimoire().getFreeTargetsFromPerformer(
            game.getPlayerByRole("Fortune Teller"),
            2,
            "Select 2 players to search for a Demon.");
        // wait for some time to obscure drunk/poisoned status
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(7500) + 2500); // sleep 2.5 to 10 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        for (PlayerPerformer target : targets) {
            if (target.getUUID().equals(redHerring.getUUID())
            || target.getRole().getAffiliation(
                game.getGrimoire(),
                game.getPlayerByRole("Fortune Teller").getName(),
                "Fortune Teller",
                target.getName())
                .equals(Affiliation.DEMON)) 
            {
                game.getGrimoire().basicMessage(
                    game.getPlayerByRole("Fortune Teller"),
                    "You sense a Demon.");
                return new ActionLog(
                    game.getPlayerByRole("Fortune Teller"),
                    "fortune_teller",
                    false,
                    Boolean.toString(true),
                    new ArrayList<Performer>(targets));
            }
        }
        game.getGrimoire().basicMessage(
            game.getPlayerByRole("Fortune Teller"),
            "You do not sense a Demon.");
        return new ActionLog(
            game.getPlayerByRole("Fortune Teller"),
            "fortune_teller",
            false,
            Boolean.toString(false),
            new ArrayList<Performer>(targets));
    }

    @Override
    public ActionLog falseFirstNightAction(Game game) {
        return cloudyBall(game);
    }
    @Override
    public ActionLog falseOtherNightAction(Game game) {
        return cloudyBall(game);
    }

    private ActionLog cloudyBall(Game game) {
        // prompt FT for 2 targets
        List<PlayerPerformer> targets = game.getGrimoire().getFreeTargetsFromPerformer(
            game.getPlayerByRole("Fortune Teller"),
            2,
            "Select 2 players to search for a Demon.");
        // get result from storyteller
        boolean foundDemon = game.getGrimoire().getBooleanFromPerformer(
            game.getStoryteller(),
            "The Drunk/Poisoned Fortune Teller is searching for a Demon among " + targets.get(0).getName() + " and " + targets.get(1).getName() + ".\nWhat would you like to tell them?");
        // inform FT of result
        if (foundDemon) {
            game.getGrimoire().basicMessage(
                game.getStoryteller(),
                "You sense a Demon.");
        } else {
            game.getGrimoire().basicMessage(
                game.getStoryteller(),
                "You do not sense a Demon.");
        }
        return new ActionLog(
            game.getPlayerByRole("Fortune Teller"),
            "fortune_teller",
            true,
            Boolean.toString(foundDemon),
            new ArrayList<Performer>(targets));
    }
}