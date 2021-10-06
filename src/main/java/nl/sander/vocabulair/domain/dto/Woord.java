package nl.sander.vocabulair.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Woord {
    @JsonProperty("nl")
    private String nederlands;
    @JsonProperty("vr")
    private String vreemd;
    private String lang;
}
