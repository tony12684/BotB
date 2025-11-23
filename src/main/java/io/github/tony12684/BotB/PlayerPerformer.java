package io.github.tony12684.BotB;

import java.util.UUID;

public class PlayerPerformer extends Performer{
    private boolean drunk = false; // Indicates if the player is drunk
    private boolean poisoned = false; // Indicates if the player is poisoned
    private boolean alive = true; // Indicates if the player is alive
    private int seat; // Player's seat number
    // Left neighbor is +1 seat, right neighbor is -1 seat by default
    private PlayerPerformer leftNeighbor = null; // Reference to the left neighbor
    private PlayerPerformer rightNeighbor = null; // Reference to the right neighbor
    public PlayerPerformer(UUID uuid, Role role, String name) {
        // Constructor for PlayerPerformer class
        super(uuid, role, name);
    }

    public boolean getDrunk() {
        return drunk;
    }
    public void setDrunk(boolean drunk) {
        this.drunk = drunk;
    }

    public boolean getPoisoned() {
        return poisoned;
    }
    public void setPoisoned(boolean poisoned) {
        this.poisoned = poisoned;
    }
    
    public boolean getAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getSeat() {
        return seat;
    }
    public void setSeat(int seat) {
        this.seat = seat;
    }

    public PlayerPerformer getLeftNeighbor() {
        return leftNeighbor;
    }
    public void setLeftNeighbor(PlayerPerformer leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public PlayerPerformer getRightNeighbor() {
        return rightNeighbor;
    }
    public void setRightNeighbor(PlayerPerformer rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }
}
