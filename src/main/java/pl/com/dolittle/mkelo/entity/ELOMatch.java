package pl.com.dolittle.mkelo.entity;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ELOMatch {

    private final List<Player> players = new ArrayList<>();

    public List<Player> getPlayers() {
        return this.players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void calculateELOs() {
        int n = players.size();
        float k = 32 / (float) (n - 1);

        for (int i = 0; i < n; i++) {
            int curPlace = players.get(i).getPlace();
            int curELO = players.get(i).getElo();

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    int opponentPlace = players.get(j).getPlace();
                    int opponentELO = players.get(j).getElo();

                    //work out S
                    float s;
                    if (curPlace < opponentPlace)
                        s = 1.0F;
                    else if (curPlace == opponentPlace)
                        s = 0.5F;
                    else
                        s = 0.0F;

                    //work out EA
                    float ea = 1 / (1.0f + (float) Math.pow(10.0f, (opponentELO - curELO) / 400.0f));

                    //calculate ELO change vs this one opponent, add it to our change bucket
                    //I currently round at this point, this keeps rounding changes symetrical between EA and EB, but changes K more than it should
                    players.get(i).addToElo(Math.round(k * (s - ea)));
                }
            }
            //add accumulated change to initial ELO for final ELO
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var player : players) {
            sb.append(player.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
