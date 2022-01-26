package com.dlsc.jfxcentral.views.ikonli;

import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import javafx.beans.Observable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.IkonProvider;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.util.EnumSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;

public class IkonliBrowser extends BorderPane {

    private static final int COLUMNS = 5;

    private final TextField selection;
    private final GridPane gridPane;
    private final IkonSearchField searchField;

    public IkonliBrowser() {
        getStyleClass().add("ikonli-browser");

        gridPane = new GridPane();
        gridPane.getStyleClass().add("icon-grid");
        gridPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < COLUMNS; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100 / COLUMNS);
            gridPane.getColumnConstraints().add(col);
        }

        ListView<IkonData> fontsListView = new ListView<>();
        fontsListView.setMinWidth(Region.USE_PREF_SIZE);
        fontsListView.getItems().setAll(resolveIkonData());
        fontsListView.getSelectionModel().selectedItemProperty().addListener(it -> updateFlowGridPane(fontsListView.getSelectionModel().getSelectedItem()));

        Label fontLabel = new Label("Icon Font:");
        fontLabel.getStyleClass().add("box-label");

        Label selectionLabel = new Label("Selection:");
        selection = new TextField();
        selection.setEditable(false);

        Button copy = new Button();
        copy.setGraphic(FontIcon.of(MaterialDesign.MDI_CONTENT_COPY, Color.WHITE));
        copy.disableProperty().bind(selection.textProperty().isEmpty());
        copy.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(selection.getText());
            clipboard.setContent(content);
        });

        searchField = new IkonSearchField();
        searchField.getSuggestions().addListener((Observable it) -> updateFlowGridPane(fontsListView.getSelectionModel().getSelectedItem()));
        searchField.setPromptText("Search by name ...");

        fontsListView.getSelectionModel().selectedItemProperty().addListener(it -> {
            searchField.setText("");
            selection.setText("");
        });

        HBox header = new HBox(selectionLabel, selection, copy, searchField);
        header.setAlignment(Pos.CENTER_RIGHT);
        header.getStyleClass().add("header");

        PrettyScrollPane prettyScrollPane = new PrettyScrollPane(gridPane);
        prettyScrollPane.setShowScrollToTopButton(true);
        prettyScrollPane.setShowShadow(false);

        setTop(header);
        setLeft(fontsListView);
        setCenter(prettyScrollPane);

        fontsListView.getSelectionModel().select(0);

        setPrefHeight(0);
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    private void updateFlowGridPane(IkonData value) {
        IkonProvider ikonProvider = value.getIkonProvider();
        fillIkonGridPane(EnumSet.allOf(ikonProvider.getIkon()));
    }

    private VBox previousSelection;

    private void fillIkonGridPane(EnumSet<? extends Ikon> enumSet) {
        searchField.setIconSet(enumSet);

        gridPane.getChildren().clear();

        int column = 0;
        int row = 0;
        int index = 0;

        for (Ikon value : enumSet) {
            if (!(StringUtils.isBlank(searchField.getText()) || searchField.getSuggestions().contains(value))) {
                continue;
            }
            FontIcon icon = FontIcon.of(value);
            Label nameLabel = new Label(value.getDescription());
            nameLabel.setWrapText(true);
            nameLabel.setMinHeight(Region.USE_PREF_SIZE);
            nameLabel.setTextAlignment(TextAlignment.CENTER);

            VBox wrapper = new VBox(icon, nameLabel);
            wrapper.getStyleClass().add("wrapper");

            wrapper.setOnMouseClicked(me -> {
                if (previousSelection != null) {
                    previousSelection.getStyleClass().remove("active-icon");
                }

                selection.setText(icon.getIconCode().getDescription());
                wrapper.getStyleClass().add("active-icon");
                previousSelection = wrapper;
            });

            gridPane.add(wrapper, column++, row);

            GridPane.setHalignment(wrapper, HPos.CENTER);
            GridPane.setValignment(wrapper, VPos.CENTER);
            GridPane.setHgrow(wrapper, Priority.ALWAYS);
            GridPane.setFillWidth(wrapper, false);

            if (++index % COLUMNS == 0) {
                column = 0;
                row++;
            }
        }
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
