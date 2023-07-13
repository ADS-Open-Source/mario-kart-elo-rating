package pl.com.dolittle.mkelo.exception;

import java.text.MessageFormat;

public class PlayerNameNotFoundException extends RuntimeException {

    public PlayerNameNotFoundException(String name) {
        super(MessageFormat.format("no_player_found_with_name_{0}", name));
    }
}
