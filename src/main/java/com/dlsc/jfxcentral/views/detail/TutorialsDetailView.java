package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.data.model.Tutorial;
import com.dlsc.jfxcentral.data.model.Tutorial.Format;
import com.dlsc.jfxcentral.panels.PrettyListView;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.page.StandardIcons;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TutorialsDetailView extends DetailViewWithListView<Tutorial> {

    private final FilterView.FilterGroup<Tutorial> formatGroup = new FilterView.FilterGroup<>("Format");
    private final FilterView.FilterGroup<Tutorial> typeGroup = new FilterView.FilterGroup<>("Type");
    private final FilterView.FilterGroup<Tutorial> personGroup = new FilterView.FilterGroup<>("Speaker");

    private final InvalidationListener updateFilterListener = (Observable it) -> updateFilters();
    private final WeakInvalidationListener weakUpdateFilterListener = new WeakInvalidationListener(updateFilterListener);

    public TutorialsDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("tutorials-detail-view");

        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("Tutorials");
        sectionPane.setEnableAutoSubtitle(true);

        FilterView<Tutorial> filterView = sectionPane.getFilterView();
        filterView.setItems(DataRepository.getInstance().tutorialsProperty());
        filterView.getFilterGroups().setAll(formatGroup, typeGroup, personGroup);
        filterView.setTextFilterProvider(text -> tutorial -> {
            if (StringUtils.containsAnyIgnoreCase(tutorial.getName(), text)) {
                return true;
            }

            if (StringUtils.containsAnyIgnoreCase(tutorial.getSummary(), text)) {
                return true;
            }

            if (StringUtils.containsAnyIgnoreCase(DataRepository.getInstance().tutorialTextProperty(tutorial).get(), text)) {
                return true;
            }

            return false;
        });

        PrettyListView<Tutorial> listView = new PrettyListView<>();
        listView.setCellFactory(view -> new TutorialCell(true));
        listView.itemsProperty().bind(filterView.filteredItemsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        setContent(sectionPane);

        DataRepository.getInstance().tutorialsProperty().addListener(weakUpdateFilterListener);
        updateFilters();
    }

    private void updateFilters() {
        typeGroup.getFilters().clear();
        formatGroup.getFilters().clear();
        personGroup.getFilters().clear();

        updateFormatGroup();
        updatePersonGroup();
        updateTypeGroup();
    }

    private void updatePersonGroup() {
        List<String> speakersList = new ArrayList<>();

        DataRepository.getInstance().getTutorials().forEach(tutorial -> {
            List<String> personIds = tutorial.getPersonIds();
            if (personIds != null) {
                for (String id : personIds) {
                    if (!speakersList.contains(id.trim())) {
                        speakersList.add(id.trim());
                    }
                }
            }
        });

        List<FilterView.Filter<Tutorial>> filters = new ArrayList<>();

        speakersList.forEach(item -> {
            Optional<Person> personById = DataRepository.getInstance().getPersonById(item);
            if (personById.isPresent()) {
                filters.add(new FilterView.Filter<>(personById.get().getName()) {
                    @Override
                    public boolean test(Tutorial tutorial) {
                        return tutorial.getPersonIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        personGroup.getFilters().setAll(filters);
    }

    private void updateFormatGroup() {
        for (Format type : Format.values()) {
            formatGroup.getFilters().add(new FilterView.Filter<>(type.name()) {
                @Override
                public boolean test(Tutorial tutorial) {
                    return tutorial.getType().equals(type);
                }
            });
        }
    }

    private void updateTypeGroup() {
        typeGroup.getFilters().add(new FilterView.Filter<>("Free") {
            @Override
            public boolean test(Tutorial tutorial) {
                return !tutorial.isCommercial();
            }
        });
        typeGroup.getFilters().add(new FilterView.Filter<>("Paid Course") {
            @Override
            public boolean test(Tutorial tutorial) {
                return tutorial.isCommercial();
            }
        });
    }

    class TutorialCell extends AdvancedListCell<Tutorial> {

        private final Label titleLabel = new Label();
        private final MarkdownView descriptionMarkdownView = new MarkdownView();
        private final ImageView thumbnailView = new ImageView();
        private final Button visitButton = new Button("Visit Tutorial");
        private final Label commercialLabel = new Label("$$$");

        public TutorialCell(boolean insideListView) {
            getStyleClass().add("tutorial-cell");

            if (insideListView) {
                setPrefWidth(0);
            }

            commercialLabel.getStyleClass().add("commercial-label");

            visitButton.setGraphic(new FontIcon(StandardIcons.TUTORIAL));

            titleLabel.getStyleClass().addAll("header3", "title-label");
            titleLabel.setWrapText(true);
            titleLabel.setMinHeight(Region.USE_PREF_SIZE);
            titleLabel.setGraphic(commercialLabel);

            descriptionMarkdownView.getStyleClass().add("description-label");

            thumbnailView.setFitWidth(300);
            thumbnailView.setPreserveRatio(true);

            StackPane thumbnailWrapper = new StackPane(thumbnailView);
            thumbnailWrapper.getStyleClass().add("thumbnail-wrapper");
            thumbnailWrapper.setMaxHeight(Region.USE_PREF_SIZE);
            StackPane.setAlignment(thumbnailView, Pos.TOP_LEFT);

            HBox buttonBox = new HBox(10, visitButton);
            buttonBox.setMinHeight(Region.USE_PREF_SIZE);
            buttonBox.setAlignment(Pos.BOTTOM_LEFT);

            VBox vBox = new VBox(titleLabel, descriptionMarkdownView, buttonBox);
            vBox.setAlignment(Pos.TOP_LEFT);
            vBox.setFillWidth(true);
            vBox.getStyleClass().add("vbox");

            HBox.setHgrow(vBox, Priority.ALWAYS);

            HBox hBox = new HBox(vBox, thumbnailWrapper);
            hBox.getStyleClass().add("hbox");
            hBox.setAlignment(Pos.TOP_LEFT);

            setGraphic(hBox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            hBox.visibleProperty().bind(itemProperty().isNotNull());

            setOnMouseClicked(evt -> {
                if (evt.getClickCount() == 2) {
                    showLargeImage(getItem());
                }
            });
        }

        private void showLargeImage(Tutorial tutorial) {
            ImageView largeImageView = new ImageView();
            largeImageView.setFitWidth(800);
            largeImageView.setPreserveRatio(true);
            largeImageView.imageProperty().bind(ImageManager.getInstance().tutorialImageLargeProperty(tutorial));
            getRootPane().getDialogPane().showNode(DialogPane.Type.BLANK, "Title", largeImageView);
        }

        @Override
        protected void updateItem(Tutorial tutorial, boolean empty) {
            super.updateItem(tutorial, empty);

            if (!empty && tutorial != null) {
                titleLabel.setText(tutorial.getName());

                commercialLabel.setVisible(tutorial.isCommercial());

                descriptionMarkdownView.setBaseURL(DataRepository.BASE_URL + "tutorials/" + tutorial.getId());
                descriptionMarkdownView.mdStringProperty().bind(DataRepository.getInstance().tutorialTextProperty(tutorial));

                thumbnailView.setVisible(true);
                thumbnailView.setManaged(true);
                thumbnailView.imageProperty().bind(ImageManager.getInstance().tutorialImageProperty(tutorial));

                Util.setLink(visitButton, tutorial.getUrl(), tutorial.getName());
            }
        }
    }
}