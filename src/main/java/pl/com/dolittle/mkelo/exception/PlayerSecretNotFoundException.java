package pl.com.dolittle.mkelo.exception;

import java.text.MessageFormat;

public class PlayerSecretNotFoundException extends RuntimeException{

    public PlayerSecretNotFoundException(String secret) {
        super(MessageFormat.format("no_player_found_with_secret_{0}", secret));
    }
}
