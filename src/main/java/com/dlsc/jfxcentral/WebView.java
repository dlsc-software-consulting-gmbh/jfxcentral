package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.views.IPage;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;


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

        System.out.println("view: " + view);
        System.out.println("id " + id);

        rootPane.setView(view);

        IPage currentPage = rootPane.getCurrentPage();
        System.out.println("current page: " + currentPage);

        ModelObject item = null;

        if (id != null) {
            switch (view) {
                case BLOGS:
                    item = DataRepository.getInstance().getBlogById(id).get();
                    break;
                case BOOKS:
                    item = DataRepository.getInstance().getBookById(id).get();
                    break;
                case LIBRARIES:
                    item = DataRepository.getInstance().getLibraryById(id).get();
                    break;
                case PEOPLE:
                    item = DataRepository.getInstance().getPersonById(id).get();
                    break;
                case REAL_WORLD:
                    item = DataRepository.getInstance().getRealWorldAppById(id).get();
                    break;
                case TOOLS:
                    item = DataRepository.getInstance().getToolById(id).get();
                    break;
                case DOWNLOADS:
                    item = DataRepository.getInstance().getDownloadById(id).get();
                    break;
                case VIDEOS:
                    item = DataRepository.getInstance().getVideoById(id).get();
                    break;
                case COMPANIES:
                    item = DataRepository.getInstance().getCompanyById(id).get();
                    break;
                case TUTORIALS:
                    item = DataRepository.getInstance().getTutorialById(id).get();
                    break;
                case TIPS:
                    item = DataRepository.getInstance().getTipById(id).get();
                    break;
            }
        }

        if (currentPage != null && (item != null || rootPane.isMobile())) {
            System.out.println("current page != null, current page = " + currentPage.getClass().getSimpleName());
            // ok to pass null when "mobile", resets to master views
            currentPage.setSelectedItem(item);
        }

        return true;
    }
}
