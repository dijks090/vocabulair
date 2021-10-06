package nl.sander.vocabulair.domain;

import lombok.RequiredArgsConstructor;
import nl.sander.vocabulair.domain.dto.Woord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WoordenService {

    private final List<Woord> woorden;

    public List<Woord> getWoordenVoorTaal(String taal) {
        return woorden
                .stream()
                .filter(woord -> woord.getLang().equalsIgnoreCase(taal))
                .collect(Collectors.toList());
    }
}
