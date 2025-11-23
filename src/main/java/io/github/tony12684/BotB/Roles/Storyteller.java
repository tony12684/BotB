package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Performer;
import io.github.tony12684.BotB.Role;

/*
 * Storyteller - standard, non-fabled
 * Setup action: design game start information.
 * First night action.
 * No other night action. Until prompted by other roles.
*/

public class Storyteller extends Role {
    public Storyteller(Performer performer) {
        super(performer, "Storyteller", Affiliation.STORYTELLER, Team.STORYTELLER);
    }
}
