package nl.sander.vocabulair.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.sander.vocabulair.domain.WoordenService;
import nl.sander.vocabulair.domain.dto.Woord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleUiController {

    private final WoordenService woordenService;

    @FXML
    public Label labellinks;

    @FXML
    public Label labelrechts;

    @FXML
    public Button next;

    @FXML
    public Button show;

    @FXML
    public ChoiceBox<String> talen;

    private List<Woord> woorden;
    private Woord gekozenWoord;

    @FXML
    public void initialize() {
        populateTalen();
        populateKnoppen();
        woorden = woordenService.getWoordenVoorTaal("frans");
    }

    private void populateKnoppen() {
        this.next.setOnAction(
                actionEvent -> {
                    gekozenWoord = getRandomWoord();
                    this.labellinks.setText(gekozenWoord.getNederlands());
                    this.labelrechts.setText("");
                }
        );
        this.show.setOnAction(
                actionEvent -> {
                    this.labelrechts.setText(gekozenWoord.getVreemd());
                }
        );
    }

    private Woord getRandomWoord() {
        Random random = new Random();
        int index = random.nextInt(woorden.size());
        return woorden.get(index);
    }

    private void populateTalen() {
        this.talen.getItems().add("Frans");
        this.talen.getItems().add("Duits");
        this.talen.getItems().add("Engels");
        this.talen.setValue("Frans");

        this.talen.setOnAction(actionEvent -> {
            Object selectedItem = talen.getSelectionModel().getSelectedItem();
            log.debug("gekozen taal: {}", selectedItem.toString());
            woorden = woordenService.getWoordenVoorTaal(selectedItem.toString());
        });

    }
}
