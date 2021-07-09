package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.GlassPane;
import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.panels.PrettyScrollPane;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.mobile.MobileHeader;
import com.dlsc.jfxcentral.views.mobile.MobileTopMenu;
import com.dlsc.jfxcentral.views.mobile.page.*;
import com.dlsc.jfxcentral.views.page.*;
import com.gluonhq.attach.display.DisplayService;
import com.jpro.webapi.WebAPI;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Locale;

public class RootPane extends StackPane {

    private IPage<?> page;

    private OverlayPane overlayPane = new OverlayPane();

    private StackPane contentPane;

    private boolean mobile;

    private HiddenSidesPane hiddenSidesPane;
    private BorderPane borderPane;
    private StackPane compassWrapper;
    private GlassPane glassPane;
    private Locale locale = Locale.US;

    public RootPane() {
        getStyleClass().add("root-pane");
    }

    public void init(boolean mobile) {
        overlayPane.getStylesheets().add(JFXCentralApp.class.getResource("styles.css").toExternalForm());
        overlayPane.getStylesheets().add(JFXCentralApp.class.getResource("markdown.css").toExternalForm());

        if (mobile) {
            initMobile();
        } else {
            initDesktopOrBrowser();
        }

        viewProperty().addListener((obs, oldView, newView) -> updateView());
    }

    private void initMobile() {
        mobile = true;

        getStylesheets().add(JFXCentralApp.class.getResource("mobile.css").toExternalForm());

        BorderPane borderPane = new BorderPane();

        MobileTopMenu topMenu = new MobileTopMenu();
        topMenu.viewProperty().bind(viewProperty());

        PrettyScrollPane prettyScrollPane = new PrettyScrollPane(topMenu);
        prettyScrollPane.setShowShadow(false);
        prettyScrollPane.setShowScrollToTopButton(false);
        prettyScrollPane.setFitToHeight(true);
        prettyScrollPane.setFitToWidth(true);
        prettyScrollPane.setMobile(true);

        StackPane stackPane = new StackPane(prettyScrollPane); // wrap for shadow effect
        stackPane.getStyleClass().add("top-menu-scrollpane-wrapper");

        hiddenSidesPane = new HiddenSidesPane();
        hiddenSidesPane.setAnimationDelay(Duration.ZERO);
        hiddenSidesPane.setAnimationDuration(Duration.millis(200));
        hiddenSidesPane.setLeft(stackPane);
        hiddenSidesPane.setTriggerDistance(0);

        glassPane = new GlassPane();
        glassPane.setFadeInOutDuration(Duration.millis(200));
        glassPane.hideProperty().bind(Bindings.createBooleanBinding(() -> !hiddenSidesPane.getProperties().containsKey("showPane"), hiddenSidesPane.getProperties()));
        glassPane.setOnMouseClicked(evt -> hiddenSidesPane.hide());

        viewProperty().addListener(it -> hiddenSidesPane.hide());

        contentPane = new StackPane();
        hiddenSidesPane.setContent(contentPane);

        MobileHeader mobileHeader = new MobileHeader(hiddenSidesPane);
        borderPane.setTop(mobileHeader);
        borderPane.setCenter(hiddenSidesPane);

        getChildren().addAll(borderPane);

        updateView();
    }

    private void initDesktopOrBrowser() {
        mobile = false;

        ImageView logoImageView = new ImageView();
        logoImageView.getStyleClass().add("logo-image-view");
        logoImageView.setCursor(Cursor.HAND);

        DoubleBinding imageSizeBinding = Bindings.createDoubleBinding(() -> {
            if (isExpanded()) {
                return 128d;
            }
            return 80d;
        }, expandedProperty());

        logoImageView.fitWidthProperty().bind(imageSizeBinding);
        logoImageView.fitHeightProperty().bind(imageSizeBinding);
        logoImageView.setPreserveRatio(true);

        Util.setLink(logoImageView, PageUtil.getLink(View.HOME), "Home");

        compassWrapper = new StackPane(logoImageView);
        compassWrapper.getStyleClass().add("logo-image-wrapper");
        compassWrapper.maxWidthProperty().bind(logoImageView.fitWidthProperty());
        compassWrapper.maxHeightProperty().bind(logoImageView.fitHeightProperty());
        StackPane.setAlignment(compassWrapper, Pos.TOP_LEFT);

        borderPane = new BorderPane();
        borderPane.setTop(new HeaderPane(this));

        getChildren().setAll(borderPane);

        DisplayService.create().ifPresentOrElse(service -> {
            if (service.isDesktop()) {
                setDisplay(Display.DESKTOP);
            } else if (service.isPhone()) {
                setDisplay(Display.PHONE);
            } else if (service.isTablet()) {
                setDisplay(Display.TABLET);
            }
        }, () -> {
            if (WebAPI.isBrowser()) {
                setDisplay(Display.WEB);
            } else {
                setDisplay(Display.DESKTOP);
            }
        });

        expandedProperty().addListener(it -> updateExpandedPseudoClass());
        updateExpandedPseudoClass();

        contentPane = this;

        updateView();
    }

