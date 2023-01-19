package pl.com.dolittle.mkelo.exception;

import java.text.MessageFormat;

public class PlayerUUIDNotFoundException extends RuntimeException{

    public PlayerUUIDNotFoundException(String uuid) {
        super(MessageFormat.format("no_player_found_with_uuid_{0}", uuid));
    }
}
