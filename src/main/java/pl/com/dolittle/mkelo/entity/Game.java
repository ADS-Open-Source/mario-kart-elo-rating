package pl.com.dolittle.mkelo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Game implements Serializable {

    private LocalDateTime reportedTime;
    private Player reportedBy;
    private List<List<Player>> ranking;
}
