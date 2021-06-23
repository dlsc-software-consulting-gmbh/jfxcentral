package com.dlsc.jfxcentral.panels;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class AdvancedScrollPane extends ScrollPane {

    private final Region shadow = new Region();
    private final Button scrollToTopButton = new Button("Scroll Up");

    public AdvancedScrollPane() {
        super();
        init();
    }

    public AdvancedScrollPane(Node content) {
        super(content);
        init();
    }

    private final BooleanProperty showScrollToTopButton = new SimpleBooleanProperty(this, "showScrollToTopButton", false);

    public final boolean isShowScrollToTopButton() {
        return showScrollToTopButton.get();
    }

    public final BooleanProperty showScrollToTopButtonProperty() {
        return showScrollToTopButton;
    }

    public final void setShowScrollToTopButton(boolean showScrollToTopButton) {
        this.showScrollToTopButton.set(showScrollToTopButton);
    }

    private final BooleanProperty scrollToTopButtonNeeded = new SimpleBooleanProperty();

    private void init() {
        skinProperty().addListener(it -> getChildren().addAll(shadow, scrollToTopButton));

        scrollToTopButton.setGraphic(new FontIcon());
        scrollToTopButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        scrollToTopButton.getStyleClass().addAll("scroll-to-top-button", "floating-action-button");
        scrollToTopButton.setVisible(false);
        scrollToTopButton.setOpacity(0);
        scrollToTopButton.setOnAction(evt -> {
            KeyValue value = new KeyValue(vvalueProperty(), 0);
            KeyFrame frame = new KeyFrame(Duration.millis(150), value);
            Timeline timeline = new Timeline(frame);
            timeline.play();
        });

        scrollToTopButtonNeeded.bind(vvalueProperty().greaterThan(0));
        scrollToTopButtonNeeded.addListener((it, oldNeeded, newNeeded) -> {
            if (isShowScrollToTopButton()) {
                if (newNeeded) {
                    showButton();
                } else {
                    hideButton();
                }
            }
        });

        showScrollToTopButton.addListener(it -> {
            if (isShowScrollToTopButton()) {
                if (scrollToTopButtonNeeded.get()) {
                    showButton();
                }
            } else {
                hideButton();
            }
        });

        getStyleClass().add("advanced-scroll-pane");
        setFitToWidth(true);
        setPannable(true);

        shadow.setManaged(false);
        shadow.getStyleClass().add("shadow");
        shadow.setMouseTransparent(true);
        shadow.visibleProperty().bind(vvalueProperty().greaterThan(0).or(alwaysShowFullShadowProperty()));

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

        vvalueProperty().addListener(it -> {
            if (lastOffset != computeOffset()) {
                requestLayout();
            }
        });
        showShadowProperty().addListener(it -> requestLayout());
    }

    private final BooleanProperty alwaysShowVerticalScrollBar = new SimpleBooleanProperty(this, "alwaysShowVerticalScrollBar", false);

    public boolean isAlwaysShowVerticalScrollBar() {
        return alwaysShowVerticalScrollBar.get();
    }

    public BooleanProperty alwaysShowVerticalScrollBarProperty() {
        return alwaysShowVerticalScrollBar;
    }

    public void setAlwaysShowVerticalScrollBar(boolean alwaysShowVerticalScrollBar) {
        this.alwaysShowVerticalScrollBar.set(alwaysShowVerticalScrollBar);
    }

    private final BooleanProperty alwaysShowHorizontalScrollBar = new SimpleBooleanProperty(this, "alwaysShowHorizontalScrollBar", false);

    public boolean isAlwaysShowHorizontalScrollBar() {
        return alwaysShowHorizontalScrollBar.get();
    }

    public BooleanProperty alwaysShowHorizontalScrollBarProperty() {
        return alwaysShowHorizontalScrollBar;
    }

    public void setAlwaysShowHorizontalScrollBar(boolean alwaysShowHorizontalScrollBar) {
        this.alwaysShowHorizontalScrollBar.set(alwaysShowHorizontalScrollBar);
    }

    private void hideButton() {
        FadeTransition transition = new FadeTransition(Duration.millis(150), scrollToTopButton);
        transition.setToValue(0);
        transition.setOnFinished(evt -> scrollToTopButton.setVisible(false));
        transition.play();
    }

    private void showButton() {
        scrollToTopButton.setVisible(true);
        FadeTransition transition = new FadeTransition(Duration.millis(150), scrollToTopButton);
        transition.setToValue(1);
        transition.play();
    }

    private final BooleanProperty showShadow = new SimpleBooleanProperty(this, "showShadow", true);

    public final BooleanProperty showShadowProperty() {
        return showShadow;
    }

    public final boolean isShowShadow() {
        return showShadow.get();
    }

    public final void setShowShadow(boolean show) {
        showShadow.set(show);
    }

    private final BooleanProperty alwaysShowFullShadow = new SimpleBooleanProperty(this, "alwaysShowFullShadow", false);

    public final boolean isAlwaysShowFullShadow() {
        return alwaysShowFullShadow.get();
    }

    public final BooleanProperty alwaysShowFullShadowProperty() {
        return alwaysShowFullShadow;
    }

    public final void setAlwaysShowFullShadow(boolean alwaysShowFullShadow) {
        this.alwaysShowFullShadow.set(alwaysShowFullShadow);
    }

    private static final int SHADOW_HEIGHT = 30;


    @Override
    protected void layoutChildren() {
        Insets insets = getInsets();
        double w = getWidth();
        double h = getHeight();

        if (isShowShadow()) {
            double offset = computeOffset();
            shadow.resizeRelocate(-10, insets.getTop() - shadow.prefHeight(-1) - SHADOW_HEIGHT + offset, w + 20, shadow.prefHeight(-1) - 1);
            lastOffset = offset;
        }

        double pw = scrollToTopButton.prefWidth(-1);
        double ph = scrollToTopButton.prefHeight(-1);

        scrollToTopButton.resizeRelocate(w - pw - 10, 25, pw, ph);

        super.layoutChildren();
    }

    private double lastOffset;

    private double computeOffset() {
        if (isAlwaysShowFullShadow()) {
            return SHADOW_HEIGHT;
        } else {
            Node content = getContent();
            if (content != null) {
                return Math.min(getVvalue() * getContent().prefHeight(-1), SHADOW_HEIGHT);
            }

            return 0;
        }
    }
}
