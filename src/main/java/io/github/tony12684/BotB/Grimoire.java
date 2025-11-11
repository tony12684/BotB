package io.github.tony12684.BotB;

import java.util.List;

import io.github.tony12684.BotB.Role.Affiliation;
import io.github.tony12684.BotB.Role.Team;

public class Grimoire {
    private StorytellerPerformer storyteller;
    private Game game; //store game pointer reference so we don't need to pass it around
    /*
     * handles storing, updating, and displaying game information to players
     * prompts storyteller and players for inputs and actions
     */
    public Grimoire(StorytellerPerformer storyteller, Game game) {
        // Constructor for Grimoire class
        this.storyteller = storyteller;
        this.game = game;
    }

    public StorytellerPerformer getStoryteller() {
        return storyteller;
    }

    public void errorMessage(Performer performer, String message) {
        // Method to show error messages to a performer
    }

    public List<PlayerPerformer> getFreeTargetsFromPerformer(Performer actingPerformer, int numberOfTargets, String message) {
        // Method to get some number of unrestricted targets from a performer
        return null; // Placeholder return;
    }

    public int getNumberFromPerformer(Performer performer, String promptMessage) {
        // Method to get a number input from a performer
        return 0; // Placeholder return;
    }

    public boolean getBooleanFromPerformer(Performer performer, String promptMessage) {
        // Method to get a boolean input from a performer
        return false; // Placeholder return;
    }

    public Team getTeamFromPerformer(Performer performer, String promptMessage) {
        // Method to get the team of a performer
        return null; // Placeholder return;
    }

    public Affiliation getAffiliationFromPerformer(Performer performer, String promptMessage) {
        // Method to get the affiliation of a performer
        return null; // Placeholder return;
    }

    public void basicMessage(Performer performer, String message) {
        // Method to show action results to a performer
    }
}
