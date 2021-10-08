package nl.sander.vocabulair.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sander.vocabulair.domain.WoordenService;
import nl.sander.vocabulair.domain.dto.Woord;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleUiController {

    private final WoordenService woordenService;

    @FXML
    public TextField labellinks;

    @FXML
    public TextField labelrechts;

    @FXML
    public Button file;

    @FXML
    public Label filelabel;

    @FXML
    public Button next;

    @FXML
    public Button show;

    private List<Woord> woorden;
    private Woord gekozenWoord;
    private File selectedFile;

    @FXML
    public void initialize() {
        populateKnoppen();
    }

    private void populateLabel(){
        gekozenWoord = getRandomWoord();
        this.labellinks.setText(gekozenWoord.getNederlands());
        this.labelrechts.setText("");
    }

    private void populateKnoppen() {
        this.next.setOnAction(actionEvent -> populateLabel());
        this.show.setOnAction(actionEvent -> this.labelrechts.setText(gekozenWoord.getVreemd()));
        this.file.setOnAction(
                actionEvent -> {
                    FileChooser fc = new FileChooser();
                    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
                    selectedFile = fc.showOpenDialog(null);
                    if(selectedFile != null) {
                        filelabel.setText(selectedFile.getName());
                        woorden = woordenService.getWoorden(selectedFile);
                        populateLabel();
                    } else {
                        log.debug("File is not valid!");
                    }
                }
        );
    }

    private Woord getRandomWoord() {
        Random random = new Random();
        int index = random.nextInt(woorden.size());
        Woord nieuwWoord = woorden.get(index);
        if (gekozenWoord != null && gekozenWoord.equals(nieuwWoord)) {
            return getRandomWoord();
        }
        return woorden.get(index);
    }

}
