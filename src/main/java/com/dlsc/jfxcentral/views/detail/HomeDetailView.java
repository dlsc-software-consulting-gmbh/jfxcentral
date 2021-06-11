package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.data.model.News;
import com.dlsc.jfxcentral.data.model.Person;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.detail.cells.DetailNewsCell;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.time.LocalDate;
import java.util.*;

public class HomeDetailView extends DetailViewWithListView<News> {

    private final VBox content = new VBox();
    private final FilterView.FilterGroup<News> typeGroup = new FilterView.FilterGroup<>("Type");
    private final FilterView.FilterGroup<News> personGroup = new FilterView.FilterGroup<>("Person");
    private final FilterView.FilterGroup<News> libraryGroup = new FilterView.FilterGroup<>("Library");
    private final FilterView.FilterGroup<News> timeGroup = new FilterView.FilterGroup<>("Publication Date");

    private final InvalidationListener updateFilterListener = (Observable it) -> updateFilters();
    private final WeakInvalidationListener weakUpdateFilterListener = new WeakInvalidationListener(updateFilterListener);

    public HomeDetailView(RootPane rootPane) {
        super(rootPane);
        getStyleClass().add("home-detail-view");

        createWelcomeSection();
        createNewsSection();
        createRecentItemsSection();
        createContactInfo();

        setContent(content);
    }

