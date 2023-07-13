package pl.com.dolittle.mkelo.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import pl.com.dolittle.mkelo.services.PersistenceService;

@Slf4j
@AllArgsConstructor
@Configuration
public class PersistenceConfig {

    private final PersistenceService persistenceService;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataFromS3() {
      log.info("loading insert statements");
      persistenceService.executeInsertStatements(null);
    }
}
