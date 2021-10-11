package nl.sander.vocabulair.domain;


import nl.sander.vocabulair.services.WoordenService;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@ContextConfiguration(classes = WoordenService.class)
@TestPropertySource(properties = "woorden.json")
@ImportAutoConfiguration(WoordenService.class)
@JsonTest
class TalenAutoConfigurationTest {

    @org.junit.jupiter.api.Test
    void getTalen() {
    }
}
