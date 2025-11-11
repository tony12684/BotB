package io.github.tony12684.BotB.Roles;
import io.github.tony12684.BotB.Role;

/*
* Librarian - Townsfolk
* "You start knowing that 1 of 2 players is a particular Outsider. (Or that zero are in play.)"
* No special setup.
* First night action.
* No other night action.
*/

public class Librarian extends Role {
    public Librarian() {
        super("Librarian", Affiliation.TOWNSFOLK, Team.GOOD);
    }
}
