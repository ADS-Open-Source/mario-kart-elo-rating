package pl.com.dolittle.mkelo.control.third;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ELOPlayer {

    private String name;
    private int place = 0;
    private int eloPre = 0;
    private int eloPost = 0;
    private int eloChange = 0;


    public ELOPlayer(String name, int place, int eloPre) {
        this.name = name;
        this.place = place;
        this.eloPre = eloPre;
    }

    public void addToEloPre(float number) {
        this.eloPre += number;
    }

    public void updateEloPost() {
        this.eloPost = this.eloPre + this.eloChange;
    }

    @Override
    public String toString() {
        return "ELOPlayer{" +
                "name='" + name + '\'' +
                ", place=" + place +
                ", eloPre=" + eloPre +
                ", eloPost=" + eloPost +
                ", eloChange=" + eloChange +
                '}';
    }
}

