package nl.sander.vocabulair.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
    public Button skipAndNext;
    @FXML
    public MenuItem actief;
    @FXML
    public MenuItem passief;
    @FXML
    public MenuItem schrijven;
    @FXML
    public ImageView imageview;
    @FXML
    private TableView<Woord> woordenTable;
    @FXML
    public  TableColumn<Woord, Boolean> skipColumn;
    @FXML
    private TableColumn<Woord, String> nederlandsColumn;
    @FXML
    public  TableColumn<Woord, String> vreemdColumn;

    private final SoundService soundService;
    private final WoordenService woordenService;
    private final GiphyService giphyService;
    private List<Woord> woorden = List.of();
    private Woord gekozenWoord;
    private File selectedFile;
    private TypeOefening typeOefing = TypeOefening.SCHRIJVEN;
    Random random = new Random();
    private final KeyCombination ctrlN = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
    private final KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    private final KeyCombination ctrlW = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);


    @FXML
    public void initialize() {
        populateKnoppen();
        woordenTable.setEditable(true);
        next.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                    if (ctrlN.match(event)) {
                        next();
                    }
                    if (ctrlS.match(event)) {
                        skipAndNext();
                    }
                    if (ctrlW.match(event)) {
                        show();
                    }
                });
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        show();
                    }
                });
            }
        });
    }

    private String getLinkerTekst(Woord woord) {
        return typeOefing == TypeOefening.PASSIEF ? woord.getVreemd() : woord.getNederlands();
    }

    private String getRechterTekst(Woord woord) {
        return typeOefing == TypeOefening.PASSIEF ? woord.getNederlands() : woord.getVreemd();
    }

    private void setFileLabel() {
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

    private void populateLabel() {
        gekozenWoord = getRandomWoord();
        this.labellinks.setText(getLinkerTekst(gekozenWoord));
        this.labelrechts.setText("");
        setFileLabel();
        labelrechts.requestFocus();
    }

    private void show() {
        if (gekozenWoord == null) {
            return;
        }
        if (TypeOefening.SCHRIJVEN.equals(typeOefing)) {
            if (gekozenWoord.getVreemd().trim().equals(labelrechts.getText().trim())) {
                soundService.cheer();
                this.labelrechts.setText(getRechterTekst(gekozenWoord) + " " + Character.toString(10004));
            } else {
                this.labelrechts.setText(labelrechts.getText().trim() + " --> " + getRechterTekst(gekozenWoord) + " " + Character.toString(10006));
            }
        } else {
            this.labelrechts.setText(getRechterTekst(gekozenWoord));
        }
    }

    private void open() {
        imageview.setVisible(false);
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
        selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            updateKnopTeksten();
            woorden = woordenService.getWoorden(selectedFile);
            populateLabel();
        } else {
            log.error("File is not valid!");
        }
        skipColumn.setCellValueFactory(new PropertyValueFactory<>("skip"));
        skipColumn.setCellFactory(tc -> {
            CheckBoxTableCell<Woord, Boolean> cell = new CheckBoxTableCell<>();
            cell.setEditable(true);
            return cell;
        });
        nederlandsColumn.setCellValueFactory(new PropertyValueFactory<>("nederlands"));
        vreemdColumn.setCellValueFactory(new PropertyValueFactory<>("vreemd"));
        // Set up CheckBox cell for the first column
        ObservableList<Woord> data = FXCollections.observableArrayList(woorden);
        // Add data to the table
        woordenTable.setItems(data);

    }

    private void next() {
        if (gekozenWoord == null) {
            return;
        }
        gekozenWoord.setSkip(false);
        populateLabel();
        labelrechts.requestFocus();
    }

    private void skipAndNext() {
        if (gekozenWoord == null) {
            return;
        }
        gekozenWoord.setSkip(true);
        populateLabel();
        labelrechts.requestFocus();
    }

    private void actief() {
        typeOefing = TypeOefening.ACTIEF;
        updateKnopTeksten();
    }

    private void passief() {
        typeOefing = TypeOefening.PASSIEF;
        updateKnopTeksten();
    }

    private void schrijven() {
        typeOefing = TypeOefening.SCHRIJVEN;
        updateKnopTeksten();
    }

    private void populateKnoppen() {
        this.next.setOnAction(actionEvent -> next());
        this.skipAndNext.setOnAction(actionEvent -> skipAndNext());
        this.show.setOnAction(actionEvent -> show());
        this.quit.setOnAction(actionEvent -> System.exit(0));
        this.open.setOnAction(actionEvent -> open());
        this.actief.setOnAction(actionEvent -> actief());
        this.passief.setOnAction(actionEvent -> passief());
        this.schrijven.setOnAction(actionEvent -> schrijven());
    }

    private void updateKnopTeksten() {
        show.setText(typeOefing.knopLabel);
        labelrechts.setDisable(typeOefing.invullenDisabled);
        setFileLabel();
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
            return overgeblevenWoorden.getFirst();
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

    public void showHelpDialog(ActionEvent actionEvent) {
        Alert helpDialog = new Alert(Alert.AlertType.INFORMATION);
        helpDialog.initModality(Modality.APPLICATION_MODAL);
        helpDialog.setTitle("Help");
        helpDialog.setHeaderText("Shortcut Information");
        helpDialog.setContentText(
                """
                        Gebruik deze shortcuts om snel te navigeren
                        Linker Ctrl+n = Next
                        Linker Ctrl+s = Skip and next
                        Linker Ctrl+w = shoW me
                        
                        """
        );

        helpDialog.showAndWait();
    }

    @AllArgsConstructor
    public enum TypeOefening {
        ACTIEF("actief", "_shoW me", true),
        PASSIEF("passief", "_shoW me", true),
        SCHRIJVEN("schrijven", "controleer", false);

        private final String naam;
        private final String knopLabel;
        private final boolean invullenDisabled;
    }

}
