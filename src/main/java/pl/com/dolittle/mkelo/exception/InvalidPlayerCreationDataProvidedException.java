package pl.com.dolittle.mkelo.exception;

public class InvalidPlayerCreationDataProvidedException extends IllegalArgumentException {

    public InvalidPlayerCreationDataProvidedException() {
        super("invalid_player_creation_data_provided");
    }
}
