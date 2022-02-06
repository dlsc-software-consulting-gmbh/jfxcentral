package com.dlsc.jfxcentral.views.mobile;

import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.page.StandardIcons;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

public class MobileTopMenu extends VBox {

    public MobileTopMenu() {
        super();

        getStyleClass().addAll("top-menu", "mobile");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
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
        ToggleButton tipsButton = createButton("Tips & Tricks", View.TIPS, new FontIcon(StandardIcons.TIP));

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(
                homeButton,
                openJfxButton,
                tipsButton,
                peopleButton,
                companyButton,
                blogsButton,
                videosButton,
                booksButton,
                toolsButton,
                libsButton,
                tutorialsButton,
                realWorldAppsButton);

        toggleGroup.selectToggle(homeButton);

        // wrapping these buttons as the links (JPro) need it.
        getChildren().addAll(
                wrap(homeButton),
                wrap(openJfxButton),
                wrap(tipsButton),
                wrap(realWorldAppsButton),
                wrap(peopleButton),
                wrap(companyButton),
                wrap(blogsButton),
                wrap(videosButton),
                wrap(booksButton),
                wrap(toolsButton),
                wrap(libsButton),
                wrap(tutorialsButton));

        toggleGroup.selectedToggleProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                toggleGroup.selectToggle(oldSelection);
                return;
            }
        });

        viewProperty().addListener(it -> {
            View view = getView();
            if (view == null) {
                return;
            }

            switch (view) {
                case HOME:
                    toggleGroup.selectToggle(homeButton);
                    break;
                case REAL_WORLD:
                    toggleGroup.selectToggle(realWorldAppsButton);
                    break;
                case OPENJFX:
                    toggleGroup.selectToggle(openJfxButton);
                    break;
                case TIPS:
                    toggleGroup.selectToggle(tipsButton);
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
            }
        });
    }

    private final ObjectProperty<View> view = new SimpleObjectProperty<>(this, "view");

    public View getView() {
        return view.get();
    }

    public ObjectProperty<View> viewProperty() {
        return view;
    }

    public void setView(View view) {
        this.view.set(view);
    }

    private Node wrap(Node node) {
        return new StackPane(node);
    }

    private ToggleButton createButton(String name, View view, FontIcon icon) {
        ToggleButton button = new ToggleButton(name);
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
            case TIPS:
                button.setTooltip(new Tooltip("Tips & Tricks"));
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
            case DEVELOPMENT:
                button.setTooltip(new Tooltip("Online Developer Tools"));
                break;
        }
        return button;
    }

    private Node wrap(FontIcon icon) {
        StackPane stackPane = new StackPane(icon);
        stackPane.getStyleClass().add("icon-wrapper");
        return stackPane;
    }
}