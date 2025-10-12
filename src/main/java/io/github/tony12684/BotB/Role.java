package io.github.tony12684.BotB;

public class Role {
    private String roleName; // Name of the role
    private String team; // Default team affiliation
    private String startingMessage = "Have fun!"; // Message sent to player when assigned this role
    private boolean firstNight; // Indicates if the role has a first night action
    private boolean allNight; // indicates if the role has actions every night
    private int actionPriority; // Priority of the role's action during the night phase (lower number = higher priority)
    private boolean specialSetup = false; // Indicates if the role requires special setup

    public Role(String roleName, String team, boolean firstNight, boolean allNight) {
        // Constructor for Role class
        this.roleName = roleName;
        this.team = team;
        this.firstNight = firstNight;
        this.allNight = allNight;
    }
    public Role(String roleName, String team, boolean firstNight, String startingMessage, boolean allNight) {
        // Constructor for Role class with starting message
        this.roleName = roleName;
        this.team = team;
        this.firstNight = firstNight;
        this.allNight = allNight;
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

    public boolean getSpecialSetup() {
        return specialSetup;
    }

    public void setSpecialSetup(boolean specialSetup) {
        this.specialSetup = specialSetup;
    }

    public boolean getFirstNight() {
        return firstNight;
    }

    public boolean getAllNight() {
        return allNight;
    }

    public boolean firstNightAction() {
        // Default implementation does nothing
        // Override this method in subclasses for specific first night actions
        // return true if action was successful, false otherwise
        return false;
    }

    public int getActionPriority() {
        return actionPriority;
    }

    public void setActionPriority(int actionPriority) {
        this.actionPriority = actionPriority;
    }
}
