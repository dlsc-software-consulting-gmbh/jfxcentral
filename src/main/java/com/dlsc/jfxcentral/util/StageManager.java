package com.dlsc.jfxcentral.util;

import javafx.beans.InvalidationListener;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class StageManager {

    private final Logger LOG = Logger.getLogger(StageManager.class.getSimpleName());

    private final Stage stage;
    private final Preferences preferences;

    private double minWidth;
    private double minHeight;

    public static StageManager install(Stage stage, String path) {
        return install(stage, path, 850, 800);
    }

    public static StageManager install(Stage stage, String path, double minWidth, double minHeight) {
        StageManager manager = new StageManager(stage, path, minWidth, minHeight);
        return manager;
    }

    private StageManager(Stage stage, String path, double minWidth, double minHeight) {
        if (minWidth <= 0) {
            throw new IllegalArgumentException("min width must be larger than 0");
        }
        if (minHeight <= 0) {
            throw new IllegalArgumentException("min height must be larger than 0");
        }

        this.stage = stage;
        this.minWidth = minWidth;
        this.minHeight = minHeight;

        preferences = Preferences.userRoot().node(path);

        restoreStage();

        InvalidationListener stageListener = it -> {
            try {
                saveStage();
            } catch (SecurityException ex) {
                LOG.throwing(StageManager.class.getName(), "init", ex);
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

        double x = preferences.getDouble("x", -1);
        double y = preferences.getDouble("y", -1);
        double w = preferences.getDouble("width", stage.getWidth());
        double h = preferences.getDouble("height", stage.getHeight());

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        if (x == -1 && y == -1) {
            stage.centerOnScreen();
        } else {
            stage.setX(Math.max(0, x));
            stage.setY(Math.max(0, y));
        }

        stage.setWidth(Math.max(minWidth, Math.min(w, bounds.getWidth())));
        stage.setHeight(Math.max(minHeight, Math.min(h, bounds.getHeight())));
    }

    public Preferences getPreferences() throws SecurityException {
        return preferences;
    }
}
