package nl.sander.vocabulair.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sander.vocabulair.domain.WoordenService;
import nl.sander.vocabulair.domain.dto.Woord;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
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

    private List<Woord> woorden;
    private Woord gekozenWoord;
    private File selectedFile;

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
        this.show.setOnAction(actionEvent -> this.labelrechts.setText(gekozenWoord.getVreemd()));
        this.quit.setOnAction(actionEvent -> System.exit(0));
        this.open.setOnAction(
                actionEvent -> {
                    FileChooser fc = new FileChooser();
                    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
                    selectedFile = fc.showOpenDialog(null);
                    if(selectedFile != null) {
                        filelabel.setText("Je oefent nu: " + selectedFile.getName().split("\\.")[0].toUpperCase());
                        woorden = woordenService.getWoorden(selectedFile);
                        populateLabel();
                    } else {
                        log.debug("File is not valid!");
                    }
                }
        );
//        this.skip.setOnAction(actionEvent -> {});
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

}
