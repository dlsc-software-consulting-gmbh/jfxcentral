package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.page.Page;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;


public class JPROWebView extends com.jpro.web.View {

    String initialURL;

    public JPROWebView(String initialURL) {
        this.initialURL = initialURL;
    }

    @Override
    public String title() {
        Page currentPage = rootPane.getCurrentPage();
        return currentPage.getTitle();
    }

    @Override
    public String description() {
        Page currentPage = rootPane.getCurrentPage();
        return currentPage.getDescription();
    }

    @Override
    public boolean fullscreen() {
        return true;
    }

    private final RootPane rootPane = new RootPane();

    @Override
    public Node content() {
        handleURL(initialURL);

        rootPane.setMaxWidth(1200);

        StackPane wrapper = new StackPane(rootPane);
        wrapper.getStyleClass().add("root-wrapper");
        return wrapper;
    }

    // IF this returns true, we don't query for a new page
    @Override public boolean handleURL(String s) {
        View view = PageUtil.getViewFromURL(s);
        String id = PageUtil.getIdFromURL(s);

        System.out.println("VIEW: " + view);
        System.out.println("ID: " + id);

        rootPane.setView(view);

        if(id == null) {
            return true;
        }

        Page currentPage = rootPane.getCurrentPage();
        ModelObject item = null;

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
        }
        if(item != null) {
            currentPage.setSelectedItem(item);
        }

        return true;
    }
}
