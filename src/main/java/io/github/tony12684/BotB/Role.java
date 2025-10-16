package io.github.tony12684.BotB;

public class Role {
    private String roleName; // Name of the role
    private String team; // Default team affiliation
    private String startingMessage = "Have fun!"; // Message sent to player when assigned this role
    private int actionPriority; // Priority of the role's action during the night phase (lower number = higher priority)
    private Role falseRole; // A false role to display to the player instead of their actual role

    public Role(String roleName, String team) {
        // Constructor for Role class
        this.roleName = roleName;
        this.team = team;
    }
    public Role(String roleName, String team, String startingMessage) {
        // Constructor for Role class with starting message
        this.roleName = roleName;
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Role getFalseRole() {
        return falseRole;
    }
    public void setFalseRole(Role falseRole) {
        this.falseRole = falseRole;
    }

    public boolean setup(Game game) {
        // Default implementation does nothing
        // Override this method in subclasses for specific setup logic
        // return true if setup was performed successfully, false otherwise
        return false;
    }

    public boolean firstNightAction(Game game) {
        // Default implementation does nothing
        // Override this method in subclasses for specific first night actions
        // return true if action was performed successfully, false otherwise
        return false;
    }

    public boolean otherNightAction(Game game){
        // Default implementation does nothing
        // Override this method in subclasses for specific other night actions
        // return true if action was performed successfully, false otherwise
        return false;
    }

    public int getActionPriority() {
        return actionPriority;
    }

    public void setActionPriority(int actionPriority) {
        this.actionPriority = actionPriority;
    }
}
