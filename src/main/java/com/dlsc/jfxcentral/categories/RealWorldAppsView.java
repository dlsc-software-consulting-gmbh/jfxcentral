package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.MarkdownView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.RealWorldApp;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.RealWorldAppView;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class RealWorldAppsView extends CategoryView {

    private RealWorldAppView appView;
    private ListView<RealWorldApp> listView = new ListView<>();

    public RealWorldAppsView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("real-world-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new RealWorldAppListCell());
        listView.itemsProperty().bind(DataRepository.getInstance().realWorldAppsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setApp(listView.getSelectionModel().getSelectedItem()));
        listView.getItems().addListener((Observable it) -> performDefaultSelection());

        appProperty().addListener(it -> listView.getSelectionModel().select(getApp()));

        setCenter(listView);

        performDefaultSelection();
    }

    private void performDefaultSelection() {
        if (!listView.getItems().isEmpty()) {
            listView.getSelectionModel().select(0);
        } else {
            listView.getSelectionModel().clearSelection();
        }
    }

    @Override
    public Node getDetailPane() {
        if (appView == null) {
            appView = new RealWorldAppView(getRootPane());
            appView.appProperty().bind(appProperty());
        }

        return appView;
    }

    private final ObjectProperty<RealWorldApp> app = new SimpleObjectProperty<>(this, "app");

    public RealWorldApp getApp() {
        return app.get();
    }

    public ObjectProperty<RealWorldApp> appProperty() {
        return app;
    }

    public void setApp(RealWorldApp app) {
        this.app.set(app);
    }

    class RealWorldAppListCell extends AdvancedListCell<RealWorldApp> {

        private final Label nameLabel = new Label();
        private final ImageView imageView = new ImageView();
        private final MarkdownView summaryMarkdownView = new MarkdownView();

        public RealWorldAppListCell() {
            getStyleClass().add("app-list-cell");

            setPrefWidth(0);

            imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
            imageView.managedProperty().bind(imageView.imageProperty().isNotNull());
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);

            nameLabel.getStyleClass().add("name-label");
            nameLabel.setMinWidth(0);

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(summaryMarkdownView, 0, 1);
            gridPane.add(imageView, 0, 2);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(gridPane);
        }

        @Override
        protected void updateItem(RealWorldApp app, boolean empty) {
            super.updateItem(app, empty);

            if (!empty && app != null) {
                nameLabel.setText(app.getName());
                imageView.imageProperty().bind(ImageManager.getInstance().realWorldAppImageProperty(app));
                summaryMarkdownView.setMdString(app.getSummary());
            } else {
                nameLabel.setText("");
                imageView.imageProperty().unbind();
                summaryMarkdownView.setMdString("");
            }
        }
    }
}