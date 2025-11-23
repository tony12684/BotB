package io.github.tony12684.BotB;

import java.util.List;
import java.util.UUID;

public class PlayerPerformer extends Performer{
    private boolean drunk = false; // Indicates if the player is drunk
    private List<Role> drinkLedger = new java.util.ArrayList<>(); // Ledger of roles that are currently responsible for the player's drunkenness
    private boolean poisoned = false; // Indicates if the player is poisoned
    private List<Role> poisonLedger = new java.util.ArrayList<>(); // Ledger of roles that are currently responsible for the player's poisoning
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
    public void setDrunk(boolean newDrunk, Role roleCausingDrunkenness) {
        if (!newDrunk && drinkLedger.size() > 1) {
            // if we are setting to sober, but there are multiple roles causing drunkenness, just remove this role from the ledger
            drinkLedger.remove(roleCausingDrunkenness);
        } else if (!newDrunk) {
            // If setting to sober
            drunk = false;
            drinkLedger.clear();
        }
        if (newDrunk) {
            // If setting to drunk
            drunk = true;
            if (!drinkLedger.contains(roleCausingDrunkenness)) {
                drinkLedger.add(roleCausingDrunkenness);
            }
        }
    }
    public List<Role> getDrinkLedger() {
        return drinkLedger;
    }
    public void setSober() {
        drunk = false;
        drinkLedger.clear();
    }

    public boolean getPoisoned() {
        return poisoned;
    }
    public void setPoisoned(boolean newPoisoned, Role roleCausingPoisoning) {
        if (!newPoisoned && poisonLedger.size() > 1) {
            // if we are setting to unpoisoned, but there are multiple roles causing poisoning, just remove this role from the ledger
            poisonLedger.remove(roleCausingPoisoning);
        } else if (!newPoisoned) {
            // If setting to unpoisoned
            poisoned = false;
            poisonLedger.clear();
        }
        if (newPoisoned) {
            // If setting to poisoned
            poisoned = true;
            if (!poisonLedger.contains(roleCausingPoisoning)) {
                poisonLedger.add(roleCausingPoisoning);
            }
        }
    }
    public List<Role> getPoisonLedger() {
        return poisonLedger;
    }
    public void setHealthy() {
        poisoned = false;
        poisonLedger.clear();
    }

    public void setSoberAndHealthy() {
        // Sets the player to both sober and healthy
        setSober();
        setHealthy();
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