    private void createNewsSection() {
        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("News");

        FilterView<News> filterView = sectionPane.getFilterView();
        filterView.setItems(DataRepository.getInstance().newsProperty());
        filterView.getFilterGroups().setAll(typeGroup, personGroup, libraryGroup, timeGroup);
        filterView.setTextFilterProvider(text -> news -> {

            if (StringUtils.containsAnyIgnoreCase(news.getTitle(), text)) {
                return true;
            }

            if (StringUtils.containsAnyIgnoreCase(news.getSubtitle(), text)) {
                return true;
            }

            StringProperty stringProperty = DataRepository.getInstance().newsTextProperty(news);
            if (stringProperty != null && StringUtils.containsAnyIgnoreCase(stringProperty.get(), text)) {
                return true;
            }

            return false;
        });

        AdvancedListView<News> listView = new AdvancedListView<>();
        listView.setVisibleRowCount(3);
        listView.setPaging(true);
        listView.setCellFactory(view -> new DetailNewsCell(getRootPane()));
        listView.itemsProperty().bind(filterView.filteredItemsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        content.getChildren().add(sectionPane);

        DataRepository.getInstance().newsProperty().addListener(weakUpdateFilterListener);

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
        timeGroup.getFilters().add(new FilterView.Filter<>("Last Week") {
            @Override
            public boolean test(News news) {
                return news.getCreatedOn().isAfter(LocalDate.now().minusWeeks(1)) && news.getCreatedOn().isBefore(LocalDate.now())
                        || news.getModifiedOn().isAfter(LocalDate.now().minusWeeks(1)) && news.getModifiedOn().isBefore(LocalDate.now());
            }
        });
        timeGroup.getFilters().add(new FilterView.Filter<>("Last Month") {
            @Override
            public boolean test(News news) {
                return news.getCreatedOn().isAfter(LocalDate.now().minusMonths(1)) && news.getCreatedOn().isBefore(LocalDate.now())
                        || news.getModifiedOn().isAfter(LocalDate.now().minusMonths(1)) && news.getModifiedOn().isBefore(LocalDate.now());
            }
        });
        timeGroup.getFilters().add(new FilterView.Filter<>("Last Year") {
            @Override
            public boolean test(News news) {
                return news.getCreatedOn().isAfter(LocalDate.now().minusYears(1)) && news.getCreatedOn().isBefore(LocalDate.now())
                        || news.getModifiedOn().isAfter(LocalDate.now().minusYears(1)) && news.getModifiedOn().isBefore(LocalDate.now());
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

        List<FilterView.Filter<News>> filters = new ArrayList<>();

        personList.forEach(item -> {
            Optional<Person> personById = DataRepository.getInstance().getPersonById(item);
            if (personById.isPresent()) {
                filters.add(new FilterView.Filter<>(personById.get().getName()) {
                    @Override
                    public boolean test(News news) {
                        return news.getPersonIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
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

        List<FilterView.Filter<News>> filters = new ArrayList<>();

        librariesList.forEach(item -> {
            Optional<Library> libraryById = DataRepository.getInstance().getLibraryById(item);
            if (libraryById.isPresent()) {
                filters.add(new FilterView.Filter<>(libraryById.get().getTitle()) {
                    @Override
                    public boolean test(News news) {
                        return news.getLibraryIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        libraryGroup.getFilters().setAll(filters);
    }

    private void updateTypeGroup() {
        List<News.Type> usedTypesList = new ArrayList<>();
        DataRepository.getInstance().getNews().forEach(news -> {
            News.Type type = news.getType();
            if (!usedTypesList.contains(type)) {
                usedTypesList.add(type);
            }
        });

        Collections.sort(usedTypesList);
        usedTypesList.forEach(type -> typeGroup.getFilters().add(new FilterView.Filter<>(StringUtils.capitalize(type.name().toLowerCase())) {
            @Override
            public boolean test(News news) {
                return Objects.equals(news.getType(), type);
            }
        }));
    }

    private void createWelcomeSection() {
        MarkdownView markdownView = new MarkdownView();
        markdownView.mdStringProperty().bind(DataRepository.getInstance().homeTextProperty());
        HBox.setHgrow(markdownView, Priority.ALWAYS);

        ImageView logo = new ImageView(JFXCentralApp.class.getResource("javafx-logo-text-only.png").toExternalForm());
        logo.setFitWidth(300);
        logo.setFitHeight(60);
        logo.setPreserveRatio(true);

        HBox hBox = new HBox(markdownView);

        SectionPane sectionPane = new SectionPane(hBox);
        sectionPane.setTitle("Welcome");
        sectionPane.setExtras(logo);

        VBox.setVgrow(sectionPane, Priority.NEVER);
        content.getChildren().add(sectionPane);
    }

    private void createRecentItemsSection() {
        AdvancedListView<ModelObject> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(8);
        listView.setCellFactory(view -> new RecentItemCell());
        listView.itemsProperty().bind(DataRepository.getInstance().recentItemsProperty());
        VBox.setVgrow(listView, Priority.ALWAYS);

        SectionPane sectionPane = new SectionPane(listView);
        sectionPane.setTitle("Website Changes");
        sectionPane.setSubtitle("Libraries, news, books, people, etc... that have recently been added or updated");

        content.getChildren().add(sectionPane);
    }

    private void createContactInfo() {
        MarkdownView markdownView = new MarkdownView();
        markdownView.setMdString("Asylweg 28, 8134 Adliswil\n\nSwitzerland\n\nPhone: +41-79-800-23-20");
        markdownView.getStyleClass().add("contact-markdown-view");
        HBox.setHgrow(markdownView, Priority.ALWAYS);

        ImageView imageView = new ImageView(JFXCentralApp.class.getResource("dlsc-logo.png").toExternalForm());
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);
        StackPane.setAlignment(imageView, Pos.TOP_RIGHT);

        StackPane twitter = wrap(new FontIcon(FontAwesomeBrands.TWITTER));
        StackPane linkedIn = wrap(new FontIcon(FontAwesomeBrands.LINKEDIN));
        StackPane github = wrap(new FontIcon(FontAwesomeBrands.GITHUB));
        StackPane youtube = wrap(new FontIcon(FontAwesomeBrands.YOUTUBE));
        StackPane mail = wrap(new FontIcon(Material.MAIL));

        Label twitterLabel = new Label("Twitter");
        Label linkedInLabel = new Label("LinkedIn");
        Label gitHubLabel = new Label("GitHub");
        Label youTubeLabel = new Label("YouTube");
        Label mailLabel = new Label("Mail");

        twitter.getStyleClass().add("social-icon");
        linkedIn.getStyleClass().add("social-icon");
        github.getStyleClass().add("social-icon");
        youtube.getStyleClass().add("social-icon");
        mail.getStyleClass().add("social-icon");

        twitterLabel.getStyleClass().add("social-label");
        linkedInLabel.getStyleClass().add("social-label");
        gitHubLabel.getStyleClass().add("social-label");
        youTubeLabel.getStyleClass().add("social-label");
        mailLabel.getStyleClass().add("social-label");

        GridPane socialPane = new GridPane();
        socialPane.getStyleClass().add("social-pane");

        socialPane.add(twitter, 0, 0);
        socialPane.add(twitterLabel, 1, 0);
        socialPane.add(linkedIn, 0, 1);
        socialPane.add(linkedInLabel, 1, 1);
        socialPane.add(github, 2, 0);
        socialPane.add(gitHubLabel, 3, 0);
        socialPane.add(youtube, 2, 1);
        socialPane.add(youTubeLabel, 3, 1);
        socialPane.add(mail, 4, 0);
        socialPane.add(mailLabel, 5, 0);

        Util.setLink(twitter, "https://twitter.com/dlemmermann", "");
        Util.setLink(linkedIn, "https://www.linkedin.com/in/dlemmermann/", "");
        Util.setLink(github, "https://github.com/dlemmermann", "");
        Util.setLink(youtube, "https://www.youtube.com/channel/UCFyRQ_euxxPDwlqyhff-x0Q", "");
        Util.setLink(mail, "mailto:dlemmermann@gmail.com?subject=JFXCentral", "");

        GridPane.setMargin(twitterLabel, new Insets(0, 10, 0, 0));
        GridPane.setMargin(linkedInLabel, new Insets(0, 10, 0, 0));

        Util.setLink(twitterLabel, "https://twitter.com/dlemmermann", "");
        Util.setLink(linkedInLabel, "https://www.linkedin.com/in/dlemmermann/", "");
        Util.setLink(gitHubLabel, "https://github.com/dlemmermann", "");
        Util.setLink(youTubeLabel, "https://www.youtube.com/channel/UCFyRQ_euxxPDwlqyhff-x0Q", "");
        Util.setLink(mailLabel, "mailto:dlemmermann@gmail.com?subject=JFXCentral", "");

        HBox hBox = new HBox(markdownView, socialPane);
        hBox.getStyleClass().add("footer");

        SectionPane sectionPane = new SectionPane(hBox);
        sectionPane.setTitle("Contact Us");
        sectionPane.setSubtitle("DLSC Software & Consulting GmbH");
        content.getChildren().add(sectionPane);
    }

    private StackPane wrap(FontIcon icon) {
        StackPane stackPane = new StackPane(icon);
        stackPane.getStyleClass().add("icon-wrapper");
        return stackPane;
    }
}