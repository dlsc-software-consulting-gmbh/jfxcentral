package com.dlsc.jfxcentral;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.StackPane;

public abstract class ViewPane extends StackPane {

    protected ViewPane() {
    }

    private ObjectProperty<View> view = new SimpleObjectProperty<>(this, "view", View.HOME);

    public View getView() {
        return view.get();
    }

    public ObjectProperty<View> viewProperty() {
        return view;
    }

    public void setView(View view) {
        this.view.set(view);
    }

    private ObjectProperty<Display> display = new SimpleObjectProperty<>(this, "display", Display.DESKTOP);

    public Display getDisplay() {
        return display.get();
    }

    public ObjectProperty<Display> displayProperty() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display.set(display);
    }
}
