package pl.com.dolittle.mkelo.exception;

import java.text.MessageFormat;

public class PlayerAlreadyExistsException extends IllegalArgumentException {

    public PlayerAlreadyExistsException(String username, String email) {
        super(MessageFormat.format("player_with_username_{0}_or_email_{1}_already_exists", username, email));
    }
}
