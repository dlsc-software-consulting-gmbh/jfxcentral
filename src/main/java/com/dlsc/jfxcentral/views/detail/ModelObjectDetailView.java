package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.EmptySelectionModel;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.cells.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public abstract class ModelObjectDetailView<T extends ModelObject> extends DetailView<T> {

    protected VBox content = new VBox();

    public ModelObjectDetailView(RootPane rootPane, View view) {
        super(rootPane, view);

        setContent(content);
        content.setPrefWidth(0);
        content.setMinWidth(0);

        selectedItemProperty().addListener((obs, oldItem, newItem) -> updateView(oldItem, newItem));
    }

    protected void createStandardBoxes() {
        DataRepository repository = DataRepository.getInstance();
        createListViewBox("Libraries", listView -> new DetailLibraryCell(getRootPane(), false), item -> repository.getLibrariesByModelObject(item));
        createListViewBox("Tools", listView -> new DetailToolCell(getRootPane()), item -> repository.getToolsByModelObject(item));
        createListViewBox("Blogs", listView -> new DetailBlogCell(getRootPane(), false), item -> repository.getBlogsByModelObject(item));
        createListViewBox("Tutorials", listView -> new DetailTutorialCell(getRootPane(), false), item -> repository.getTutorialsByModelObject(item));
        if (!getRootPane().isMobile()) {
            createListViewBox("Downloads", listView -> new DetailDownloadCell(getRootPane(), false), item -> repository.getDownloadsByModelObject(item));
        }
        createListViewBox("Videos", listView -> new DetailVideoCell(getRootPane(), false), item -> repository.getVideosByModelObject(item));
        createListViewBox("Books", listView -> new DetailBookCell(getRootPane(), false), item -> repository.getBooksByModelObject(item));
        createListViewBox("People", listView -> new DetailPersonCell(getRootPane(), false), item -> repository.getPeopleByModelObject(item));
        createListViewBox("Companies", listView -> new DetailCompanyCell(false, getRootPane(), false), item -> repository.getCompaniesByModelObject(item));
        createListViewBox("Apps", listView -> new DetailRealWorldAppCell(getRootPane(), false), item -> repository.getRealWorldAppsByModelObject(item));
        createListViewBox("Tips", listView -> new DetailTipCell(getRootPane(), false), item -> repository.getTipsByModelObject(item));
    }

    protected boolean isUsingMasterView() {
        return true;
    }

    protected void createReadMeBox(Callback<T, String> baseUrlProvider, Callback<T, StringProperty> textPropertyProvider) {
        createReadMeBox("Readme", baseUrlProvider, textPropertyProvider);
    }

    protected SectionPane createReadMeBox(String title, Callback<T, String> baseUrlProvider, Callback<T, StringProperty> textPropertyProvider) {
        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle(title);

        MarkdownView markdownView = new MarkdownView();
        sectionPane.getNodes().add(markdownView);

        selectedItemProperty().addListener(it -> {
            T item = getSelectedItem();
            if (item != null) {
                markdownView.setBaseURL(baseUrlProvider.call(item));
                markdownView.mdStringProperty().bind(textPropertyProvider.call(item));
            }
        });

        sectionPane.visibleProperty().bind(Bindings.isNotEmpty(markdownView.mdStringProperty()));
        sectionPane.managedProperty().bind(Bindings.isNotEmpty(markdownView.mdStringProperty()));

        content.getChildren().add(sectionPane);

        return sectionPane;
    }

    private <M extends ModelObject> void createListViewBox(String name, Callback<ListView<M>, ListCell<M>> cellFactory, Callback<T, ObservableList<M>> listProvider) {
        AdvancedListView<M> listView = new AdvancedListView<>();
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(cellFactory);

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle(name);
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> {
            T item = getSelectedItem();
            if (item != null) {
                sectionPane.setSubtitle(name + " related to " + item.getName());
                Bindings.bindContent(listView.getItems(), listProvider.call(item));
            } else {
                sectionPane.setSubtitle("");
                listView.setItems(FXCollections.observableArrayList());
            }
        });

        sectionPane.visibleProperty().bind(listView.itemsProperty().emptyProperty().not());
        sectionPane.managedProperty().bind(listView.itemsProperty().emptyProperty().not());

        content.getChildren().add(sectionPane);
    }

    protected abstract void createTitleBox();

    protected void updateView(T oldObject, T newObject) {
    }
}
