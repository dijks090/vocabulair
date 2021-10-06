package nl.sander.vocabulair.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.sander.vocabulair.domain.dto.Woord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.List;

@Configuration
@Import(WoordenService.class)
public class TalenAutoConfiguration {

    @Bean
    public List<Woord> getTalen(Jackson2ObjectMapperBuilder objectMapper,
                                @Value("woorden.json") ClassPathResource resource) throws IOException {
        var mapper = objectMapper.build();
        try (var stream = resource.getInputStream()) {
                return mapper.readValue(stream, new TypeReference<>() {
            });
        }
    }
}
