package com.dlsc.jfxcentral.panels;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.stage.Screen;
import org.controlsfx.control.PopOver.ArrowLocation;

import java.util.List;

public class PopOverUtil {

    public ArrowLocation getArrowLocation(Node owner) {
        Bounds bounds = owner.localToScreen(owner.getLayoutBounds());

        List<Screen> screens = Screen.getScreensForRectangle(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));

        Screen screen = screens.get(0);

        double spaceAbove = bounds.getMinY();
        double spaceBelow = screen.getVisualBounds().getMaxY() - bounds.getMaxY();
        double spaceLeft = bounds.getMinX();
        double spaceRight = screen.getVisualBounds().getMaxX() - bounds.getMaxX();

        ArrowLocation arrowLocation;

        if (spaceBelow >= spaceAbove) {
            // below
            if (spaceRight >= spaceLeft) {
                // right
                arrowLocation = ArrowLocation.TOP_LEFT;
            } else {
                // left
                arrowLocation = ArrowLocation.TOP_RIGHT;
            }
        } else {
            // above
            if (spaceRight >= spaceLeft) {
                // right
                arrowLocation = ArrowLocation.BOTTOM_LEFT;
            } else {
                // left
                arrowLocation = ArrowLocation.BOTTOM_RIGHT;
            }
        }

        return arrowLocation;
    }

}
