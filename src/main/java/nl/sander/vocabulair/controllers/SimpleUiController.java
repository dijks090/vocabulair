package nl.sander.vocabulair.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sander.vocabulair.domain.Woord;
import nl.sander.vocabulair.services.GiphyService;
import nl.sander.vocabulair.services.SoundService;
import nl.sander.vocabulair.services.WoordenService;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Random;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleUiController {


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
    public MenuItem passief;
    @FXML
    public MenuItem schrijven;
    @FXML
    public ImageView imageview;

    private final SoundService soundService;
    private final WoordenService woordenService;
    private final GiphyService giphyService;
    private List<Woord> woorden = List.of();
    private Woord gekozenWoord;
    private File selectedFile;
    private TypeOefening typeOefing = TypeOefening.SCHRIJVEN;
    Random random = new Random();

    @FXML
    public void initialize() {
        log.debug("initialize");
        populateKnoppen();
    }

    private String getLinkerTekst(Woord woord) {
        return typeOefing == TypeOefening.PASSIEF ? woord.getVreemd() : woord.getNederlands();
    }

    private String getRechterTekst(Woord woord) {
        return typeOefing == TypeOefening.PASSIEF ? woord.getNederlands() : woord.getVreemd();
    }

    private void populateLabel() {
        gekozenWoord = getRandomWoord();
        this.labellinks.setText(getLinkerTekst(gekozenWoord));
        this.labelrechts.setText("");
        long aantalToGo = woorden
                .stream()
                .filter(woord -> !woord.isSkip())
                .count();

        this.filelabel.setText(
                format("Je oefent nu %s, %s. Nog %d te gaan",
                        selectedFile.getName().split("\\.")[0].toUpperCase(),
                        typeOefing.naam,
                        aantalToGo));
    }

    private void populateKnoppen() {
        this.next.setOnAction(actionEvent -> {
            gekozenWoord.setSkip(skip.isSelected());
            skip.setSelected(false);
            populateLabel();
        });
        this.show.setOnAction(actionEvent -> {
            if (TypeOefening.SCHRIJVEN.equals(typeOefing)) {
                if (gekozenWoord.getVreemd().trim().equals(labelrechts.getText().trim())) {
                    log.debug("CORRECT");
                    soundService.cheer();
                    this.labelrechts.setText(getRechterTekst(gekozenWoord) + " " + Character.toString(10004));
                } else {
                    log.debug("FOUT");
                    this.labelrechts.setText(getRechterTekst(gekozenWoord) + " " + Character.toString(10006));
                }
            } else {
                this.labelrechts.setText(getRechterTekst(gekozenWoord));
            }
        });
        this.quit.setOnAction(actionEvent -> System.exit(0));
        this.open.setOnAction(
                actionEvent -> {
                    imageview.setVisible(false);
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
        this.passief.setOnAction(actionEvent -> {
            typeOefing = TypeOefening.PASSIEF;
            updateKnopTeksten();
            log.debug("passief");
        });
        this.schrijven.setOnAction(actionEvent -> {
            typeOefing = TypeOefening.SCHRIJVEN;
            updateKnopTeksten();
            log.debug("schrijven");
        });
    }

    private void updateKnopTeksten() {
        show.setText(typeOefing.knopLabel);
        labelrechts.setDisable(typeOefing.invullenDisabled);
    }

    private Woord getRandomWoord() {
        var overgeblevenWoorden = woorden
                .stream()
                .filter(woord -> !woord.isSkip())
                .toList();
        if (overgeblevenWoorden.isEmpty()) {
            toonAnimatie();
            return Woord.builder().nederlands("Topper!").vreemd("alles gedaan!").build();
        }
        if (overgeblevenWoorden.size() == 1) {
            return overgeblevenWoorden.get(0);
        }
        int index = random.nextInt(overgeblevenWoorden.size());
        Woord nieuwWoord = overgeblevenWoorden.get(index);
        if (gekozenWoord != null && gekozenWoord.equals(nieuwWoord) || nieuwWoord.isSkip()) {
            return getRandomWoord();
        }
        return overgeblevenWoorden.get(index);
    }

    private void toonAnimatie() {
        Image image = new Image(giphyService.getGiphyUrl());
        imageview.setImage(image);
        imageview.setVisible(true);
    }

    @AllArgsConstructor
    public enum TypeOefening {
        ACTIEF("actief", "show me", true),
        PASSIEF("passief", "show me", true),
        SCHRIJVEN("schrijven", "controleer", false);

        private String naam;
        private String knopLabel;
        private boolean invullenDisabled;
    }

}
