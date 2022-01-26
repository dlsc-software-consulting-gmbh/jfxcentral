package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.util.DeveloperTool;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.views.IPage;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.Optional;


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

        System.out.println("view: " + view + ", id = " + id);

        rootPane.setView(view);

        IPage currentPage = rootPane.getCurrentPage();

        ModelObject item = null;

        if (id != null) {
            if (view.equals(View.DEVELOPMENT)) {
                Optional<DeveloperTool> optional = rootPane.getDeveloperTools().filtered(o -> o.getId().equals(id)).stream().findFirst();
                if (optional.isPresent()) {
                    item = optional.get();
                } else {
                    System.out.println("no item present");
                }
            } else {
                item = DataRepository.getInstance().getByID(PageUtil.getClassOfView(view), id);
            }
        }

        System.out.println("item: " + item);

        if (currentPage != null) {
            if (isMobile() || item != null) {
                currentPage.showItem(item); // even when item is null (because mobile)
            }
        }

        return true;
    }
}
