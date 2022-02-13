package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.NavigationView;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.DataRepository.Source;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.views.ModelObjectSearchResultCell;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.autocomplete.OmniBoxSearchField;
import com.dlsc.jfxcentral.views.autocomplete.OmniBoxService;
import com.dlsc.jfxcentral.views.autocomplete.SearchContext;
import com.dlsc.jfxcentral.views.autocomplete.SearchResult;
import com.jpro.webapi.WebAPI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.scenicview.ScenicView;

import java.util.ArrayList;
import java.util.List;

public class HeaderPane extends HBox {

    public HeaderPane(RootPane rootPane) {
        getStyleClass().add("header-pane");

        setAlignment(Pos.CENTER_LEFT);

        OmniBoxSearchField searchField = new OmniBoxSearchField();
        searchField.setAutoSearch(true);
        searchField.getOmniBoxServices().add(new OmniBoxService<ModelObject>() {
            @Override
            public String getServiceName() {
                return null;
            }

            @Override
            public List<SearchResult<ModelObject>> search(SearchContext ctx, String pattern) {
                List<ModelObject> result = DataRepository.getInstance().search(pattern);
                List<SearchResult<ModelObject>> searchResults = new ArrayList<>();
                for (ModelObject mo : result) {
                    searchResults.add(new SearchResult<>(mo));
                }
                return searchResults;
            }
        });
        searchField.getOmniBox().getListView().setCellFactory(view -> new ModelObjectSearchResultCell(rootPane));

        Label title1 = new Label("JFX-Central");
        title1.setMaxWidth(Double.MAX_VALUE);
        title1.getStyleClass().add("title");
        StackPane.setAlignment(title1, Pos.CENTER_LEFT);

        Label title2 = new Label("JFX-Central");
        title2.setMaxWidth(Double.MAX_VALUE);
        title2.getStyleClass().addAll("title", "title-shadow");
        StackPane.setAlignment(title2, Pos.CENTER_LEFT);

        StackPane stackPane = new StackPane(title2, title1);
        HBox.setHgrow(stackPane, Priority.ALWAYS);

        Label statusLabel = new Label();
        Label percentageLabel = new Label();

        ComboBox<Source> sourceComboBox = new ComboBox<>();
        sourceComboBox.getItems().addAll(Source.values());
        sourceComboBox.valueProperty().bindBidirectional(DataRepository.getInstance().sourceProperty());
        sourceComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Source source) {
                if (source == null) {
                    return "";
                }

                switch (source) {
                    case LIVE:
                        return "Live Data";
                    case STAGING:
                        return "Staging Data";
                }

                return "";
            }

            @Override
            public Source fromString(String string) {
                return null;
            }
        });

        ImageView imageView = new ImageView(JFXCentralApp.class.getResource("duke.png").toExternalForm());
        imageView.setFitHeight(48);
        imageView.setPreserveRatio(true);
        StackPane.setAlignment(imageView, Pos.TOP_LEFT);

        sourceComboBox.setVisible(Boolean.getBoolean("show.source.box"));
        sourceComboBox.setManaged(Boolean.getBoolean("show.source.box"));

        if (!WebAPI.isBrowser()) {
            NavigationView navigationView = new NavigationView();
            navigationView.setVisible(!WebAPI.isBrowser());
            navigationView.setManaged(!WebAPI.isBrowser());
            HBox.setMargin(navigationView, new Insets(0, 0, 0, 20));

            Button scenicView = new Button("Scenic View");
            scenicView.setOnAction(evt -> ScenicView.show(getScene()));
            scenicView.setVisible(Boolean.getBoolean("show.scenicview.button"));
            scenicView.setManaged(Boolean.getBoolean("show.scenicview.button"));

            ToggleButton dark = new ToggleButton("Dark Mode");
            dark.selectedProperty().addListener(it -> updateDark(getScene(), dark.isSelected()));
            dark.setVisible(Boolean.getBoolean("show.dark.button"));
            dark.setManaged(Boolean.getBoolean("show.dark.button"));

            getChildren().addAll(stackPane, sourceComboBox, dark, scenicView, searchField, navigationView);
        } else {
            getChildren().addAll(stackPane, sourceComboBox, searchField);
        }

//        Button count = new Button("count");
//        count.setOnAction(evt -> countTree(getScene()));
//        getChildren().add(count);
    }

    private void updateDark(Scene scene, boolean darkMode) {
        System.out.println("dark: " + darkMode);
        scene.getStylesheets().remove(JFXCentralApp.class.getResource("dark.css").toExternalForm());
        scene.getStylesheets().remove(JFXCentralApp.class.getResource("markdown.css").toExternalForm());
        scene.getStylesheets().remove(JFXCentralApp.class.getResource("markdown-dark.css").toExternalForm());
        if (darkMode) {
            scene.getStylesheets().add(JFXCentralApp.class.getResource("dark.css").toExternalForm());
            scene.getStylesheets().add(JFXCentralApp.class.getResource("markdown-dark.css").toExternalForm());
        } else {
            scene.getStylesheets().add(JFXCentralApp.class.getResource("markdown.css").toExternalForm());
        }
    }

    class Counter {
        int count;
    }

    private void countTree(Scene scene) {
        Counter counter = new Counter();

        countTree(scene.getRoot(), counter);
        System.out.println("counter: " + counter.count);
    }

    private void countTree(Parent root, Counter counter) {
        counter.count += root.getChildrenUnmodifiable().size();
        root.getChildrenUnmodifiable().forEach(child -> {
            if (child instanceof Parent) {
                countTree((Parent) child, counter);
            }
        });
    }
}
