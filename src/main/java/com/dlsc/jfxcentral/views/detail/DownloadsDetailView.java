package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Company;
import com.dlsc.jfxcentral.data.model.Download;
import com.dlsc.jfxcentral.data.model.Download.DownloadType;
import com.dlsc.jfxcentral.data.model.Download.FileType;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.panels.PrettyListView;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DownloadsDetailView extends DetailView<Download> {

    private final FilterView.FilterGroup<Download> downloadTypeGroup = new FilterView.FilterGroup<>("Type");
    private final FilterView.FilterGroup<Download> fileTypeGroup = new FilterView.FilterGroup<>("File Type");
    private final FilterView.FilterGroup<Download> personGroup = new FilterView.FilterGroup<>("Person");
    private final FilterView.FilterGroup<Download> companyGroup = new FilterView.FilterGroup<>("Company");

    public DownloadsDetailView(RootPane rootPane) {
        super(rootPane);

        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("Downloads");
        sectionPane.setEnableAutoSubtitle(true);

        FilterView<Download> filterView = sectionPane.getFilterView();
        filterView.setItems(DataRepository.getInstance().downloadsProperty());
        filterView.getFilterGroups().setAll(downloadTypeGroup, fileTypeGroup, personGroup, companyGroup);
        filterView.setTextFilterProvider(text -> video -> {
            if (video.getTitle().toLowerCase().contains(text)) {
                return true;
            }
            return false;
        });

        PrettyListView<Download> listView = new PrettyListView<>();
        listView.setCellFactory(view -> new DownloadCell(rootPane, true));
        listView.itemsProperty().bind(filterView.filteredItemsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        setContent(sectionPane);

        selectedItemProperty().addListener(it -> listView.getSelectionModel().select(getSelectedItem()));

        DataRepository.getInstance().videosProperty().addListener((Observable it) -> updateFilters());

        updateFilters();
    }

    private void updateFilters() {
        fileTypeGroup.getFilters().clear();
        downloadTypeGroup.getFilters().clear();
        personGroup.getFilters().clear();
        companyGroup.getFilters().clear();

        updateDownloadTypeGroup();
        updatePersonGroup();
        updateCompanyGroup();
        updateFileTypeGroup();
    }

    private void updatePersonGroup() {
        List<String> personList = new ArrayList<>();

        DataRepository.getInstance().getDownloads().forEach(download -> {
            List<String> personIds = download.getPersonIds();
            if (personIds != null) {
                for (String id : personIds) {
                    if (!personList.contains(id.trim())) {
                        personList.add(id.trim());
                    }
                }
            }
        });

        List<FilterView.Filter<Download>> filters = new ArrayList<>();

        personList.forEach(item -> {
            Optional<Person> personById = DataRepository.getInstance().getPersonById(item);
            if (personById.isPresent()) {
                filters.add(new FilterView.Filter<>(personById.get().getName()) {
                    @Override
                    public boolean test(Download download) {
                        return download.getPersonIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        personGroup.getFilters().setAll(filters);
    }

    private void updateCompanyGroup() {
        List<String> companyList = new ArrayList<>();

        DataRepository.getInstance().getDownloads().forEach(download -> {
            List<String> personIds = download.getPersonIds();
            if (personIds != null) {
                for (String id : personIds) {
                    if (!companyList.contains(id.trim())) {
                        companyList.add(id.trim());
                    }
                }
            }
        });

        List<FilterView.Filter<Download>> filters = new ArrayList<>();

        companyList.forEach(item -> {
            Optional<Company> companyById = DataRepository.getInstance().getCompanyById(item);
            if (companyById.isPresent()) {
                filters.add(new FilterView.Filter<>(companyById.get().getName()) {
                    @Override
                    public boolean test(Download download) {
                        return download.getCompanyIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        companyGroup.getFilters().setAll(filters);
    }

    private void updateFileTypeGroup() {
        for (FileType fileType : FileType.values()) {
            fileTypeGroup.getFilters().add(new FilterView.Filter<>(fileType.toString()) {
                @Override
                public boolean test(Download download) {
                    return download.getFiles().stream().anyMatch(file -> file.getFileType().equals(fileType));
                }
            });
        }
    }

    private void updateDownloadTypeGroup() {
        for (DownloadType downloadType : DownloadType.values()) {
            downloadTypeGroup.getFilters().add(new FilterView.Filter<>(downloadType.toString()) {
                @Override
                public boolean test(Download download) {
                    return download.getDownloadType().equals(downloadType);
                }
            });
        }
    }

    static class DownloadCell extends AdvancedListCell<Download> {

        private final Label titleLabel = new Label();
        private final MarkdownView descriptionMarkdownView = new MarkdownView();
        private final ImageView imageView = new ImageView();
        private final RootPane rootPane;
        private final HBox buttonBox;

        public DownloadCell(RootPane rootPane, boolean insideListView) {
            this.rootPane = rootPane;

            getStyleClass().add("download-cell");

            if (insideListView) {
                setPrefWidth(0);
            }

            titleLabel.getStyleClass().addAll("header3", "title-label");
            titleLabel.setWrapText(true);
            titleLabel.setMinHeight(Region.USE_PREF_SIZE);

            descriptionMarkdownView.getStyleClass().add("description-label");
            VBox.setVgrow(descriptionMarkdownView, Priority.ALWAYS);

            imageView.setFitWidth(300);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);

            StackPane thumbnailWrapper = new StackPane(imageView);
            thumbnailWrapper.getStyleClass().add("thumbnail-wrapper");
            thumbnailWrapper.setMaxHeight(Region.USE_PREF_SIZE);
            StackPane.setAlignment(imageView, Pos.TOP_LEFT);

            buttonBox = new HBox(10);
            buttonBox.setMinHeight(Region.USE_PREF_SIZE);
            buttonBox.setAlignment(Pos.BOTTOM_LEFT);

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            VBox vBox = new VBox(titleLabel, descriptionMarkdownView, spacer, buttonBox);
            vBox.setAlignment(Pos.TOP_LEFT);
            vBox.setFillWidth(true);
            vBox.getStyleClass().add("vbox");

            HBox.setHgrow(vBox, Priority.ALWAYS);

            HBox hBox = new HBox(vBox, thumbnailWrapper);
            hBox.setFillHeight(true);
            hBox.getStyleClass().add("hbox");
            hBox.setAlignment(Pos.TOP_LEFT);

            setGraphic(hBox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            hBox.visibleProperty().bind(itemProperty().isNotNull());
        }

        private void downloadFile(Download.DownloadFile downloadFile) {
            if (StringUtils.isNotBlank(downloadFile.getUrl())) {
                Util.browse(downloadFile.getUrl(), false);
            } else {
                Util.browse(DataRepository.getInstance().getBaseUrl() + "downloads/" + getItem().getId() + "/" + downloadFile.getFileName(), false);
            }
        }

        @Override
        protected void updateItem(Download download, boolean empty) {
            super.updateItem(download, empty);

            buttonBox.getChildren().clear();

            if (!empty && download != null) {
                titleLabel.setText(download.getTitle());
                descriptionMarkdownView.mdStringProperty().bind(DataRepository.getInstance().downloadTextProperty(download));
                imageView.setVisible(true);
                imageView.setManaged(true);
                imageView.imageProperty().bind(ImageManager.getInstance().downloadBannerImageProperty(download));

                download.getFiles().forEach(file -> {
                    Button downloadButton = new Button(file.getName());
                    downloadButton.setGraphic(new FontIcon(MaterialDesign.MDI_DOWNLOAD));
                    downloadButton.setOnAction(evt -> downloadFile(file));
                    buttonBox.getChildren().add(downloadButton);
                });

            } else {
                titleLabel.setText("");
                descriptionMarkdownView.mdStringProperty().unbind();
                imageView.setVisible(false);
                imageView.setManaged(false);
                imageView.imageProperty().unbind();
            }
        }
    }
}
