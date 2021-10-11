package nl.sander.vocabulair.services;

import nl.sander.vocabulair.domain.Woord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WoordenServiceTest {

    @Autowired
    WoordenService woordenService;
    @Autowired
    ResourceLoader resourceLoader;

    @Test
    void getWoorden() throws IOException {
        File file = resourceLoader.getResource("classpath:taal.txt").getFile();
        List<Woord> woorden = woordenService.getWoorden(file);
        assertThat(woorden).hasSize(1);
    }
}
