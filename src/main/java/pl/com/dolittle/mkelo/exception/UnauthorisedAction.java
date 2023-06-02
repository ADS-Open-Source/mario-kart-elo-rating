package pl.com.dolittle.mkelo.exception;

public class UnauthorisedAction extends RuntimeException {

    public UnauthorisedAction() {
        super("not_authorised_to_perform_this_action");
    }
}