package io.github.tony12684.BotB;

import java.util.UUID;

public class Performer {
    private UUID uuid; // Player's UUID
    private Role role; // Player's role in the game
    private String name; // Player's in game name
    public Performer(UUID uuid, Role role, String name) {
        // Constructor for Performer class
        this.uuid = uuid;
        this.role = role;
        this.name = name;
    }
    public UUID getUUID() {
        return uuid;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public String getName() {
        return name;
    }
}

