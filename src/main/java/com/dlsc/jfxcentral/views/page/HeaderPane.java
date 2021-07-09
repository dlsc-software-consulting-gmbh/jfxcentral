package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.DataRepository.Source;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.views.ModelObjectSearchResultCell;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.autocomplete.OmniBoxSearchField;
import com.dlsc.jfxcentral.views.autocomplete.OmniBoxService;
import com.dlsc.jfxcentral.views.autocomplete.SearchContext;
import com.dlsc.jfxcentral.views.autocomplete.SearchResult;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.io.IOException;
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
        searchField.getOmniBox().getListView().setCellFactory(view -> new ModelObjectSearchResultCell(searchField, rootPane));

        Label title1 = new Label("JFX-Central");
        title1.setMaxWidth(Double.MAX_VALUE);
        title1.getStyleClass().add("title");
        StackPane.setAlignment(title1, Pos.CENTER_LEFT);

        Label title2 = new Label("JFX-Central");
        title2.setMaxWidth(Double.MAX_VALUE);
        title2.getStyleClass().addAll("title", "title-shadow");
        StackPane.setAlignment(title2, Pos.CENTER_LEFT);

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(evt -> {
            try {
                JFXCentralApp.updateRepository(new TextProgressMonitor());
                DataRepository.getInstance().refreshData();
            } catch (GitAPIException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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

        StackPane stackPane = new StackPane(title2, title1);
        HBox.setHgrow(stackPane, Priority.ALWAYS);

        refreshButton.setVisible(Boolean.getBoolean("show.refresh.button"));
        refreshButton.setManaged(Boolean.getBoolean("show.refresh.button"));

        sourceComboBox.setVisible(Boolean.getBoolean("show.source.box"));
        sourceComboBox.setManaged(Boolean.getBoolean("show.source.box"));

//        getChildren().addAll(stackPane, refreshButton, sourceComboBox, new FontSizeSelector(), searchField);
        getChildren().addAll(stackPane, refreshButton, sourceComboBox, searchField);
    }
}
