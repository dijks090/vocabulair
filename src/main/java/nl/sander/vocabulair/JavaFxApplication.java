package nl.sander.vocabulair;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import nl.sander.vocabulair.events.StageReadyEvent;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class JavaFxApplication extends Application {


	private ConfigurableApplicationContext context;

	@Override
	public void init() throws Exception {
		ApplicationContextInitializer<GenericApplicationContext> initializer = new ApplicationContextInitializer<GenericApplicationContext>() {
			@Override
			public void initialize(GenericApplicationContext genericApplicationContext) {
				genericApplicationContext.registerBean(Application.class, () -> JavaFxApplication.this);
				genericApplicationContext.registerBean(Parameters.class, () -> getParameters());
				genericApplicationContext.registerBean(HostServices.class, () -> getHostServices());
			}
		};

		this.context = new SpringApplicationBuilder()
				.sources(VocabilairApplication.class)
				.initializers(initializer)
				.build()
				.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.context.publishEvent(new StageReadyEvent(primaryStage));
	}

	@Override
	public void stop() throws Exception {
		this.context.close();
		Platform.exit();
	}
}

