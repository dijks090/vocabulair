package nl.sander.vocabulair.controllers;

import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sander.vocabulair.services.SoundService;
import nl.sander.vocabulair.services.WoordenService;
import nl.sander.vocabulair.domain.Woord;
import org.springframework.stereotype.Component;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
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
    @FXML
    public ImageView imageview;

    private List<Woord> woorden;
    private Woord gekozenWoord;
    private File selectedFile;
    private TypeOefening typeOefing = TypeOefening.ACTIEF;
    private boolean alldone = false;

    @FXML
    public void initialize() {
        log.debug("initialize");
        populateKnoppen();
        alert();
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
            toonAnimatie();
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

    private void toonAnimatie() {
        Image image = new Image("https://i.giphy.com/JRQ1PegFVKXBu.gif");
        imageview.setImage(image);
        imageview.setVisible(true);
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        //Setting the title
        alert.setTitle("Alert");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        //Setting the content of the dialog
        alert.setContentText("This is a confirmmation alert");
        //Adding buttons to the dialog pane
        alert.getDialogPane().getButtonTypes().add(type);
        Image image = new Image("https://i.giphy.com/JRQ1PegFVKXBu.gif");
//        alert.getDialogPane().setGraphic(image);
        //Setting the label
        Text txt = new Text("Click the button to show the dialog");
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
        txt.setFont(font);
        alert.showAndWait();
    }

    @AllArgsConstructor
    public enum TypeOefening {
        ACTIEF("actief", "show me"), SCHRIJVEN("schrijven", "controleer");

        private String naam;
        private String knopLabel;
    }

}
