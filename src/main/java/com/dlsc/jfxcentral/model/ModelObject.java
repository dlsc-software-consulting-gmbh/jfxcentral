package com.dlsc.jfxcentral.model;

public abstract class ModelObject {

    private String id;

    protected ModelObject() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
