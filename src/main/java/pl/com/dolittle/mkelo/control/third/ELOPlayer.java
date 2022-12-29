package pl.com.dolittle.mkelo.control.third;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ELOPlayer {

    private String uuid;
    private int place = 0;
    private int elo = 0;


    public void addToElo(float number) {
        this.elo += number;
    }


    @Override
    public String toString() {
        return "ELOPlayer{" +
                "uuid='" + uuid + '\'' +
                ", place=" + place +
                ", elo=" + elo +
                '}';
    }
}

