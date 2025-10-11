package io.github.tony12684.BotB;

public class Grimoire {
    private String storytellerUUID;
    /*
     * handles storing, updating, and displaying game information to the storyteller and spy
     * prompts storyteller for actions
     */
    public Grimoire(String storytellerUUID) {
        // Constructor for Grimoire class
        this.storytellerUUID = storytellerUUID;
    }

    public String getStorytellerUUID() {
        return storytellerUUID;
    }
}
