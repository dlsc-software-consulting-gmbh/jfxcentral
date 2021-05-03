package com.dlsc.jfxcentral.model;

import java.util.List;

public class Video extends ModelObject {

    public enum Type {
        YOUTUBE
    }

    private Type type;
    private String title;
    private String description;
    private List<String> personIds;
    private String image;

    public Video() {
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

    public List<String> getPersonIds() {
        return personIds;
    }

    public void setPersonIds(List<String> personIds) {
        this.personIds = personIds;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
