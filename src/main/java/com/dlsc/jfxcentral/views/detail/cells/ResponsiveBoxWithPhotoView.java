package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.PhotoView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class ResponsiveBoxWithPhotoView extends Pane {

    private final PhotoView photoView = new PhotoView();
    private final Label titleLabel = new Label();
    private final Label subtitleLabel = new Label();
    private final MarkdownView markdownView = new MarkdownView();
    private final StackPane imageWrapper;
    private final ImageLocation imageLocation;
    private final FlowPane extraControlsPane;

    public enum ImageLocation {
        LARGE_ON_SIDE,
        SMALL_ON_SIDE,
        BANNER,
        HIDE
    }

    public ResponsiveBoxWithPhotoView(ImageLocation imageLocation) {
        this.imageLocation = imageLocation;

        photoView.setEditable(false);

        getStyleClass().add("responsive-box");

        setPrefWidth(0);

        BooleanBinding showImageView = Bindings.createBooleanBinding(() -> photoView.getPhoto() != null && imageLocation != ImageLocation.HIDE, photoView.photoProperty());

        photoView.photoProperty().bind(imageProperty());

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
        markdownView.setMinHeight(Region.USE_PREF_SIZE);
        markdownView.getStyleClass().add("description-label");

        imageWrapper = new StackPane(photoView);
        imageWrapper.getStyleClass().add("image-wrapper");
        imageWrapper.setMaxHeight(Region.USE_PREF_SIZE);
        imageWrapper.visibleProperty().bind(showImageView);
        imageWrapper.managedProperty().bind(showImageView);
        StackPane.setAlignment(photoView, Pos.TOP_RIGHT);

        extraControlsPane = new FlowPane();
        Bindings.bindContent(extraControlsPane.getChildren(), extraControlsProperty());
        extraControlsPane.getStyleClass().add("button-box");
        extraControlsPane.setMinHeight(Region.USE_PREF_SIZE);
        extraControlsPane.setAlignment(Pos.BOTTOM_LEFT);
        extraControlsPane.visibleProperty().bind(Bindings.isNotEmpty(extraControlsPane.getChildren()));
        extraControlsPane.managedProperty().bind(Bindings.isNotEmpty(extraControlsPane.getChildren()));

        getChildren().setAll(imageWrapper, titleLabel, subtitleLabel, markdownView, extraControlsPane);

        footerProperty().addListener((obs, oldFooter, newFooter) -> {
            if (oldFooter != null) {
                getChildren().remove(oldFooter);
            }
            if (newFooter != null) {
                getChildren().add(newFooter);
            }
        });
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    protected void layoutChildren() {
        Insets insets = getInsets();

        double x = insets.getLeft();
        double y = insets.getTop();
        double w = getWidth() - insets.getLeft() - insets.getRight();

        double ph;

        if (imageLocation.equals(ImageLocation.BANNER) || getWidth() < 500) {
            ph = imageWrapper.prefHeight(w);
            imageWrapper.resizeRelocate(x, y, w, ph);

            if (titleLabel.isManaged() || subtitleLabel.isManaged() ||markdownView.isManaged() || extraControlsPane.isManaged() || (getFooter() != null && getFooter().isManaged())) {
                y += ph + getVgap();
            }

        } else {
            ph = imageWrapper.prefHeight(w);
            double pw = imageWrapper.prefWidth(w);

            imageWrapper.resizeRelocate(x + w - pw, y, pw, ph);

            w -= pw; // less available horizontal space now
            w -= getHgap();
        }

        if (titleLabel.isManaged()) {
            ph = titleLabel.prefHeight(w);
            titleLabel.resizeRelocate(x, y, w, ph);

            if (subtitleLabel.isManaged() || markdownView.isManaged() || extraControlsPane.isManaged() || (getFooter() != null && getFooter().isManaged())) {
                y += ph + getVgap();
            }
        }

        if (subtitleLabel.isManaged()) {
            ph = subtitleLabel.prefHeight(w);
            subtitleLabel.resizeRelocate(x, y, w, ph);
            if (markdownView.isManaged() || extraControlsPane.isManaged() || (getFooter() != null && getFooter().isManaged())) {
                y += ph + getVgap();
            }
        }

        if (markdownView.isManaged()) {
            ph = markdownView.prefHeight(w);
            markdownView.resizeRelocate(x, y, w, ph);
            if (extraControlsPane.isManaged() || (getFooter() != null && getFooter().isManaged())) {
                y += ph + getVgap();
            }
        }

        if (extraControlsPane.isManaged()) {
            ph = extraControlsPane.prefHeight(w);
            extraControlsPane.resizeRelocate(x, y, w, ph);
            if (getFooter() != null && getFooter().isManaged()) {
                y += ph + getVgap();
            }
        }

        Node footer = getFooter();
        if (footer != null && footer.isManaged()) {
            ph = footer.prefHeight(w);
            footer.resizeRelocate(x, y, w, ph);
        }
    }

    private final DoubleProperty hgap = new SimpleDoubleProperty(this, "hgap", 10);

    public double getHgap() {
        return hgap.get();
    }

    public DoubleProperty hgapProperty() {
        return hgap;
    }

    public void setHgap(double hgap) {
        this.hgap.set(hgap);
    }

    private final DoubleProperty vgap = new SimpleDoubleProperty(this, "vgap", 10);

    public double getVgap() {
        return vgap.get();
    }

    public DoubleProperty vgapProperty() {
        return vgap;
    }

    public void setVgap(double vgap) {
        this.vgap.set(vgap);
    }

    @Override
    protected double computePrefHeight(double w) {
        double h =  getInsets().getTop() + getInsets().getBottom();

        if (imageWrapper.isManaged()) {
            if ((imageLocation.equals(ImageLocation.BANNER) || w < 500)) {
                h += imageWrapper.prefHeight(w);
                if (titleLabel.isManaged() || subtitleLabel.isManaged() || markdownView.isManaged() || extraControlsPane.isManaged() || (getFooter() != null && getFooter().isManaged())) {
                    h += getVgap();
                }
            } else if (imageLocation.equals(ImageLocation.LARGE_ON_SIDE)) {
                w -= getLargeImageWidth();
                w -= getHgap();
            } else if (imageLocation.equals(ImageLocation.SMALL_ON_SIDE)) {
                w -= getSmallImageWidth();
                w -= getHgap();
            }
        }

        if (titleLabel.isManaged()) {
            h += titleLabel.prefHeight(w);
            if (subtitleLabel.isManaged() || markdownView.isManaged() || extraControlsPane.isManaged() || (getFooter() != null && getFooter().isManaged())) {
                h += getVgap();
            }
        }

        if (subtitleLabel.isManaged()) {
            h += subtitleLabel.prefHeight(w);
            if (markdownView.isManaged() || extraControlsPane.isManaged() || (getFooter() != null && getFooter().isManaged())) {
                h += getVgap();
            }
        }

        if (markdownView.isManaged()) {
            h += markdownView.prefHeight(w);
            if (extraControlsPane.isManaged() || (getFooter() != null && getFooter().isManaged())) {
                h += getVgap();
            }
        }

        if (extraControlsPane.isManaged()) {
            h += extraControlsPane.prefHeight(w);
            if (getFooter() != null && getFooter().isManaged()) {
                h += getVgap();
            }
        }

        Node footer = getFooter();
        if (footer != null && footer.isManaged()) {
            h += footer.prefHeight(w);
        }

        return h;
    }

    @Override
    protected double computeMaxHeight(double width) {
        return computePrefHeight(width);
    }

    @Override
    protected double computeMinHeight(double width) {
        return super.computePrefHeight(width);
    }

    public PhotoView getPhotoView() {
        return photoView;
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

    private final ListProperty<Node> extraControls = new SimpleListProperty<>(this, "buttons", FXCollections.observableArrayList());

    public ObservableList<Node> getExtraControls() {
        return extraControls.get();
    }

    public ListProperty<Node> extraControlsProperty() {
        return extraControls;
    }

    public void setExtraControls(ObservableList<Node> controls) {
        this.extraControls.set(controls);
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
