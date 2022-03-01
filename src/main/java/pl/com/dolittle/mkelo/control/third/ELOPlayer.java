package pl.com.dolittle.mkelo.control.third;

public class ELOPlayer
{
    public String name;
    
    public int place     = 0;
    public int eloPre    = 0;
    public int eloPost   = 0;
    public int eloChange = 0;

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

