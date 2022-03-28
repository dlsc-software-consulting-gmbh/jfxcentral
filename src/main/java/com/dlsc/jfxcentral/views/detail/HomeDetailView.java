package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.JFXCentralApp;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.*;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.Detector;
import com.dlsc.jfxcentral.util.EmptySelectionModel;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.detail.cells.DetailLinksOfTheWeekCell;
import com.dlsc.jfxcentral.views.detail.cells.DetailNewsCell;
import com.dlsc.jfxcentral.views.detail.cells.DetailRecentItemCell;
import com.jpro.webapi.HTMLView;
import com.jpro.webapi.WebAPI;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.time.LocalDate;
import java.util.*;

public class HomeDetailView extends DetailViewWithListView<News> {

    private final VBox content = new VBox(20);

    private final FilterView.FilterGroup<News> typeGroup = new FilterView.FilterGroup<>("Type");
    private final FilterView.FilterGroup<News> personGroup = new FilterView.FilterGroup<>("Person");
    private final FilterView.FilterGroup<News> libraryGroup = new FilterView.FilterGroup<>("Library");
    private final FilterView.FilterGroup<News> timeGroup = new FilterView.FilterGroup<>("Publication Date");

    public HomeDetailView(RootPane rootPane) {
        super(rootPane, View.HOME);

        getStyleClass().add("home-detail-view");

        createWelcomeSection();
        createLinksOfTheWeekSection();
        //createNewsSection();
        createRecentItemsSection();
        createContactInfo();

        if (rootPane.isMobile()) {
            // no Twitter feed on mobile
            setContent(content);
        } else {
            SectionPane webviewSectionPane = new SectionPane();

            Node node;

            if (WebAPI.isBrowser()) {
                HTMLView htmlView = new HTMLView();
                htmlView.setPrefWidth(300);
                htmlView.setMinWidth(300);
                htmlView.setMaxWidth(300);

                sceneProperty().addListener(it -> {
                    Scene scene = getScene();
                    if (scene != null) {
                        WebAPI.getWebAPI(scene).darkMode().addListener(it2 -> htmlView.setContent(getTwitterContent()));
                        htmlView.setContent(getTwitterContent());
                    }
                });

                node = htmlView;

            } else {
                WebView webView = new WebView();
                webView.setPrefWidth(300);
                webView.setMinWidth(300);
                webView.setMaxWidth(300);
                webView.getEngine().loadContent(getTwitterContent());
                VBox.setVgrow(webView, Priority.ALWAYS);
                JFXCentralApp.darkModeProperty().addListener(it -> {
                    webView.getEngine().loadContent("");
                    webView.getEngine().loadContent(getTwitterContent());
                });
                node = webView;
            }

            webviewSectionPane.getNodes().add(node);

            HBox hBox = new HBox(10, content, webviewSectionPane);

            setContent(hBox);
        }
    }

