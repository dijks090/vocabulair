package nl.sander.vocabulair.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Woord {
    private String nederlands;
    private String vreemd;
    private boolean skip;
}
