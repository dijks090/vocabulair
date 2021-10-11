package nl.sander.vocabulair.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sander.vocabulair.services.SoundService;
import nl.sander.vocabulair.services.WoordenService;
import nl.sander.vocabulair.domain.Woord;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Random;

import static java.lang.String.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleUiController {

    private final SoundService soundService;
    private final WoordenService woordenService;
    @FXML
    public TextField labellinks;
    @FXML
    public TextField labelrechts;
    @FXML
    public MenuBar menuBar;
    @FXML
    public Menu menu;
    @FXML
    public MenuItem open;
    @FXML
    public MenuItem quit;
    @FXML
    public Label filelabel;
    @FXML
    public Button next;
    @FXML
    public Button show;
    @FXML
    public CheckBox skip;
    @FXML
    public MenuItem actief;
    @FXML
    public MenuItem schrijven;

    private List<Woord> woorden;
    private Woord gekozenWoord;
    private File selectedFile;
    private TypeOefening typeOefing = TypeOefening.ACTIEF;

    @FXML
    public void initialize() {
        log.debug("initialize");
        populateKnoppen();
    }

    private void populateLabel() {
        gekozenWoord = getRandomWoord();
        this.labellinks.setText(gekozenWoord.getNederlands());
        this.labelrechts.setText("");
    }

    private void populateKnoppen() {
        this.next.setOnAction(actionEvent -> {
            gekozenWoord.setSkip(skip.isSelected());
            skip.setSelected(false);
            populateLabel();
        });
        this.show.setOnAction(actionEvent -> {
            if (TypeOefening.SCHRIJVEN.equals(typeOefing)) {
                if (gekozenWoord.getVreemd().equals(labelrechts.getText())) {
                    log.debug("CORRECT");
                    soundService.cheer();
                } else {
                    log.debug("FOUT");
                }
            }
            this.labelrechts.setText(gekozenWoord.getVreemd());
        });
        this.quit.setOnAction(actionEvent -> System.exit(0));
        this.open.setOnAction(
                actionEvent -> {
                    FileChooser fc = new FileChooser();
                    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
                    selectedFile = fc.showOpenDialog(null);
                    if(selectedFile != null) {
                        updateKnopTeksten();
                        woorden = woordenService.getWoorden(selectedFile);
                        populateLabel();
                    } else {
                        log.debug("File is not valid!");
                    }
                }
        );
        this.actief.setOnAction(actionEvent -> {
            typeOefing = TypeOefening.ACTIEF;
            updateKnopTeksten();
            log.debug("actief");
        });
        this.schrijven.setOnAction(actionEvent -> {
            typeOefing = TypeOefening.SCHRIJVEN;
            updateKnopTeksten();
            log.debug("schrijven");
        });
    }

    private void updateKnopTeksten() {
        filelabel.setText(format("Je oefent nu %s, %s", selectedFile.getName().split("\\.")[0].toUpperCase(), typeOefing.naam));
        show.setText(typeOefing.knopLabel);
    }

    private Woord getRandomWoord() {
        if (woorden.stream().allMatch(Woord::isSkip)){
            return Woord.builder().nederlands("Topper!").vreemd("alles gedaan!").build();
        }
        Random random = new Random();
        int index = random.nextInt(woorden.size());
        Woord nieuwWoord = woorden.get(index);
        if (gekozenWoord != null && gekozenWoord.equals(nieuwWoord) || nieuwWoord.isSkip()) {
            return getRandomWoord();
        }
        return woorden.get(index);
    }

    @AllArgsConstructor
    public enum TypeOefening {
        ACTIEF("actief", "show me"), SCHRIJVEN("schrijven", "controleer");

        private String naam;
        private String knopLabel;
    }

}
