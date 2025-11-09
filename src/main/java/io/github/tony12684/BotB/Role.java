package io.github.tony12684.BotB;

public class Role {
    private String roleName; // Name of the role
    private Affiliation affiliation; // Townsfolk, Outsider, Minion, Demon, Traveler, etc.
    private Team team; // Good or Evil team
    private String startingMessage = "Have fun!"; // Message sent to player when assigned this role
    private int actionPriority; // Priority of the role's action during the night phase (lower number = higher priority)
    private Role falseRole; // A false role to display to the player instead of their actual role

    public enum Affiliation {
        TOWNSFOLK,
        OUTSIDER,
        MINION,
        DEMON,
        TRAVELER,
        STORYTELLER
    }

    public enum Team {
        GOOD,
        EVIL,
        STORYTELLER
    }

    public Role(String roleName, Affiliation affiliation, Team team) {
        // Constructor for Role class
        this.roleName = roleName;
        this.affiliation = affiliation;
        this.team = team;
    }
    public Role(String roleName, Affiliation affiliation, Team team, String startingMessage) {
        // Constructor for Role class with starting message
        this.roleName = roleName;
        this.affiliation = affiliation;
        this.team = team;
        this.startingMessage = startingMessage;
    }

    public String getStartingMessage() {
        return startingMessage;
    }
    
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Affiliation getAffiliation() {
        return affiliation;
    }
    public void setAffiliation(Affiliation affiliation) {
        this.affiliation = affiliation;
    }

    public Role getFalseRole() {
        return falseRole;
    }
    public void setFalseRole(Role falseRole) {
        this.falseRole = falseRole;
    }
    
    public int getActionPriority() {
        return actionPriority;
    }

    public void setActionPriority(int actionPriority) {
        this.actionPriority = actionPriority;
    }

    // TODO overhaul setup and action methods to return information nessessary for logging the actions
    public boolean setup(Game game) {
        return false;
    }

    public boolean firstNightAction(Game game) {
        return false;
    }

    public boolean otherNightAction(Game game){
        return false;
    }

    public boolean voteAction(Game game) {
        // For when you place a vote on a nominee
        return false;
    }

    public boolean voteNominateAction(Game game) {
        // For when you nominate a player for execution
        return false;
    }

    public boolean voteNomineeAction(Game game) {
        // For when you are nominated for execution
        return false;
    }

}
