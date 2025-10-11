package io.github.tony12684.BotB;

public class Role {
    private String roleName; // Name of the role
    //TODO add nighttime priority as an int?
    public Role(String roleName) {
        // Constructor for Role class
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
