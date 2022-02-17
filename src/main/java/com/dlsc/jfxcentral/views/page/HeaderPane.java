package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.NavigationView;
import com.dlsc.jfxcentral.data.DataRepository;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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

        title1.visibleProperty().bind(rootPane.widthProperty().greaterThan(1000));
        title1.managedProperty().bind(rootPane.widthProperty().greaterThan(1000));
        title2.visibleProperty().bind(rootPane.widthProperty().greaterThan(1000));
        title2.managedProperty().bind(rootPane.widthProperty().greaterThan(1000));

        StackPane stackPane = new StackPane(title2, title1);
        HBox.setHgrow(stackPane, Priority.ALWAYS);

        ImageView imageView = new ImageView(JFXCentralApp.class.getResource("duke.png").toExternalForm());
        imageView.setFitHeight(48);
        imageView.setPreserveRatio(true);
        StackPane.setAlignment(imageView, Pos.TOP_LEFT);

        ToggleButton dark = new ToggleButton("Dark Mode");
        dark.selectedProperty().addListener(it -> updateDark(getScene(), dark.isSelected()));
        dark.setVisible(Boolean.getBoolean("show.dark.button"));
        dark.setManaged(Boolean.getBoolean("show.dark.button"));

        if (!WebAPI.isBrowser()) {
            NavigationView navigationView = new NavigationView();
            navigationView.setVisible(!WebAPI.isBrowser());
            navigationView.setManaged(!WebAPI.isBrowser());
            HBox.setMargin(navigationView, new Insets(0, 0, 0, 20));

            Button scenicView = new Button("Scenic View");
            scenicView.setOnAction(evt -> ScenicView.show(getScene()));
            scenicView.setVisible(Boolean.getBoolean("show.scenicview.button"));
            scenicView.setManaged(Boolean.getBoolean("show.scenicview.button"));

            getChildren().addAll(stackPane, dark, scenicView, searchField, navigationView);
        } else {
            getChildren().addAll(stackPane, dark, searchField);
        }
    }

    private void updateDark(Scene scene, boolean darkMode) {
        scene.setFill(Color.web("#4483A0"));
        scene.getStylesheets().remove(JFXCentralApp.class.getResource("dark.css").toExternalForm());
        scene.getStylesheets().remove(JFXCentralApp.class.getResource("markdown-dark.css").toExternalForm());
        if (darkMode) {
            scene.setFill(Color.rgb(60, 63, 65));
            scene.getStylesheets().add(JFXCentralApp.class.getResource("dark.css").toExternalForm());
            scene.getStylesheets().add(JFXCentralApp.class.getResource("markdown-dark.css").toExternalForm());
        }
    }
}
