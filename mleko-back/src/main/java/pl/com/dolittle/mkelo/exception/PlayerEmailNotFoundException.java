package pl.com.dolittle.mkelo.exception;

import java.text.MessageFormat;

public class PlayerEmailNotFoundException extends RuntimeException {

    public PlayerEmailNotFoundException(String email) {
        super(MessageFormat.format("no_player_found_with_email_{0}", email));
    }
}
