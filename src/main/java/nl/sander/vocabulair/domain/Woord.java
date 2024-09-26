package nl.sander.vocabulair.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Woord {
    private String nederlands;
    private String vreemd;
    private boolean skip;
}
