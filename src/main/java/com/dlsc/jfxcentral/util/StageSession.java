package com.dlsc.jfxcentral.util;

import com.dlsc.jfxcentral.CustomStage;
import javafx.beans.InvalidationListener;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class StageSession {

    private final Logger LOG = Logger.getLogger(StageSession.class.getSimpleName());

    private final Stage stage;
    private final Preferences preferences;

    public StageSession(Stage stage, Preferences preferences) {
        this.stage = stage;
        this.preferences = preferences;

        restoreStage();

        InvalidationListener stageListener = it -> {
            try {
                saveStage();
            } catch (SecurityException ex) {
                LOG.throwing(StageSession.class.getName(), "init", ex);
            }
        };

        stage.xProperty().addListener(stageListener);
        stage.yProperty().addListener(stageListener);
        stage.widthProperty().addListener(stageListener);
        stage.heightProperty().addListener(stageListener);
    }

    private void saveStage() throws SecurityException {
        Preferences preferences = getPreferences();
        preferences.putDouble("x", stage.getX());
        preferences.putDouble("y", stage.getY());
        preferences.putDouble("width", stage.getWidth());
        preferences.putDouble("height", stage.getHeight());
    }

    private void restoreStage() throws SecurityException {
        Preferences preferences = getPreferences();
        double x = preferences.getDouble("x", stage.getX());
        double y = preferences.getDouble("y", stage.getY());
        double w = preferences.getDouble("width", stage.getWidth());
        double h = preferences.getDouble("height", stage.getHeight());

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        stage.setX(Math.max(0, x));
        stage.setY(Math.max(0, y));
        stage.setWidth(Math.max(CustomStage.MIN_STAGE_WIDTH, Math.min(w, bounds.getWidth())));
        stage.setHeight(Math.max(CustomStage.MIN_STAGE_HEIGHT, Math.min(h, bounds.getHeight())));
    }

    public Preferences getPreferences() throws SecurityException {
        return preferences;
    }
}
