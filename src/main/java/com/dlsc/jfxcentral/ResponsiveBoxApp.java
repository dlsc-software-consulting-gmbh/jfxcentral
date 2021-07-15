package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.views.detail.cells.ResponsiveBox;
import com.gluonhq.attach.display.DisplayService;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public class ResponsiveBoxApp extends Application {

    @Override
    public void start(Stage stage) throws IOException, GitAPIException {
        ResponsiveBox box = new ResponsiveBox(ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        box.setTitle("Title");
        box.setSubtitle("Subtitle");
        box.setDescription(getDescription());
        box.setImage(new Image(ResponsiveBoxApp.class.getResource("jpro-logo.png").toExternalForm()));
        box.getExtraControls().addAll(new Button("Button 1"), new Button("Button 2"));

        SectionPane sectionPane = new SectionPane();
        sectionPane.getNodes().add(box);

        VBox vBox = new VBox(sectionPane);
        vBox.setPadding(new Insets(20));
        Scene scene = new Scene(vBox, 500, 500);

        scene.getStylesheets().add(ResponsiveBoxApp.class.getResource("styles.css").toExternalForm());
        scene.getStylesheets().add(ResponsiveBoxApp.class.getResource("markdown.css").toExternalForm());

        stage.setOnCloseRequest(evt -> System.exit(0));
        stage.centerOnScreen();
        stage.setTitle("Responsive Box");
        stage.setScene(scene);
        stage.show();

        if (Boolean.getBoolean("cssfx")) {
            DisplayService.create().ifPresentOrElse(service -> {
                if (service.isDesktop()) {
                    CSSFX.start();
                }
            }, () -> CSSFX.start());
        }
    }

    private String getDescription() {
        return "# Benefits\n" +
                "\n" +
                "**Professional Gantt charts for JavaFX 8 and 11+** - FlexGanttFX is a state-of-the art custom control for JavaFX 8 and utilizes the latest and greatest API that this platform has to offer.\n" +
                "\n" +
                "**Enterprise Resource Planning** - FlexGanttFX can be used to implement an add-on for your ERP system. Use it to visualize the allocation of company resources.\n" +
                "\n" +
                "**Manufacturing** - Use FlexGanttFX to visualize the information in your MES. Show or hide details depending on the level of detail you require.";
    }

    public static void main(String args[]) {
        launch(args);
    }
}
