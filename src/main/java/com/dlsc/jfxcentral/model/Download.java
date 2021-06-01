package com.dlsc.jfxcentral.model;

import java.util.ArrayList;
import java.util.List;

public class Download extends ModelObject {

    private String title;
    private String description;
    private Type  type;
    private List<DownloadFile> files = new ArrayList<>();

    public enum Type {
        APPLICATION,
        PRESENTATION,
        DOCUMENTATION
    }

    public enum FileType {
        DMG,
        PKG,
        MSI
    }

    public static class DownloadFile {
        private String url;
        private String fileName;
        private FileType fileType;

        public DownloadFile() {
        }

        public FileType getFileType() {
            return fileType;
        }

        public void setFileType(FileType fileType) {
            this.fileType = fileType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    public Download() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DownloadFile> getFiles() {
        return files;
    }

    public void setFiles(List<DownloadFile> files) {
        this.files = files;
    }
}
