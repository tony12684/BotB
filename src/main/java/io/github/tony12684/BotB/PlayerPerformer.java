package io.github.tony12684.BotB;

public class PlayerPerformer extends Performer {
    private boolean drunk = false; // Indicates if the player is drunk
    private boolean poisoned = false; // Indicates if the player is poisoned
    private int seat; // Player's seat number
    public PlayerPerformer(String uuid, Role role) {
        // Constructor for PlayerPerformer class
        super(uuid, role);
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public boolean isDrunk() {
        return drunk;
    }

    public void setDrunk(boolean drunk) {
        this.drunk = drunk;
    }

    public boolean isPoisoned() {
        return poisoned;
    }

    public void setPoisoned(boolean poisoned) {
        this.poisoned = poisoned;
    }
}
