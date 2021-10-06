package nl.sander.vocabulair.controllers;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleUiController {

    private final HostServices hostServices;

    @FXML
    public Label label;

    @FXML
    public Button button;

    @FXML
    public ChoiceBox<String> talen;

    public SimpleUiController(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void initialize() {
        this.button.setOnAction(actionEvent -> this.label.setText(hostServices.getDocumentBase()));
        populateTalen();
    }

    private void populateTalen() {
        this.talen.getItems().add("Frans");
        this.talen.getItems().add("Duits");
        this.talen.getItems().add("Engels");
        this.talen.setValue("Frans");

        this.talen.setOnAction(actionEvent -> {
            Object selectedItem = talen.getSelectionModel().getSelectedItem();

            log.debug("gekozen taal: {}", selectedItem.toString());
        });

    }
}
