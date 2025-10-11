package io.github.tony12684.BotB;


public class Performer {
    private String uuid; // Player's UUID
    private Role role; // Player's role in the game
    public Performer(String uuid, Role role) {
        // Constructor for Performer class
        this.uuid = uuid;
        this.role = role;
    }
    public String getUUID() {
        return uuid;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}

