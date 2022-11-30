package pl.com.dolittle.mkelo.control.third;

import java.util.ArrayList;
import java.util.Objects;

public class ELOMatch {

    private final ArrayList<ELOPlayer> players = new ArrayList<>();

    public void addPlayer(String name, int place, int elo) {
        ELOPlayer player = new ELOPlayer(name, place, elo);
        players.add(player);
    }

    public int getELO(String name) {
        for (ELOPlayer p : players) {
            if (Objects.equals(p.getName(), name))
                return p.getEloPost();
        }
        return 1500;
    }

    public int getELOChange(String name) {
        for (ELOPlayer p : players) {
            if (p.getName().equals(name))
                return p.getEloChange();
        }
        return 0;
    }

    public boolean checkIfIsPlayer(String name) {

        for (ELOPlayer p : players) {
            if (Objects.equals(p.getName(), name))
                return true;
        }
        return false;
    }

    public void calculateELOs() {
        int n = players.size();
        float K = 32 / (float) (n - 1);

        for (int i = 0; i < n; i++) {
            int curPlace = players.get(i).getPlace();
            int curELO = players.get(i).getEloPre();

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    int opponentPlace = players.get(j).getPlace();
                    int opponentELO = players.get(j).getEloPre();

                    //work out S
                    float S;
                    if (curPlace < opponentPlace)
                        S = 1.0F;
                    else if (curPlace == opponentPlace)
                        S = 0.5F;
                    else
                        S = 0.0F;

                    //work out EA
                    float EA = 1 / (1.0f + (float) Math.pow(10.0f, (opponentELO - curELO) / 400.0f));

                    //calculate ELO change vs this one opponent, add it to our change bucket
                    //I currently round at this point, this keeps rounding changes symetrical between EA and EB, but changes K more than it should
                    players.get(i).addToEloPre(Math.round(K * (S - EA)));
                }
            }
            //add accumulated change to initial ELO for final ELO
            players.get(i).updateEloPost();
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
