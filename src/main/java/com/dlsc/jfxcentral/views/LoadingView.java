package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.data.DataRepository;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class LoadingView extends VBox {

    private final InvalidationListener progressListener = it -> Platform.runLater(() -> setProgress(DataRepository.getInstance().getProgress()));

    private final WeakInvalidationListener weakProgressListener = new WeakInvalidationListener(progressListener);

    private final InvalidationListener statusListener = it -> Platform.runLater(() -> setStatus(DataRepository.getInstance().getMessage()));

    private final WeakInvalidationListener weakStatusListener = new WeakInvalidationListener(statusListener);

    public LoadingView(Runnable callback) {
        getStyleClass().add("loading-view");

        Label label = new Label();
        label.textProperty().bind(statusProperty());
        label.setWrapText(true);
        label.setMinHeight(Region.USE_PREF_SIZE);
        label.setTextAlignment(TextAlignment.CENTER);

        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(progressProperty());

        getChildren().setAll(label, progressBar);

        DataRepository.getInstance().progressProperty().addListener(weakProgressListener);
        DataRepository.getInstance().messageProperty().addListener(weakStatusListener);

        progressProperty().addListener(it -> maybeCallCallback(callback));
        maybeCallCallback(callback);
    }

    private void maybeCallCallback(Runnable callback) {
        if (getProgress() >= 1) {
            DataRepository.getInstance().progressProperty().removeListener(weakProgressListener);
            DataRepository.getInstance().messageProperty().removeListener(weakStatusListener);
            callback.run();
        }
    }

    private final StringProperty status = new SimpleStringProperty(this, "status");

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    private final DoubleProperty progress = new SimpleDoubleProperty(this, "progress");

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }
}
