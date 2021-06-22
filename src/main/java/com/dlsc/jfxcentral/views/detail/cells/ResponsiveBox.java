package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.views.MarkdownView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class ResponsiveBox extends HBox {

    private final ImageView imageView = new ImageView();
    private final Label titleLabel = new Label();
    private final MarkdownView markdownView = new MarkdownView();

    public enum ImageLocation {
        LARGE_ON_SIDE,
        SMALL_ON_SIDE,
        BANNER,
        HIDE
    }

    public ResponsiveBox(ImageLocation imageLocation) {
        getStyleClass().add("responsive-box");

        setPrefWidth(0);

        BooleanBinding showImageView = Bindings.createBooleanBinding(() -> imageView.getImage() != null && imageLocation != ImageLocation.HIDE, imageView.imageProperty());

        imageView.setPreserveRatio(true);
        imageView.imageProperty().bind(imageProperty());
        imageView.visibleProperty().bind(showImageView);
        imageView.managedProperty().bind(showImageView);

        titleLabel.textProperty().bind(titleProperty());
        titleLabel.getStyleClass().addAll("header3", "title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        markdownView.mdStringProperty().bind(descriptionProperty());
        markdownView.getStyleClass().add("description-label");

        StackPane imageWrapper = new StackPane(imageView);
        imageWrapper.getStyleClass().add("image-wrapper");
        imageWrapper.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(imageView, Pos.TOP_LEFT);

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

        getChildren().add(vBox);

        widthProperty().addListener(it -> updateLayout(imageLocation, imageWrapper, buttonBox, vBox));
        updateLayout(imageLocation, imageWrapper, buttonBox, vBox);
    }

    private void updateLayout(ImageLocation imageLocation, StackPane imageWrapper, FlowPane buttonBox, VBox vBox) {
        if (imageLocation.equals(ImageLocation.BANNER) || getWidth() < 500) {
            imageWrapper.setPrefWidth(0);
            imageWrapper.setMinWidth(0);
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> imageWrapper.getWidth() - imageWrapper.getInsets().getLeft() - imageWrapper.getInsets().getRight(), imageWrapper.widthProperty(), imageWrapper.insetsProperty()));
            vBox.getChildren().setAll(titleLabel, imageWrapper, markdownView, buttonBox);
            getChildren().remove(imageWrapper);
        } else {
            imageWrapper.setPrefWidth(Region.USE_COMPUTED_SIZE);
            imageWrapper.setMinWidth(Region.USE_COMPUTED_SIZE);
            imageView.fitWidthProperty().unbind();
            imageView.setFitWidth(imageLocation.equals(ImageLocation.LARGE_ON_SIDE) ? 320d : 160d);

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            vBox.getChildren().setAll(titleLabel, markdownView, spacer, buttonBox);
            if (!getChildren().contains(imageWrapper)) {
                getChildren().add(imageWrapper);
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
