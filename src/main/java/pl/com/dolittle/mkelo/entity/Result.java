package pl.com.dolittle.mkelo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result implements Serializable {

    private Player player;
    private int eloBefore;
    private int eloAfter;
}
