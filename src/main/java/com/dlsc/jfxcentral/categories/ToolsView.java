package com.dlsc.jfxcentral.categories;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.MarkdownView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Tool;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.ToolView;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ToolsView extends CategoryView {

    private ToolView toolView;
    private ListView<Tool> listView = new ListView<>();

    public ToolsView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("tools-view");

        listView.setMinWidth(Region.USE_PREF_SIZE);
        listView.setCellFactory(view -> new ToolListCell());
        listView.itemsProperty().bind(DataRepository.getInstance().toolsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setTool(listView.getSelectionModel().getSelectedItem()));
        listView.getItems().addListener((Observable it) -> performDefaultSelection());

        toolProperty().addListener(it -> listView.getSelectionModel().select(getTool()));

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
        if (toolView == null) {
            toolView = new ToolView(getRootPane());
            toolView.toolProperty().bind(toolProperty());
        }

        return toolView;
    }

    private final ObjectProperty<Tool> tool = new SimpleObjectProperty<>(this, "tool");

    public Tool getTool() {
        return tool.get();
    }

    public ObjectProperty<Tool> toolProperty() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool.set(tool);
    }

    class ToolListCell extends AdvancedListCell<Tool> {

        private final Label nameLabel = new Label();
        private final ImageView imageView = new ImageView();
        private final MarkdownView markdownView = new MarkdownView();

        public ToolListCell() {
            getStyleClass().add("tool-list-cell");

            setPrefWidth(0);

            imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
            imageView.managedProperty().bind(imageView.imageProperty().isNotNull());
            imageView.setFitHeight(40);
            imageView.setFitWidth(60);
            imageView.setPreserveRatio(true);

            nameLabel.getStyleClass().add("name-label");
            nameLabel.setMinWidth(0);

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(markdownView, 0, 1);
            gridPane.add(imageView, 1, 0);

            GridPane.setRowSpan(imageView, 2);

            GridPane.setHgrow(nameLabel, Priority.ALWAYS);
            GridPane.setHgrow(markdownView, Priority.ALWAYS);

            GridPane.setValignment(nameLabel, VPos.TOP);
            GridPane.setValignment(markdownView, VPos.TOP);
            GridPane.setValignment(imageView, VPos.TOP);

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(gridPane);
        }

        @Override
        protected void updateItem(Tool tool, boolean empty) {
            super.updateItem(tool, empty);

            if (!empty && tool != null) {
                nameLabel.setText(tool.getName());
                imageView.imageProperty().bind(ImageManager.getInstance().toolImageProperty(tool));
                markdownView.setMdString(tool.getSummary());
            } else {
                nameLabel.setText("");
                imageView.imageProperty().unbind();
                markdownView.setMdString("");
            }
        }
    }
}