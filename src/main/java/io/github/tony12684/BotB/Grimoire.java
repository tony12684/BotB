package io.github.tony12684.BotB;

import java.util.List;

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

    public List<PlayerPerformer> getFreeTargetsFromPlayer(Performer actingPerformer, int numberOfTargets, String message) {
        // Method to get some number of unrestricted targets from a performer
        return null; // Placeholder return;
    }

    public int getNumber(Performer performer, String promptMessage) {
        // Method to get a number input from a performer
        return 0; // Placeholder return;
    }

    public void basicMessage(Performer performer, String message) {
        // Method to show action results to a performer
    }
}
