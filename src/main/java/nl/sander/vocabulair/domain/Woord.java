package nl.sander.vocabulair.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private BooleanProperty skap = new SimpleBooleanProperty(false);

    public BooleanProperty skapProperty() {
       return skap;
    }
}
