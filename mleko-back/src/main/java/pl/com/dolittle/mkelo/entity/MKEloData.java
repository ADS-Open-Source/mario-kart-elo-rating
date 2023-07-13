package pl.com.dolittle.mkelo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MKEloData implements Serializable {

    private transient String filename;
    private List<Player> players;
    private List<Game> games;

    public void updateData(List<Player> players, List<Game> games) {
        this.players = players;
        this.games = games;
    }
}
