package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.data.model.ModelObject;

public interface IPage<T extends ModelObject> {
    String getTitle();

    String getDescription();

    void setSelectedItem(T item);
}
