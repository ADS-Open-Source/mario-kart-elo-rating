package pl.com.dolittle.mkelo.exception;

import java.text.MessageFormat;

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException() {
    }

    public AuthenticationFailedException(String secret) {
        super(MessageFormat.format("no player found with secret {}", secret));
    }
}
