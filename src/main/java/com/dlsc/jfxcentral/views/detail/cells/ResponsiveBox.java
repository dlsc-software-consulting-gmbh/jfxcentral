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

public class ResponsiveBox extends VBox {

    private final ImageView imageView = new ImageView();
    private final Label titleLabel = new Label();
    private final Label subtitleLabel = new Label();
    private final MarkdownView markdownView = new MarkdownView();
    private final HBox hBox;
    private final StackPane imageWrapper;

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
        titleLabel.getStyleClass().addAll("header2", "title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        subtitleLabel.textProperty().bind(subtitleProperty());
        subtitleLabel.getStyleClass().addAll("subtitle-label");
        subtitleLabel.setWrapText(true);
        subtitleLabel.setMinHeight(Region.USE_PREF_SIZE);
        subtitleLabel.visibleProperty().bind(subtitleLabel.textProperty().isNotEmpty());
        subtitleLabel.managedProperty().bind(subtitleLabel.textProperty().isNotEmpty());

        markdownView.mdStringProperty().bind(descriptionProperty());
        markdownView.getStyleClass().add("description-label");

        imageWrapper = new StackPane(imageView);
        imageWrapper.getStyleClass().add("image-wrapper");
        imageWrapper.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(imageView, Pos.TOP_RIGHT);

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

        widthProperty().addListener(it -> updateLayout(imageLocation, imageWrapper, buttonBox, vBox));
        updateLayout(imageLocation, imageWrapper, buttonBox, vBox);
    }

    private void updateLayout(ImageLocation imageLocation, StackPane imageWrapper, FlowPane buttonBox, VBox vBox) {
        if (imageLocation.equals(ImageLocation.BANNER) || getWidth() < 500) {
            imageWrapper.setPrefWidth(0);
            imageWrapper.setMinWidth(0);
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> imageWrapper.getWidth() - imageWrapper.getInsets().getLeft() - imageWrapper.getInsets().getRight(), imageWrapper.widthProperty(), imageWrapper.insetsProperty()));
            vBox.getChildren().setAll(titleLabel, subtitleLabel, imageWrapper, markdownView, buttonBox);
            hBox.getChildren().remove(imageWrapper);
        } else {
            imageWrapper.setPrefWidth(Region.USE_COMPUTED_SIZE);
            imageWrapper.setMinWidth(Region.USE_COMPUTED_SIZE);

            imageView.fitWidthProperty().unbind();
            imageView.setFitWidth(imageLocation.equals(ImageLocation.LARGE_ON_SIDE) ? getLargeImageWidth() : getSmallImageWidth());

            imageView.fitHeightProperty().unbind();
            imageView.setFitHeight(imageLocation.equals(ImageLocation.LARGE_ON_SIDE) ? getLargeImageHeight() : getLargeImageHeight());

            vBox.getChildren().setAll(titleLabel, subtitleLabel, markdownView, buttonBox);
            if (!hBox.getChildren().contains(imageWrapper)) {
                hBox.getChildren().add(imageWrapper);
            }
        }

        Node footer = getFooter();
        if (footer != null && !getChildren().contains(footer)) {
            getChildren().add(footer);
        }
    }

    public StackPane getImageWrapper() {
        return imageWrapper;
    }

    private final DoubleProperty smallImageWidth = new SimpleDoubleProperty(this, "smallImageWidth", 160);

    public double getSmallImageWidth() {
        return smallImageWidth.get();
    }

    public DoubleProperty smallImageWidthProperty() {
        return smallImageWidth;
    }

    public void setSmallImageWidth(double smallImageWidth) {
        this.smallImageWidth.set(smallImageWidth);
    }

    private final DoubleProperty largeImageHeight = new SimpleDoubleProperty(this, "largeImageHeight", -1);

    public double getLargeImageHeight() {
        return largeImageHeight.get();
    }

    public DoubleProperty largeImageHeightProperty() {
        return largeImageHeight;
    }

    public void setLargeImageHeight(double largeImageHeight) {
        this.largeImageHeight.set(largeImageHeight);
    }

    private final DoubleProperty largeImageWidth = new SimpleDoubleProperty(this, "largeImageWidth", 320);

    public double getLargeImageWidth() {
        return largeImageWidth.get();
    }

    public DoubleProperty largeImageWidthProperty() {
        return largeImageWidth;
    }

    public void setLargeImageWidth(double largeImageWidth) {
        this.largeImageWidth.set(largeImageWidth);
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public MarkdownView getMarkdownView() {
        return markdownView;
    }

    private final ObjectProperty<Node> footer = new SimpleObjectProperty<>(this, "footer");

    public Node getFooter() {
        return footer.get();
    }

    public ObjectProperty<Node> footerProperty() {
        return footer;
    }

    public void setFooter(Node footer) {
        this.footer.set(footer);
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

    private final StringProperty subtitle = new SimpleStringProperty(this, "subtitle");

    public String getSubtitle() {
        return subtitle.get();
    }

    public StringProperty subtitleProperty() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle.set(subtitle);
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
