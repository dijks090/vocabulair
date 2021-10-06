package nl.sander.vocabulair.controllers;

import javafx.application.HostServices;
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

    private final HostServices hostServices;
    private final WoordenService woordenService;

    @FXML
    public Label label;

    @FXML
    public Button button;

    @FXML
    public ChoiceBox<String> talen;

    private List<Woord> woorden;

    @FXML
    public void initialize() {
        populateTalen();
        populateKnop();
        woorden = woordenService.getWoordenVoorTaal("frans");

        log.debug("woorden: {}", woorden);
    }

    private void populateKnop() {
        this.button.setOnAction(actionEvent -> this.label.setText(getRandomWoord()));
    }

    private String getRandomWoord() {
        Random random = new Random();
        int index = random.nextInt(woorden.size());
        return woorden.get(index).getVreemd();
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
