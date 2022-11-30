package pl.com.dolittle.mkelo.exception;

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException() {
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
