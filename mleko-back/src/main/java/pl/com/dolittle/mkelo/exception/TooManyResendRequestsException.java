package pl.com.dolittle.mkelo.exception;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;

public class TooManyResendRequestsException extends RuntimeException{

    public TooManyResendRequestsException(LocalDateTime requestTIme, LocalDateTime validRequestTIme) {
        super(MessageFormat.format("too_many_requests_try_again_after_{0}_hours",
                Duration.between(requestTIme, validRequestTIme).toHours()));
    }
}
