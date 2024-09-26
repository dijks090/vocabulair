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

//        private BooleanProperty skip = new SimpleBooleanProperty(false);
//
//    public boolean isSkip() {
//        return skip.get();
//    }
//
//    public void setSkip(boolean skip) {
//        this.skip.set(skip);
//    }
}
