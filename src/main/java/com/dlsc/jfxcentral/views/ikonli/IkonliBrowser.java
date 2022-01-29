package com.dlsc.jfxcentral.views.ikonli;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.IkonProvider;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.util.*;
import java.util.function.Predicate;

public class IkonliBrowser extends BorderPane {

    private final TextField selection = new TextField();
    private final GridView<Ikon> gridView;
    private final IkonSearchField searchField;
    private final Label statusLabel;

    public IkonliBrowser() {
        getStyleClass().add("ikonli-browser");

        ListView<IkonData> fontsListView = new ListView<>();
        fontsListView.setMinWidth(Region.USE_PREF_SIZE);
        fontsListView.getItems().setAll(resolveIkonData());
        fontsListView.getSelectionModel().getSelectedItems().addListener((Observable it) -> fillGridView(fontsListView.getSelectionModel().getSelectedItems()));
        fontsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        gridView = new GridView<>();
        gridView.setHorizontalCellSpacing(20);
        gridView.setVerticalCellSpacing(20);
        gridView.getStyleClass().add("icon-grid");
        gridView.setCellFactory(view -> new GridCell<>() {

            FontIcon icon = new FontIcon();
            Label nameLabel = new Label();

            {
                nameLabel.setWrapText(true);
                nameLabel.setMinHeight(Region.USE_PREF_SIZE);
                nameLabel.setTextAlignment(TextAlignment.CENTER);

                VBox wrapper = new VBox(icon, nameLabel);
                wrapper.getStyleClass().add("wrapper");
                wrapper.setAlignment(Pos.TOP_CENTER);

                wrapper.setOnMouseClicked(me -> {
                    if (previousSelection != null) {
                        previousSelection.getStyleClass().remove("active-icon");
                    }

                    selection.setText(icon.getIconCode().getDescription());
                    wrapper.getStyleClass().add("active-icon");
                    previousSelection = wrapper;
                });

                setGraphic(wrapper);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setMinWidth(80);
                wrapper.visibleProperty().bind(emptyProperty().not());
            }

            @Override
            protected void updateItem(Ikon item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    icon.setIconCode(item);
                    if (fontsListView.getSelectionModel().getSelectedItems().size() > 1) {
                        nameLabel.setText(item.getDescription() + "\n(" + DATA_MAP.get(item).name + ")");
                    } else {
                        nameLabel.setText(item.getDescription());
                    }
                }
            }
        });


        Label fontLabel = new Label("Icon Font:");
        fontLabel.getStyleClass().add("box-label");

        Label selectionLabel = new Label("Selection:");
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
        searchField.getSuggestions().addListener((Observable it) -> fillGridView(fontsListView.getSelectionModel().getSelectedItems()));
        searchField.setPromptText("Search by name ...");

        fontsListView.getSelectionModel().selectedItemProperty().addListener(it -> {
            searchField.setText("");
            selection.setText("");
        });

        HBox header = new HBox(selectionLabel, selection, copy, searchField);
        header.setAlignment(Pos.CENTER_RIGHT);
        header.getStyleClass().add("header");

        statusLabel = new Label();
        statusLabel.getStyleClass().add("status-label");

        setTop(header);
        setLeft(fontsListView);
        setCenter(gridView);
        setBottom(statusLabel);

        fontsListView.getSelectionModel().select(0);

        setPrefHeight(0);
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    private VBox previousSelection;

    private void fillGridView(ObservableList<IkonData> selection) {
        Platform.runLater(() -> {
            ObservableList<? extends Ikon> icons = FXCollections.observableArrayList();
            selection.forEach(data -> {
                IkonProvider ikonProvider = data.getIkonProvider();
                EnumSet enumSet = EnumSet.allOf(ikonProvider.getIkon());
                icons.addAll(enumSet);
            });

            searchField.setIcons(icons);
            statusLabel.setText("Number of icons: " + icons.size());

            FilteredList<? extends Ikon> filteredList = new FilteredList<>(icons);
            filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> (Predicate<Ikon>) ikon -> {
                if (StringUtils.isBlank(searchField.getText())) {
                    return true;
                }
                return searchField.getSuggestions().contains(ikon);
            }, searchField.getSuggestions(), searchField.textProperty()));
            gridView.getItems().setAll(filteredList);
        });
    }

    // yes, this can be static and shared across JPro sessions
    private static Map<Ikon, IkonData> DATA_MAP = new HashMap<>();

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

        ikons.forEach(data -> {
            IkonProvider ikonProvider = data.getIkonProvider();
            EnumSet enumSet = EnumSet.allOf(ikonProvider.getIkon());
            enumSet.forEach(icon -> DATA_MAP.put((Ikon) icon, data));
        });

        System.out.println("size: " + DATA_MAP.size());

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
