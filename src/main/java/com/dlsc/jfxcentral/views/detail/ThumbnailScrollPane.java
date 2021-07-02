package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Image;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.data.model.LibraryInfo;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.List;

public class ThumbnailScrollPane extends ScrollPane {

    private final HBox thumbnailBox;
    private final RootPane rootPane;

    public ThumbnailScrollPane(RootPane rootPane) {
        this.rootPane = rootPane;

        getStyleClass().add("thumbnail-scroll-pane");

        thumbnailBox = new HBox();
        thumbnailBox.getStyleClass().add("thumbnail-box");

        setPannable(true);
        setContent(thumbnailBox);
        setFitToWidth(true);
        setFitToHeight(true);
        setVbarPolicy(ScrollBarPolicy.NEVER);

        visibleProperty().bind(Bindings.isNotEmpty(thumbnailBox.getChildren()));
        managedProperty().bind(Bindings.isNotEmpty(thumbnailBox.getChildren()));

        libraryInfo.addListener(it -> updateView());

        setPrefWidth(0);
        setMinWidth(0);
    }

    private final ObjectProperty<Library> library = new SimpleObjectProperty<>(this, "library");

    public Library getLibrary() {
        return library.get();
    }

    public ObjectProperty<Library> libraryProperty() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library.set(library);
    }

    private final ObjectProperty<LibraryInfo> libraryInfo = new SimpleObjectProperty<>(this, "libraryInfo");

    public LibraryInfo getLibraryInfo() {
        return libraryInfo.get();
    }

    public ObjectProperty<LibraryInfo> libraryInfoProperty() {
        return libraryInfo;
    }

    private void updateView() {
        thumbnailBox.getChildren().clear();

        LibraryInfo info = getLibraryInfo();
        Library library = getLibrary();

        if (info != null && library != null) {
            List<Image> images = info.getImages();
            for (int i = 0; i < images.size(); i++) {

                Image image = images.get(i);
                ImageView imageView = new ImageView();
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(library, image.getPath()));

                final int imageIndex = i;

                imageView.setOnMouseClicked(evt -> {
                    Pagination pagination = new Pagination();
                    pagination.setPageCount(images.size());
                    pagination.setPrefSize(960, 540);
                    pagination.setMaxSize(960, 540);

                    pagination.setPageFactory(page -> {

                        ImageView bigImageView = new ImageView();
                        bigImageView.setPreserveRatio(true);
                        bigImageView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(library, images.get(page).getPath()));

                        StackPane stackPane = new StackPane(bigImageView);
                        stackPane.setPrefSize(0, 0); // important
                        stackPane.setMinSize(0, 0); // important
                        bigImageView.fitWidthProperty().bind(stackPane.widthProperty().multiply(.8));
                        bigImageView.fitHeightProperty().bind(stackPane.heightProperty().multiply(.8));

                        return stackPane;
                    });

                    pagination.setCurrentPageIndex(imageIndex);

                    rootPane.getOverlayPane().setContent(pagination);

                    Platform.runLater(() -> pagination.requestFocus());
                });

                StackPane imageWrapper = new StackPane(imageView);
                imageWrapper.getStyleClass().add("image-wrapper");
                if (i == images.size() - 1) {
                    imageWrapper.getStyleClass().add("last");
                }

                thumbnailBox.getChildren().add(imageWrapper);
            }
        }
    }
}