package pl.com.dolittle.mkelo.exception;

public class EmailConfigurationNotProvidedException extends RuntimeException{

    public EmailConfigurationNotProvidedException() {
        super("email_service_not_configured");
    }
}
