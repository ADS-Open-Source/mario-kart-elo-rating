package pl.com.dolittle.mkelo.util;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
