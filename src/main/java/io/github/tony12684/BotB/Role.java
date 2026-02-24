package io.github.tony12684.BotB;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Role {
    private Performer performer; // The performer (player) assigned to this role
    private String roleName; // Name of the role
    private Affiliation affiliation; // Townsfolk, Outsider, Minion, Demon, Traveler, etc.
    private Team team; // Good or Evil team
    private String startingMessage = "Have fun!"; // Message sent to player when assigned this role
    private int actionPriority = 0; // Priority of the role's action during the night phase (lower number = higher priority)
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
        // Ensure this matches Main.initTeamsInDB() teams table.
        GOOD,
        EVIL,
        STORYTELLER
    }

    public Role(Performer performer, String roleName, Affiliation affiliation, Team team) {
        // Constructor for Role class
        this.performer = performer;
        this.roleName = roleName; // Role name requires matching spelling and spacing to role_ids.yaml to function properly with database queries
        this.affiliation = affiliation;
        this.team = team;
    }
    public Role(Performer performer, String roleName, Affiliation affiliation, Team team, String startingMessage) {
        // Constructor for Role class with starting message
        this.performer = performer;
        this.roleName = roleName;
        this.affiliation = affiliation;
        this.team = team;
        this.startingMessage = startingMessage;
    }

    public Performer getPerformer() {
        return performer;
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
    public void setAffiliation(Affiliation newAffiliation) {
        this.affiliation = newAffiliation;
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

    public String getStartingMessage() {
        return startingMessage;
    }
    
    public int getActionPriority() {
        return actionPriority;
    }
    public void setActionPriority(int newActionPriority) {
        this.actionPriority = newActionPriority;
    }

    public Role getFalseRole() {
        return falseRole;
    }
    public void setFalseRole(Role newFalseRole) {
        this.falseRole = newFalseRole;
    }

    public boolean getInfoOverride() {
        return infoOverride;
    }
    public void setInfoOverride(boolean newInfoOverride) {
        this.infoOverride = newInfoOverride;
    }

    public CompletableFuture<List<ActionLog>> setup(Game game) {
        // intended to run many times per action
        // take care to handle multiple calls correctly
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> firstNightSetup(Game game) {
        // for setup of first night actions
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> firstNightAction(Game game) {
        // For first night actions
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> falseFirstNightAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> otherNightAction(Game game){
        // For regular night actions
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> falseOtherNightAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> voteNominateAction(Game game) {
        // For when you nominate a player for execution
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }
    public CompletableFuture<List<ActionLog>> falseVoteNominateAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> voteNomineeAction(Game game) {
        // For when you are nominated for execution
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }
    public CompletableFuture<List<ActionLog>> falseVoteNomineeAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> voteAction(Game game) {
        // For when you place a vote on a nominee
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }
    public CompletableFuture<List<ActionLog>> falseVoteAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> dawnAction(Game game) {
        // For dawn triggers
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }
    public CompletableFuture<List<ActionLog>> falseDawnAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> duskAction(Game game) {
        // For dusk triggers
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }
    public CompletableFuture<List<ActionLog>> falseDuskAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> executionAction(Game game) {
        // For when you are executed
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }
    public CompletableFuture<List<ActionLog>> falseExecutionAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> nightDeathAction(Game game) {
        // For when you die at night
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }
    public CompletableFuture<List<ActionLog>> falseNightDeathAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }

    public CompletableFuture<List<ActionLog>> dayDeathAction(Game game) {
        // For when you die during the day
        // I think no role uses this yet
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }
    public CompletableFuture<List<ActionLog>> falseDayDeathAction(Game game) {
        // For drunk and poisoned players
        CompletableFuture<List<ActionLog>> future = new CompletableFuture<>();
        return future;
    }
}
