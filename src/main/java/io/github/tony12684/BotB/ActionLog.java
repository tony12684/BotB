package io.github.tony12684.BotB;
import java.util.List;

public class ActionLog {
    private String performerUUID;
    private String performerTeamName;
    private String performerRoleName;
    private String actionType;
    private boolean actionContainsLie;
    private boolean actionHasTargets;
    private String actionNotes;
    private List<Performer> actionTargets;

    public ActionLog(Performer actingPerformer, String actionType, boolean actionContainsLie, String actionNotes, List<Performer> actionTargets) {
        this.performerUUID = actingPerformer.getUUID();
        this.performerTeamName = actingPerformer.getRole().getTeam().toString().toLowerCase();
        this.performerRoleName = actingPerformer.getRole().getRoleName().toLowerCase().replace("_", " ");
        this.actionType = actionType;
        this.actionContainsLie = actionContainsLie;
        this.actionHasTargets = (actionTargets == null || !actionTargets.isEmpty());
        this.actionNotes = actionNotes;
        this.actionTargets = actionTargets;
    }


    public String getPerformerUUID() {
        return performerUUID;
    }

    public String getPerformerRoleName() {
        return performerRoleName;
    }

    public String getPerformerTeamName() {
        return performerTeamName;
    }

    public String getActionType() {
        return actionType;
    }

    public boolean getActionContainsLie() {
        return actionContainsLie;
    }

    public boolean getActionHasTargets() {
        return actionHasTargets;
    }

    public String getActionNotes() {
        return actionNotes;
    }

    public List<Performer> getActionTargets() {
        return actionTargets;
    }
}
