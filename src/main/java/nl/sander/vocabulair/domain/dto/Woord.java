package nl.sander.vocabulair.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Woord {
    private String nederlands;
    private String vreemd;
}
