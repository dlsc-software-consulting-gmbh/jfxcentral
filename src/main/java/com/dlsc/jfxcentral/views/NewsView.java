package com.dlsc.jfxcentral.views;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.gemsfx.FilterView.Filter;
import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.MarkdownView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.model.News;
import com.dlsc.jfxcentral.model.News.Type;
import com.dlsc.jfxcentral.model.Person;
import com.dlsc.jfxcentral.panels.PrettyListView;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class NewsView extends PageView {

    private final FilterView.FilterGroup<News> typeGroup = new FilterView.FilterGroup<>("Type");
    private final FilterView.FilterGroup<News> personGroup = new FilterView.FilterGroup<>("Person");
    private final FilterView.FilterGroup<News> libraryGroup = new FilterView.FilterGroup<>("Library");
    private final FilterView.FilterGroup<News> timeGroup = new FilterView.FilterGroup<>("Publication Date");

    public NewsView(RootPane rootPane) {
        super(rootPane);

        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("News");

        FilterView<News> filterView = sectionPane.getFilterView();
        filterView.setItems(DataRepository.getInstance().newsProperty());
        filterView.getFilterGroups().setAll(typeGroup, personGroup, libraryGroup, timeGroup);
        filterView.setTextFilterProvider(text -> news -> {
            if (news.getTitle().toLowerCase().contains(text)) {
                return true;
            }
            if (news.getSubtitle().toLowerCase().contains(text)) {
                return true;
            }
            if (news.getText().toLowerCase().contains(text)) {
                return true;
            }
            return false;
        });


        PrettyListView<News> listView = new PrettyListView<>();
        listView.setCellFactory(view -> new NewsCell(rootPane));
        listView.itemsProperty().bind(filterView.filteredItemsProperty());
//        listView.getSelectionModel().selectedItemProperty().addListener(it -> setVideo(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        setContent(sectionPane);
        DataRepository.getInstance().videosProperty().addListener((Observable it) -> updateFilters());

        updateFilters();

        updateTimeGroup();
    }

    private void updateFilters() {
        typeGroup.getFilters().clear();
        personGroup.getFilters().clear();
        libraryGroup.getFilters().clear();
        timeGroup.getFilters().clear();

        updateTypeGroup();
        updateSpeakersGroup();
        updateLibrariesGroup();
    }

    private void updateTimeGroup() {
        timeGroup.getFilters().add(new Filter<>("Last Week") {
            @Override
            public boolean test(News news) {
                return news.getDate().isAfter(LocalDate.now().minusWeeks(1)) && news.getDate().isBefore(LocalDate.now());
            }
        });
        timeGroup.getFilters().add(new Filter<>("Last Month") {
            @Override
            public boolean test(News news) {
                return news.getDate().isAfter(LocalDate.now().minusMonths(1)) && news.getDate().isBefore(LocalDate.now());
            }
        });
        timeGroup.getFilters().add(new Filter<>("Last Year") {
            @Override
            public boolean test(News news) {
                return news.getDate().isAfter(LocalDate.now().minusYears(1)) && news.getDate().isBefore(LocalDate.now());
            }
        });
    }

    private void updateSpeakersGroup() {
        List<String> personList = new ArrayList<>();

        DataRepository.getInstance().getNews().forEach(news -> {
            List<String> personIds = news.getPersonIds();
            for (String id : personIds) {
                if (!personList.contains(id.trim())) {
                    personList.add(id.trim());
                }
            }
        });

        List<Filter<News>> filters = new ArrayList<>();

        personList.forEach(item -> {
            Optional<Person> personById = DataRepository.getInstance().getPersonById(item);
            if (personById.isPresent()) {
                filters.add(new Filter<>(personById.get().getName()) {
                    @Override
                    public boolean test(News news) {
                        return news.getPersonIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(Filter::getName));
        personGroup.getFilters().setAll(filters);
    }

    private void updateLibrariesGroup() {
        List<String> librariesList = new ArrayList<>();

        DataRepository.getInstance().getNews().forEach(news -> {
            List<String> libraryIds = news.getLibraryIds();
            for (String id : libraryIds) {
                if (!librariesList.contains(id.trim())) {
                    librariesList.add(id.trim());
                }
            }
        });

        List<Filter<News>> filters = new ArrayList<>();

        librariesList.forEach(item -> {
            Optional<Library> libraryById = DataRepository.getInstance().getLibraryById(item);
            if (libraryById.isPresent()) {
                filters.add(new Filter<>(libraryById.get().getTitle()) {
                    @Override
                    public boolean test(News news) {
                        return news.getLibraryIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(Filter::getName));
        libraryGroup.getFilters().setAll(filters);
    }

    private void updateTypeGroup() {
        List<Type> usedTypesList = new ArrayList<>();
        DataRepository.getInstance().getNews().forEach(news -> {
            Type type = news.getType();
            if (!usedTypesList.contains(type)) {
                usedTypesList.add(type);
            }
        });

        Collections.sort(usedTypesList);
        usedTypesList.forEach(type -> typeGroup.getFilters().add(new Filter<>(StringUtils.capitalize(type.name().toLowerCase())) {
            @Override
            public boolean test(News news) {
                return Objects.equals(news.getType(), type);
            }
        }));
    }

    static class NewsCell extends AdvancedListCell<News> {

        private final Label titleLabel = new Label();
        private final Label subtitleLabel = new Label();
        private final Label authorLabel = new Label();
        private final MarkdownView markdownView = new MarkdownView();
        private final ImageView bannerView = new ImageView();
        private final RootPane rootPane;
        private final Button readMoreButton = new Button("Read more ...");
        private final Map<News, BooleanProperty> readMoreMap = new HashMap<>();

        public NewsCell(RootPane rootPane) {
            this.rootPane = rootPane;

            setPrefWidth(0);

            getStyleClass().add("news-cell");

            readMoreButton.getStyleClass().add("read-more-button");

            titleLabel.getStyleClass().addAll("title-label");
            titleLabel.setWrapText(true);
            titleLabel.setMinHeight(Region.USE_PREF_SIZE);
            titleLabel.setMaxWidth(Double.MAX_VALUE);

            subtitleLabel.getStyleClass().addAll("subtitle-label");
            subtitleLabel.setWrapText(true);
            subtitleLabel.setMinHeight(Region.USE_PREF_SIZE);
            subtitleLabel.setMaxWidth(Double.MAX_VALUE);

            authorLabel.getStyleClass().add("author-label");
            authorLabel.setWrapText(true);
            authorLabel.setMinHeight(Region.USE_PREF_SIZE);
            authorLabel.setMaxWidth(Double.MAX_VALUE);

            VBox.setMargin(authorLabel, new Insets(10, 0, 0, 0));
            VBox.setMargin(markdownView, new Insets(20, 0, 0, 0));
            VBox.setMargin(readMoreButton, new Insets(10, 0, 0, 0));

            bannerView.setPreserveRatio(true);
            bannerView.fitWidthProperty().bind(coverImageWidthProperty());

            itemProperty().addListener(it -> {
                News item = getItem();
                if (item != null) {
                    BooleanProperty readMoreProperty = readMoreMap.computeIfAbsent(item, key -> new SimpleBooleanProperty());
                    markdownView.showImagesProperty().bind(readMoreProperty);
                    readMoreButton.textProperty().bind(Bindings.createStringBinding(() -> readMoreProperty.get() ? "Show less ..." : "Read more ...", readMoreProperty));
                    readMoreButton.setOnAction(evt -> readMoreProperty.set(!readMoreProperty.get()));

                    markdownView.setBaseURL(DataRepository.getInstance().getNewsBaseUrl(item));
                    markdownView.mdStringProperty().bind(Bindings.createStringBinding(() -> {
                        String text = DataRepository.getInstance().newsTextProperty(item).get();
                        if (text == null) {
                            text = "";
                        }
                        if (readMoreProperty.get()) {
                            return text;
                        } else {
                            String clipText = text.substring(0, Math.min(600, text.length()));
                            if (clipText.length() < text.length()) {
                                setShowReadMoreLink(true);
                                return clipText + " ...";
                            }
                            setShowReadMoreLink(false);
                            return clipText;
                        }

                    }, readMoreProperty, DataRepository.getInstance().newsTextProperty(item)));

                } else {
                    markdownView.showImagesProperty().unbind();
                    readMoreButton.textProperty().unbind();
                    markdownView.mdStringProperty().unbind();
                }
            });

            readMoreButton.visibleProperty().bind(showReadMoreLinkProperty());
            readMoreButton.managedProperty().bind(showReadMoreLinkProperty());

            StackPane imageWrapper = new StackPane(bannerView);
            imageWrapper.setMaxHeight(Region.USE_PREF_SIZE);
            imageWrapper.getStyleClass().add("banner-image-wrapper");
            StackPane.setAlignment(bannerView, Pos.TOP_LEFT);

            VBox vBox = new VBox(titleLabel, subtitleLabel, authorLabel, markdownView, readMoreButton);
            vBox.setAlignment(Pos.TOP_LEFT);
            vBox.setFillWidth(true);
            vBox.getStyleClass().add("vbox");

            HBox.setHgrow(vBox, Priority.ALWAYS);

            HBox hBox = new HBox(vBox, imageWrapper);
            hBox.getStyleClass().add("hbox");
            hBox.setAlignment(Pos.TOP_LEFT);

            setGraphic(hBox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            hBox.visibleProperty().bind(itemProperty().isNotNull());
        }

        private final BooleanProperty showReadMoreLink = new SimpleBooleanProperty(this, "showReadMoreLink", false);

        public boolean isShowReadMoreLink() {
            return showReadMoreLink.get();
        }

        public BooleanProperty showReadMoreLinkProperty() {
            return showReadMoreLink;
        }

        public void setShowReadMoreLink(boolean showReadMoreLink) {
            this.showReadMoreLink.set(showReadMoreLink);
        }

        private final DoubleProperty coverImageWidth = new SimpleDoubleProperty(this, "coverImageWidth", 300);

        public double getCoverImageWidth() {
            return coverImageWidth.get();
        }

        public DoubleProperty coverImageWidthProperty() {
            return coverImageWidth;
        }

        public void setCoverImageWidth(double coverImageWidth) {
            this.coverImageWidth.set(coverImageWidth);
        }

        @Override
        protected void updateItem(News news, boolean empty) {
            super.updateItem(news, empty);

            if (!empty && news != null) {
                titleLabel.setText(news.getTitle());
                subtitleLabel.setText(news.getSubtitle());
                authorLabel.setText(news.getAuthor() + " - Published on: " + DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(news.getDate()));

                bannerView.setVisible(true);
                bannerView.setManaged(true);
                bannerView.imageProperty().bind(ImageManager.getInstance().newsBannerImageProperty(news));
            }
        }
    }
}