package com.dlsc.jfxcentral.views;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class LoadingView extends VBox {

    public LoadingView(Runnable callback) {
        getStyleClass().add("loading-view");

        Label label = new Label();
        label.textProperty().bind(statusProperty());

        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(progressProperty());

        getChildren().setAll(label, progressBar);

        progressProperty().addListener(it -> maybeCallCallback(callback));
        maybeCallCallback(callback);
    }

    private void maybeCallCallback(Runnable callback) {
        System.out.println(getProgress());
        if (getProgress() >= 1) {
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
