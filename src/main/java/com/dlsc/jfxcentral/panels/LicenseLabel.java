package com.dlsc.jfxcentral.panels;

import com.dlsc.jfxcentral.model.License;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;

public class LicenseLabel extends Label {

    public LicenseLabel() {
        getStyleClass().add("license-label");

        textProperty().bind(Bindings.createStringBinding(() -> {
            if (getLicense() == null) {
                return "Unknown";
            }

            return getLicense().name();
        }, licenseProperty()));
    }

    private final ObjectProperty<License> license = new SimpleObjectProperty<>(this, "license", License.OTHER);

    public License getLicense() {
        return license.get();
    }

    public ObjectProperty<License> licenseProperty() {
        return license;
    }

    public void setLicense(License license) {
        this.license.set(license);
    }
}
