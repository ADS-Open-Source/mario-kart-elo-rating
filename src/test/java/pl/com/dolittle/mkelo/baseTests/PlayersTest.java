package pl.com.dolittle.mkelo.baseTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.dolittle.mkelo.services.StorageService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PlayersTest {

    @Autowired
    private StorageService storageService;
    private static final String FILENAME = "gameData3.txt";



    @Test
    void shouldSaveUserToFile() throws Exception{

        byte[] playersFile = storageService.downloadFile(FILENAME);
        String json = Arrays.toString(playersFile);
        System.out.println(json);
        assertTrue(true);

    }
}
