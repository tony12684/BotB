package io.github.tony12684.BotB;

import java.util.List;

/*
 * This class represents a team in the game, holding its members.
 */
public class Team {
    private List<PlayerPerformer> members;

    public Team(List<String> uuids) {
        // Constructor for Team class
        for (String uuid : uuids) {
            members.add(new PlayerPerformer(uuid));
        }
    }

    public List<PlayerPerformer> getMembers() {
        return members;
    }
}
