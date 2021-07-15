package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.views.IPage;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.jpro.web.Util;
import com.jpro.web.sessionmanager.SessionManager;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.Comparator;


public class WebView extends com.jpro.web.View {

    private final boolean mobile;
    private final String initialURL;

    public WebView(String initialURL, boolean mobile) {
        this.initialURL = initialURL;
        this.mobile = mobile;
    }

    @Override
    public String title() {
        IPage currentPage = rootPane.getCurrentPage();
        if (currentPage != null) {
            return currentPage.getTitle();
        }
        return "";
    }

    @Override
    public String description() {
        IPage currentPage = rootPane.getCurrentPage();
        if (currentPage != null) {
            return currentPage.getDescription();
        }
        return "";
    }

    @Override
    public boolean fullscreen() {
        return true;
    }

    private RootPane rootPane = new RootPane();

    @Override
    public Node content() {
        rootPane.init(mobile);
        handleURL(initialURL);
//        rootPane.setMaxWidth(1200);

        StackPane wrapper = new StackPane(rootPane, rootPane.getOverlayPane());

        wrapper.getStyleClass().add("root-wrapper");
        if (mobile) {
            wrapper.getStyleClass().add("mobile");
        }
        return wrapper;
    }

    // IF this returns true, we don't query for a new page
    @Override
    public boolean handleURL(String s) {
        View view = PageUtil.getViewFromURL(s);
        String id = PageUtil.getIdFromURL(s);

        if(id == null && view != View.HOME && view != View.OPENJFX) {
            Object obj = DataRepository.getInstance()
                    .getList(PageUtil.getClassOfView(view))
                    .stream().sorted(Comparator.comparing((ModelObject x) -> x.getName().toLowerCase()))
                    .findFirst().get();
            String firstID = ((ModelObject) obj).getId();
            Platform.runLater(() -> {
                Util.gotoPage(rootPane, s + "/" + firstID);
            });
            return true;
        }

        System.out.println("view: " + view);
        System.out.println("id " + id);

        rootPane.setView(view);

        IPage currentPage = rootPane.getCurrentPage();
        System.out.println("current page: " + currentPage);

        ModelObject item = null;

        if (id != null) {
            item = DataRepository.getInstance().getByID(PageUtil.getClassOfView(view),id);
        }

        if (currentPage != null && (item != null || rootPane.isMobile())) {
            System.out.println("current page != null, current page = " + currentPage.getClass().getSimpleName());
            // ok to pass null when "mobile", resets to master views
            currentPage.setSelectedItem(item);
        }

        return true;
    }
}