    private String getTwitterContent() {
        if (WebAPI.isBrowser()) {
            if (WebAPI.getWebAPI(getScene()).isDarkMode()) {
                return "<a class=\"twitter-timeline\" data-theme=\"dark\" data-chrome=\"transparent noscrollbar\" href=\"https://twitter.com/jfxcentral?ref_src=twsrc%5Etfw\">Tweets by JFX-Central</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
            }
        } else if (Detector.isDarkMode()) {
            // runs on desktop, use the detector
            return "<a class=\"twitter-timeline\" data-theme=\"dark\" data-chrome=\"transparent noscrollbar\" href=\"https://twitter.com/jfxcentral?ref_src=twsrc%5Etfw\">Tweets by JFX-Central</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        }

        return "<a class=\"twitter-timeline\" data-chrome=\"transparent noscrollbar\" href=\"https://twitter.com/jfxcentral?ref_src=twsrc%5Etfw\">Tweets by JFX-Central</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
    }

    private void createNewsSection() {
        SectionPaneWithFilterView sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("News");

        FilterView<News> filterView = sectionPane.getFilterView();
        Bindings.bindContent(filterView.getItems(), DataRepository.getInstance().getNews());

        if (!getRootPane().isMobile()) {
            filterView.getFilterGroups().setAll(typeGroup, personGroup, libraryGroup, timeGroup);
        }

        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setVisibleRowCount(3);
        listView.setPaging(true);
        listView.setCellFactory(view -> new DetailNewsCell(getRootPane(), true));
        listView.itemsProperty().bind(filterView.filteredItemsProperty());
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        content.getChildren().add(sectionPane);

        filterView.itemsProperty().addListener((Observable it) -> updateFilters());

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

        filters.sort(Comparator.comparing(x -> x.getName().toLowerCase()));
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
                filters.add(new FilterView.Filter<>(libraryById.get().getName()) {
                    @Override
                    public boolean test(News news) {
                        return news.getLibraryIds().contains(item);
                    }
                });
            }
        });

        filters.sort(Comparator.comparing(x -> x.getName().toLowerCase()));
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

        ImageView logo = new ImageView(JFXCentralApp.class.getResource("javafx-logo.png").toExternalForm());
        logo.setFitWidth(300);
        logo.setFitHeight(60);
        logo.setPreserveRatio(true);

        HBox hBox = new HBox(markdownView);

        SectionPane sectionPane = new SectionPane(hBox);
        sectionPane.setTitle("About JFX-Central");
        sectionPane.setSubtitle("Home to anything JavaFX related.");
        sectionPane.setExtras(logo);

        VBox.setVgrow(sectionPane, Priority.NEVER);
        content.getChildren().add(sectionPane);
    }

    private void createLinksOfTheWeekSection() {
        SortedList<LinksOfTheWeek> sortedList = new SortedList<>(DataRepository.getInstance().getLinksOfTheWeek());
        sortedList.setComparator(Comparator.comparing(ModelObject::getCreationOrUpdateDate).reversed());

        AdvancedListView<LinksOfTheWeek> listView = new AdvancedListView<>();
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setPaging(true);
        listView.setVisibleRowCount(3);
        listView.setCellFactory(view -> new DetailLinksOfTheWeekCell(getRootPane()));
        Bindings.bindContent(listView.getItems(), sortedList);

        VBox.setVgrow(listView, Priority.ALWAYS);

        SectionPane sectionPane = new SectionPane(listView);
        sectionPane.setTitle("Links of the Week");
        sectionPane.setSubtitle("Miscellaneous JavaFX stuff found on the web");

        content.getChildren().add(sectionPane);
    }

    private void createRecentItemsSection() {
        AdvancedListView<ModelObject> listView = new AdvancedListView<>();
        listView.getListView().setSelectionModel(new EmptySelectionModel<>());
        listView.setPaging(true);
        listView.setVisibleRowCount(8);
        listView.setCellFactory(view -> new DetailRecentItemCell(getRootPane()));

        Bindings.bindContent(listView.getItems(), DataRepository.getInstance().getRecentItems());
        VBox.setVgrow(listView, Priority.ALWAYS);

        SectionPane sectionPane = new SectionPane(listView);
        sectionPane.setTitle("Website Changes");
        sectionPane.setSubtitle("Libraries, news, books, people, etc... that have recently been added or updated");

        content.getChildren().add(sectionPane);
    }

    private void createContactInfo() {
        MarkdownView markdownView = new MarkdownView();
        markdownView.setMdString("**DLSC Software & Consulting**\n\nAsylweg 28, 8134 Adliswil\n\nSwitzerland\n\nPhone: +41-79-800-23-20\n\nhttps://www.dlsc.com");
        markdownView.getStyleClass().add("contact-markdown-view");
        markdownView.setCursor(Cursor.HAND);
        HBox.setHgrow(markdownView, Priority.ALWAYS);

        ImageView imageView = new ImageView(JFXCentralApp.class.getResource("dlsc-logo-text.png").toExternalForm());
        imageView.setFitHeight(30);
        imageView.setPreserveRatio(true);
        StackPane.setAlignment(imageView, Pos.TOP_RIGHT);

        StackPane imageViewWrapper = new StackPane(imageView);
        imageViewWrapper.setCursor(Cursor.HAND);

        Util.setLink(imageViewWrapper, "https://www.dlsc.com", "DLSC");
        Util.setLink(markdownView, "https://www.dlsc.com", "DLSC");

        SectionPane sectionPane = new SectionPane(markdownView, createSocialInfo());
        sectionPane.setTitle("Contact");
        sectionPane.setExtras(imageViewWrapper);

        content.getChildren().add(sectionPane);
    }

    private FlowPane createSocialInfo() {
        StackPane twitter = wrap(new FontIcon(FontAwesomeBrands.TWITTER));
        StackPane linkedIn = wrap(new FontIcon(FontAwesomeBrands.LINKEDIN));
        StackPane github = wrap(new FontIcon(FontAwesomeBrands.GITHUB));
        StackPane youtube = wrap(new FontIcon(FontAwesomeBrands.YOUTUBE));
        StackPane mail = wrap(new FontIcon(Material.MAIL));

        twitter.getStyleClass().add("social-icon");
        linkedIn.getStyleClass().add("social-icon");
        github.getStyleClass().add("social-icon");
        youtube.getStyleClass().add("social-icon");
        mail.getStyleClass().add("social-icon");

        FlowPane flowPane = new FlowPane();
        flowPane.getStyleClass().add("social-pane");

        flowPane.getChildren().add(twitter);
        flowPane.getChildren().add(linkedIn);
        flowPane.getChildren().add(github);
        flowPane.getChildren().add(youtube);
        flowPane.getChildren().add(mail);

        Util.setLink(twitter, "https://twitter.com/dlemmermann", "Twitter");
        Util.setLink(linkedIn, "https://www.linkedin.com/in/dlemmermann/", "LinkedIn");
        Util.setLink(github, "https://github.com/dlemmermann", "GitHub");
        Util.setLink(youtube, "https://www.youtube.com/channel/UCFyRQ_euxxPDwlqyhff-x0Q", "YouTube");
        Util.setLink(mail, "mailto:dlemmermann@gmail.com?subject=JFXCentral", "Mail");

        return flowPane;
    }

    private StackPane wrap(FontIcon icon) {
        StackPane stackPane = new StackPane(icon);
        stackPane.getStyleClass().add("icon-wrapper");
        return stackPane;
    }
}