package com.dlsc.jfxcentral.views.ikonli;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.IkonProvider;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.util.EnumSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;

public class IkonliBrowser extends VBox {

    private final FlowGridPane flowGridPane = new FlowGridPane(8, 10);

    public IkonliBrowser() {
        ComboBox<IkonData> fontsComboBox = new ComboBox<>();
        fontsComboBox.getItems().setAll(resolveIkonData());
        fontsComboBox.valueProperty().addListener(it -> udpateFlowGridPane(fontsComboBox.getValue()));
        fontsComboBox.getSelectionModel().select(0);

        Label label = new Label("Icon Font:");
        label.getStyleClass().add("box-label");

        HBox header = new HBox(label, fontsComboBox);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header");

        getChildren().addAll(header, flowGridPane);
    }

    private void udpateFlowGridPane(IkonData value) {
        System.out.println("name: " + value);
        System.out.println("provider: " + value.getIkonProvider());

        IkonProvider ikonProvider = value.getIkonProvider();
        flowGridPane.getChildren().add(createIkonGrid(EnumSet.allOf(ikonProvider.getIkon())));
    }

    private static Node createIkonGrid(EnumSet<? extends Ikon> enumSet) {
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Selection:");
        TextField selection = new TextField();
        selection.setEditable(false);
        Button copy = new Button();
        copy.setGraphic(FontIcon.of(MaterialDesign.MDI_CONTENT_COPY, Color.WHITE));
        copy.getStyleClass().addAll("btn", "btm-sm", "btn-primary");
        HBox.setMargin(label, new Insets(10, 5, 10, 10));
        HBox.setMargin(selection, new Insets(10, 5, 10, 5));
        HBox.setMargin(copy, new Insets(10, 10, 10, 5));
        HBox.setHgrow(selection, Priority.ALWAYS);
        HBox hbox = new HBox(label, selection, copy);
        hbox.setAlignment(Pos.BASELINE_CENTER);
        borderPane.setTop(hbox);

        copy.disableProperty().bind(selection.textProperty().isEmpty());
        copy.setOnAction(e -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(selection.getText());
            clipboard.setContent(content);
        });

        GridPane pane = new GridPane();
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setAlignment(Pos.CENTER);
        pane.setCenterShape(true);
        pane.setPadding(new Insets(5));
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        borderPane.setCenter(new ScrollPane(pane));

        int column = 0;
        int row = 0;
        int index = 0;

        FontIcon[] previousIcon = new FontIcon[1];

        for (Ikon value : enumSet) {
            FontIcon icon = FontIcon.of(value);
            icon.getStyleClass().setAll("font-icon");
            icon.setOnMouseClicked(me -> {
                if (previousIcon[0] != null) {
                    previousIcon[0].getStyleClass().remove("active-icon");
                }
                FontIcon nextIcon = (FontIcon) me.getSource();
                selection.setText(nextIcon.getIconCode().getDescription());
                nextIcon.getStyleClass().add("active-icon");
                previousIcon[0] = nextIcon;
            });
            pane.add(icon, column++, row);
            GridPane.setMargin(icon, new Insets(10, 10, 10, 10));
            if (++index % 10 == 0) {
                column = 0;
                row++;
            }
        }

        return borderPane;
    }

    private Set<IkonData> resolveIkonData() {
        Set<IkonData> ikons = new TreeSet<>();
        if (null != IkonProvider.class.getModule().getLayer()) {
            for (IkonProvider provider : ServiceLoader.load(IkonProvider.class.getModule().getLayer(), IkonProvider.class)) {
                ikons.add(IkonData.of(provider));
            }
        } else {
            for (IkonProvider provider : ServiceLoader.load(IkonProvider.class)) {
                ikons.add(IkonData.of(provider));
            }
        }

        return ikons;
    }

    private static class IkonData implements Comparable<IkonData> {
        private String name;
        private IkonProvider ikonProvider;

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int compareTo(IkonData o) {
            return name.compareTo(o.name);
        }

        public IkonProvider getIkonProvider() {
            return ikonProvider;
        }

        static IkonData of(IkonProvider ikonProvider) {
            IkonData ikonData = new IkonData();
            ikonData.name = ikonProvider.getIkon().getSimpleName();
            ikonData.ikonProvider = ikonProvider;
            return ikonData;
        }
    }
}