    private void updateView() {
        if (isMobile()) {
            updateViewMobile();
        } else {
            updateViewDesktopOrBrowser();
        }
    }

    private void updateViewMobile() {
        page = null;

        View view = getView();
        if (view == null) {
            return;
        }

        switch (view) {
            case HOME:
                page = new MobileHomePage(this);
                break;
            case OPENJFX:
                page = new MobileOpenJFXPage(this);
                break;
            case PEOPLE:
                page = new MobilePeoplePage(this);
                break;
            case TUTORIALS:
                page = new MobileTutorialsPage(this);
                break;
            case REAL_WORLD:
                page = new MobileRealWorldAppsPage(this);
                break;
            case DOWNLOADS:
                page = new MobileDownloadsPage(this);
                break;
            case COMPANIES:
                page = new MobileCompaniesPage(this);
                break;
            case TOOLS:
                page = new MobileToolsPage(this);
                break;
            case LIBRARIES:
                page = new MobileLibrariesPage(this);
                break;
            case BLOGS:
                page = new MobileBlogsPage(this);
                break;
            case BOOKS:
                page = new MobileBooksPage(this);
                break;
            case VIDEOS:
                page = new MobileVideosPage(this);
                break;
            default:
                break;
        }

        if (page != null) {
            contentPane.getChildren().setAll((Node) page, glassPane);
        }
    }

    private void updateViewDesktopOrBrowser() {
        page = null;

        View view = getView();
        if (view == null) {
            return;
        }

        switch (view) {
            case HOME:
                page = new HomePage(this);
                break;
            case OPENJFX:
                page = new OpenJFXPage(this);
                break;
            case PEOPLE:
                page = new PeoplePage(this);
                break;
            case TUTORIALS:
                page = new TutorialsPage(this);
                break;
            case REAL_WORLD:
                page = new RealWorldAppsPage(this);
                break;
            case DOWNLOADS:
                page = new DownloadsPage(this);
                break;
            case COMPANIES:
                page = new CompaniesPage(this);
                break;
            case TOOLS:
                page = new ToolsPage(this);
                break;
            case LIBRARIES:
                page = new LibrariesPage(this);
                break;
            case BLOGS:
                page = new BlogsPage(this);
                break;
            case BOOKS:
                page = new BooksPage(this);
                break;
            case VIDEOS:
                page = new VideosPage(this);
                break;
            case TIPS:
                page = new TipsPage(this);
                break;
            default:
                break;
        }

        contentPane.getChildren().setAll(borderPane, compassWrapper);

        borderPane.setCenter((Node) page);
    }

    private void updateExpandedPseudoClass() {
        pseudoClassStateChanged(PseudoClass.getPseudoClass("expanded"), isExpanded());
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

    public IPage<?> getCurrentPage() {
        return page;
    }

    public OverlayPane getOverlayPane() {
        return overlayPane;
    }

    public void showImage(String title, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        StackPane stackPane = new StackPane(imageView);
        stackPane.setPrefSize(image.getWidth(), image.getHeight()); // important
        stackPane.setMinSize(0, 0); // important

        imageView.fitWidthProperty().bind(stackPane.widthProperty().multiply(.8));

        getOverlayPane().setContent(stackPane);
    }

    private ObjectProperty<View> view = new SimpleObjectProperty<>(this, "view");

    public View getView() {
        return view.get();
    }

    public ObjectProperty<View> viewProperty() {
        return view;
    }

    public void setView(View view) {
        this.view.set(view);
    }

    private ObjectProperty<Display> display = new SimpleObjectProperty<>(this, "display");

    public Display getDisplay() {
        return display.get();
    }

    public ObjectProperty<Display> displayProperty() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display.set(display);
    }

    public boolean isMobile() {
        return mobile;
    }

    public Locale getLocale() {
        return locale;
    }
}
