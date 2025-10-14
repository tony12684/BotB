package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

public class Drunk extends Role {
    public Drunk(String falseRole, String falseStartingMessage, String falseTeam) {
        super("Drunk", "Outsider", false, false);
        this.setFalseRole(falseRole);
        this.setFalseTeam(falseTeam);
        this.setFalseStartingMessage(falseStartingMessage);
    }
    //TODO Improve this so that false role info is provided after role creation but before role notification
    //TODO implement special setup logic
}
