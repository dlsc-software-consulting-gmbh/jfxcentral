package com.dlsc.jfxcentral.views.page;

import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.Display;
import com.dlsc.jfxcentral.views.View;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class TopMenu extends ToolBar {

    private final Page page;

    public TopMenu(Page page) {
        super();

        setOrientation(Orientation.VERTICAL);

        this.page = page;

        expandedProperty().bind(Bindings.createBooleanBinding(() -> {
            // important to only run this when the menu has been added to the scene,
            // otherwise the logo will flicker
            if (getScene() != null) {
                Display display = page.getRootPane().getDisplay();
                if (display.equals(Display.DESKTOP) || display.equals(Display.WEB)) {
                    return page.getWidth() > 1000;
                }
            }

            return true;
        }, page.widthProperty()));

        page.getRootPane().expandedProperty().bind(expandedProperty());

        getStyleClass().add("top-menu");

        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> isExpanded() ? 100d : 50d, expandedProperty()));
        imageView.setOnMouseClicked(evt -> setExpanded(!isExpanded()));
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("duke");

        ToggleButton homeButton = createButton("Home", View.HOME, new FontIcon(StandardIcons.HOME));
        ToggleButton peopleButton = createButton("People", View.PEOPLE, new FontIcon(StandardIcons.PERSON));
        ToggleButton companyButton = createButton("Companies", View.COMPANIES, new FontIcon(StandardIcons.COMPANY));
        ToggleButton blogsButton = createButton("Blogs", View.BLOGS, new FontIcon(StandardIcons.BLOG));
        ToggleButton booksButton = createButton("Books", View.BOOKS, new FontIcon(StandardIcons.BOOK));
        ToggleButton tutorialsButton = createButton("Tutorials", View.TUTORIALS, new FontIcon(StandardIcons.TUTORIAL));
        ToggleButton libsButton = createButton("Libraries", View.LIBRARIES, new FontIcon(StandardIcons.LIBRARY));
        ToggleButton toolsButton = createButton("Tools", View.TOOLS, new FontIcon(StandardIcons.TOOL));
        ToggleButton videosButton = createButton("Videos", View.VIDEOS, new FontIcon(StandardIcons.VIDEO));
        ToggleButton openJfxButton = createButton("Open JFX", View.OPENJFX, new FontIcon(StandardIcons.OPENJFX));
        ToggleButton realWorldAppsButton = createButton("Real World Apps", View.REAL_WORLD, new FontIcon(StandardIcons.REAL_WORLD));
        ToggleButton downloadsButton = createButton("Downloads", View.DOWNLOADS, new FontIcon(StandardIcons.DOWNLOAD));

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(
                homeButton,
                openJfxButton,
                peopleButton,
                companyButton,
                blogsButton,
                videosButton,
                booksButton,
                toolsButton,
                libsButton,
                tutorialsButton,
                realWorldAppsButton,
                downloadsButton);

        toggleGroup.selectToggle(homeButton);

        Region spacer = new Region();
        spacer.getStyleClass().add("spacer");
        spacer.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // wrapping these buttons as the links (JPro) need it.
        getItems().addAll(
                wrap(homeButton),
                wrap(openJfxButton),
                wrap(realWorldAppsButton),
                wrap(peopleButton),
                wrap(companyButton),
                wrap(blogsButton),
                wrap(videosButton),
                wrap(booksButton),
                wrap(toolsButton),
                wrap(libsButton),
                wrap(tutorialsButton),
                wrap(downloadsButton));

        switch (page.getView()) {
            case HOME:
                toggleGroup.selectToggle(homeButton);
                break;
            case REAL_WORLD:
                toggleGroup.selectToggle(realWorldAppsButton);
                break;
            case OPENJFX:
                toggleGroup.selectToggle(openJfxButton);
                break;
            case PEOPLE:
                toggleGroup.selectToggle(peopleButton);
                break;
            case COMPANIES:
                toggleGroup.selectToggle(companyButton);
                break;
            case TUTORIALS:
                toggleGroup.selectToggle(tutorialsButton);
                break;
            case TOOLS:
                toggleGroup.selectToggle(toolsButton);
                break;
            case LIBRARIES:
                toggleGroup.selectToggle(libsButton);
                break;
            case BLOGS:
                toggleGroup.selectToggle(blogsButton);
                break;
            case BOOKS:
                toggleGroup.selectToggle(booksButton);
                break;
            case VIDEOS:
                toggleGroup.selectToggle(videosButton);
                break;
            case DOWNLOADS:
                toggleGroup.selectToggle(downloadsButton);
                break;
        }

        toggleGroup.selectedToggleProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                toggleGroup.selectToggle(oldSelection);
                return;
            }
        });

        expandedProperty().addListener(it -> updateExpandedPseudoClass());
        updateExpandedPseudoClass();
    }

    private Node wrap(Node node) {
        return new StackPane(node);
    }

    private void updateExpandedPseudoClass() {
        pseudoClassStateChanged(PseudoClass.getPseudoClass("expanded"), isExpanded());
    }

    private ToggleButton createButton(String name, View view, FontIcon icon) {
        ToggleButton button = new ToggleButton(name);
        button.contentDisplayProperty().bind(Bindings.createObjectBinding(() -> {
            Display display = page.getRootPane().getDisplay();
            if (display == null) {
                return ContentDisplay.LEFT;
            }

            switch (display) {
                case TABLET:
                case PHONE:
                    // TODO: currently treated equal (tablet, phone).
                    return isExpanded() ? ContentDisplay.LEFT : ContentDisplay.GRAPHIC_ONLY;
                case DESKTOP:
                case WEB:
                    if (isExpanded()) {
                        return ContentDisplay.LEFT;
                    }
                    return ContentDisplay.GRAPHIC_ONLY;
                default:
                    return ContentDisplay.LEFT;
            }
        }, expandedProperty(), page.getRootPane().displayProperty()));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setGraphic(wrap(icon));
        Util.setLink(button, PageUtil.getLink(view), name);

        switch (view) {
            case HOME:
                button.setTooltip(new Tooltip("Home"));
                break;
            case OPENJFX:
                button.setTooltip(new Tooltip("OpenJFX"));
                break;
            case PEOPLE:
                button.setTooltip(new Tooltip("People"));
                break;
            case TUTORIALS:
                button.setTooltip(new Tooltip("Tutorials"));
                break;
            case REAL_WORLD:
                button.setTooltip(new Tooltip("Real World Applications"));
                break;
            case COMPANIES:
                button.setTooltip(new Tooltip("Companies"));
                break;
            case TOOLS:
                button.setTooltip(new Tooltip("Tools"));
                break;
            case LIBRARIES:
                button.setTooltip(new Tooltip("Libraries"));
                break;
            case BLOGS:
                button.setTooltip(new Tooltip("Blogs"));
                break;
            case BOOKS:
                button.setTooltip(new Tooltip("Books"));
                break;
            case VIDEOS:
                button.setTooltip(new Tooltip("Videos"));
                break;
            case DOWNLOADS:
                button.setTooltip(new Tooltip("Downloads"));
                break;
        }
        return button;
    }

    private Node wrap(FontIcon icon) {
        StackPane stackPane = new StackPane(icon);
        stackPane.getStyleClass().add("icon-wrapper");
        return stackPane;
    }

    private final BooleanProperty expanded = new SimpleBooleanProperty(this, "expanded", true);

    public boolean isExpanded() {
        return expanded.get();
    }

    public BooleanProperty expandedProperty() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded.set(expanded);
    }
}