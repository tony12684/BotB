package io.github.tony12684.BotB;
import java.util.List;

public class linkTest {

    private void linkNeighbors(List<String> players) {
        // Link each player to their left and right neighbors based on seat order
        //List<String> players = sortPlayersBySeatOrder(players);
        // Left neighbor is +1 seat, right neighbor is -1 seat
        int playerCount = players.size();
        for (int i = 0; i < playerCount; i++) {
            String currentPlayer = players.get(i);
            String leftNeighbor;
            String rightNeighbor;
            // first persons right neighbor is last person
            if (i == 0) {
                rightNeighbor = players.get(playerCount - 1);
            } else {
                rightNeighbor = players.get(i - 1);
            }
            // last persons left neighbor is first person
            if (i == playerCount - 1) {
                leftNeighbor = players.get(0);
            } else {
                leftNeighbor = players.get(i + 1);
            }
            System.err.println("Player: " + currentPlayer + ", i: " + i + ", Left Neighbor: " + leftNeighbor + ", Right Neighbor: " + rightNeighbor);
            //currentPlayer.setLeftNeighbor(leftNeighbor);
            //currentPlayer.setRightNeighbor(rightNeighbor);
        }
    }

    public static void main(String[] args) {
        linkTest test = new linkTest();
        List<String> players = List.of("A");
        test.linkNeighbors(players);
    }
}

