package io.github.tony12684.BotB;

public class Role {
    private String roleName; // Name of the role
    private Affiliation affiliation; // Townsfolk, Outsider, Minion, Demon, Traveler, etc.
    private Team team; // Good or Evil team
    private String startingMessage = "Have fun!"; // Message sent to player when assigned this role
    private int actionPriority; // Priority of the role's action during the night phase (lower number = higher priority)
    private Role falseRole; // A false role to display to the player instead of their actual role
    private boolean infoOverride = false; // Whether the role has an info override for actions like recluse

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
        this.roleName = roleName; // Role name requires matching spelling and spacing to role_ids.yaml to function properly with database queries
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
    
    public String getRoleNameActual() {
        // NOT for use with game logic that may involve lying roles
        return roleName;
    }

    public String getRoleName(Grimoire grimoire, String requestingName, String requestingRole, String targetName) {
        // requestingName for the player requesting the role name
        // requestingRole for what role is requesting the role name
        // game so that we can ask the storyteller if needed
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Team getTeamActual() {
        // NOT for use with game logic that may involve lying roles
        return team;
    }

    public Team getTeam(Grimoire grimoire, String requestingName, String requestingRole, String targetName) {
        // requestingName for the player requesting the team
        // requestingRole for what role is requesting the team
        // game so that we can ask the storyteller if needed
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Affiliation getAffiliationActual() {
        // NOT for use with game logic that may involve lying roles
        return affiliation;
    }

    public Affiliation getAffiliation(Grimoire grimoire, String requestingName, String requestingRole, String targetName) {
        // requestingName for the player requesting the affiliation
        // requestingRole for what role is requesting the affiliation
        // game so that we can ask the storyteller if needed
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

    public boolean getInfoOverride() {
        return infoOverride;
    }
    public void setInfoOverride(boolean infoOverride) {
        this.infoOverride = infoOverride;
    }

    // TODO overhaul setup and action methods to return information nessessary for logging the actions
    public ActionLog setup(Game game) {
        return null;
    }

    public ActionLog firstNightSetupMode(Game game) {
        return null;
    }

    public ActionLog firstNightAction(Game game) {
        return null;
    }

    public ActionLog falseFirstNightAction(Game game) {
        // For drunk and poisoned players
        return null;
    }

    public ActionLog otherNightAction(Game game){
        return null;
    }

    public ActionLog falseOtherNightAction(Game game) {
        // For drunk and poisoned players
        return null;
    }

    public ActionLog voteAction(Game game) {
        // For when you place a vote on a nominee
        return null;
    }

    public ActionLog falseVoteAction(Game game) {
        // For drunk and poisoned players
        return null;
    }

    public ActionLog voteNominateAction(Game game) {
        // For when you nominate a player for execution
        return null;
    }

    public ActionLog falseVoteNominateAction(Game game) {
        // For drunk and poisoned players
        return null;
    }

    public ActionLog voteNomineeAction(Game game) {
        // For when you are nominated for execution
        return null;
    }

    public ActionLog falseVoteNomineeAction(Game game) {
        // For drunk and poisoned players
        return null;
    }

}
