package com.dlsc.jfxcentral.util;

import com.jpro.webapi.WebAPI;
import com.jpro.webapi.WebAPI.FileUploader;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.File;

public class FileUploadStackPane extends StackPane {

    public FileUploader fileHandler;

    public FileUploadStackPane(Node... children) {
        super(children);

        getStyleClass().add("file-upload-stack-pane");

        textProperty().addListener(it -> System.out.println(getText()));

        sceneProperty().addListener(it -> {
            if (getScene() != null) {
                System.out.println("making file upload node");
                fileHandler = WebAPI.getWebAPI(getScene()).makeFileUploadNode(FileUploadStackPane.this);
                fileHandler.setSelectFileOnClick(false);
                fileHandler.setSelectFileOnDrop(true);

                fileHandler.fileDragOverProperty().addListener((o, oldValue, newValue) -> {
                    if (newValue) {
                        getStyleClass().add("file-drag");
                    } else {
                        getStyleClass().remove("file-drag");
                    }
                });

                fileHandler.setOnFileSelected((file) -> {
                    updatePercentage();
                    fileHandler.uploadFile();
                });

                fileHandler.progressProperty().addListener((obs, oldV, newV) -> updatePercentage());

                fileHandler.uploadedFileProperty().addListener(it2 -> setText("DONE"));

                uploadedFile.bind(fileHandler.uploadedFileProperty());
                progress.bind(fileHandler.progressProperty());
            }
        });
    }

    private final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper(this, "progress");

    public double getProgress() {
        return progress.get();
    }

    public ReadOnlyDoubleProperty progressProperty() {
        return progress.getReadOnlyProperty();
    }

    private final ObjectProperty<File> uploadedFile = new SimpleObjectProperty<>(this, "uploadedFile");

    public File getUploadedFile() {
        return uploadedFile.get();
    }

    public ObjectProperty<File> uploadedFileProperty() {
        return uploadedFile;
    }

    public void setUploadedFile(File uploadedFile) {
        this.uploadedFile.set(uploadedFile);
    }

    private final StringProperty defaultText = new SimpleStringProperty(this, "defaultText");

    public String getDefaultText() {
        return defaultText.get();
    }

    public StringProperty defaultTextProperty() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText.set(defaultText);
    }

    private final StringProperty text = new SimpleStringProperty(this, "text");

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public FileUploader getFileHandler() {
        return fileHandler;
    }

    private void updatePercentage() {
        String percentages = (int) (fileHandler.getProgress() * 100) + "%";
        setText(percentages);

        if (fileHandler.getProgress() == 1.0) {
            setText(getDefaultText());
        }
    }
}