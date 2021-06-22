package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.PhotoView;
import com.dlsc.jfxcentral.views.detail.cells.ResponsiveBox.ImageLocation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class ResponsiveBoxWithPhotoView extends VBox {

    private final PhotoView photoView = new PhotoView();
    private final Label titleLabel = new Label();
    private final MarkdownView markdownView = new MarkdownView();
    private final HBox hBox;

    public ResponsiveBoxWithPhotoView(ImageLocation imageLocation) {
        getStyleClass().add("responsive-box");

        setPrefWidth(0);

        photoView.photoProperty().bind(imageProperty());
        photoView.setEditable(false);

        titleLabel.textProperty().bind(titleProperty());
        titleLabel.getStyleClass().addAll("header3", "title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        markdownView.mdStringProperty().bind(descriptionProperty());
        markdownView.getStyleClass().add("description-label");

        FlowPane buttonBox = new FlowPane();
        Bindings.bindContent(buttonBox.getChildren(), buttonsProperty());
        buttonBox.getStyleClass().add("button-box");
        buttonBox.setMinHeight(Region.USE_PREF_SIZE);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        buttonBox.visibleProperty().bind(Bindings.isNotEmpty(buttonBox.getChildren()));
        buttonBox.managedProperty().bind(Bindings.isNotEmpty(buttonBox.getChildren()));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setFillWidth(true);
        vBox.getStyleClass().add("vbox");
        HBox.setHgrow(vBox, Priority.ALWAYS);

        hBox = new HBox(vBox);
        hBox.getStyleClass().add("hbox");

        getChildren().add(hBox);

        widthProperty().addListener(it -> updateLayout(imageLocation, photoView, buttonBox, vBox));
        updateLayout(imageLocation, photoView, buttonBox, vBox);
    }

    private void updateLayout(ImageLocation imageLocation, PhotoView photoView, FlowPane buttonBox, VBox vBox) {
        if (imageLocation.equals(ImageLocation.BANNER) || getWidth() < 500) {
            vBox.getChildren().setAll(photoView, titleLabel, markdownView, buttonBox);
            vBox.setAlignment(Pos.TOP_CENTER);
            buttonBox.setAlignment(Pos.CENTER);
            markdownView.setAlignment(Pos.TOP_CENTER);
            if (!markdownView.getStyleClass().contains("centered")) {
                markdownView.getStyleClass().add("centered");
            }
            hBox.getChildren().remove(photoView);
        } else {
            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            vBox.getChildren().setAll(titleLabel, markdownView, spacer, buttonBox);
            vBox.setAlignment(Pos.TOP_LEFT);

            buttonBox.setAlignment(Pos.CENTER_LEFT);
            markdownView.getStyleClass().remove("centered");

            if (!hBox.getChildren().contains(photoView)) {
                hBox.getChildren().add(photoView);
            }
        }
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public MarkdownView getMarkdownView() {
        return markdownView;
    }

    private final ListProperty<Node> buttons = new SimpleListProperty<>(this, "buttons", FXCollections.observableArrayList());

    public ObservableList<Node> getButtons() {
        return buttons.get();
    }

    public ListProperty<Node> buttonsProperty() {
        return buttons;
    }

    public void setButtons(ObservableList<Node> buttons) {
        this.buttons.set(buttons);
    }

    private final ObjectProperty<Image> image = new SimpleObjectProperty<>(this, "image");

    public Image getImage() {
        return image.get();
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    private final StringProperty title = new SimpleStringProperty(this, "title", "");

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    private final StringProperty description = new SimpleStringProperty(this, "description", "");

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
