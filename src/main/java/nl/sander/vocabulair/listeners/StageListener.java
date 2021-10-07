package nl.sander.vocabulair.listeners;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.sander.vocabulair.events.StageReadyEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {

    private final String title;
    private final Resource fxmlResource;
    private final ApplicationContext applicationContext;

    public StageListener(
            @Value("${spring.application.ui.title}") String title,
            @Value("classpath:/ui.fxml") Resource fxmlResource,
            ApplicationContext applicationContext) {
        this.title = title;
        this.fxmlResource = fxmlResource;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        try {
            URL url = fxmlResource.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 400, 100);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
