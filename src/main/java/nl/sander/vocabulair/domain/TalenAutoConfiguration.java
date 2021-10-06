package nl.sander.vocabulair.domain;

import nl.sander.vocabulair.domain.dto.Talen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

@Configuration
//@Import(AowLeeftijdViaStamtabel.class) TODO
public class TalenAutoConfiguration {

    @Bean
    public Talen getTalen(Jackson2ObjectMapperBuilder objectMapper,
                          @Value("woorden.json") ClassPathResource resource) throws IOException {
        var mapper = objectMapper.build();
        try (var stream = resource.getInputStream()) {
            return mapper.readValue(stream, Talen.class);
        }
    }
}
