package pl.com.dolittle.mkelo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<String> handleAuthenticationFailedException(AuthenticationFailedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorisedAction.class)
    public ResponseEntity<String> handleUnauthorisedActionException(UnauthorisedAction e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PlayerUUIDNotFoundException.class)
    public ResponseEntity<String> handlePlayerUUIDNotFoundException(PlayerUUIDNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerSecretNotFoundException.class)
    public ResponseEntity<String> handlePlayerSecretNotFoundException(PlayerSecretNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerEmailNotFoundException.class)
    public ResponseEntity<String> handlePlayerEmailNotFoundException(PlayerEmailNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerNameNotFoundException.class)
    public ResponseEntity<String> handlePlayerNameNotFoundException(PlayerNameNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPlayerCreationDataProvidedException.class)
    public ResponseEntity<String> handleInvalidPlayerCreationDataProvidedException(InvalidPlayerCreationDataProvidedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public ResponseEntity<String> handlePlayerAlreadyExistsException(PlayerAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TooManyResendRequestsException.class)
    public ResponseEntity<String> handleTooManyResendRequestsException(TooManyResendRequestsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }
}
