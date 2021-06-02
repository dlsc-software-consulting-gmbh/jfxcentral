package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.*;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.ModelObject;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListView;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

public class HomeDetailsView extends DetailView {

    private final VBox content = new VBox();

    public HomeDetailsView(RootPane rootPane) {
        super(rootPane);
        getStyleClass().add("home-view");

        createWelcomeSection();
        createRecentItemsSection();
        createContactInfo();

        setContent(content);
    }

    private void createWelcomeSection() {
        MarkdownView markdownView = new MarkdownView();
        markdownView.mdStringProperty().bind(DataRepository.getInstance().homeTextProperty());
        HBox.setHgrow(markdownView, Priority.ALWAYS);

        ImageView imageView = new ImageView(JFXCentralApp.class.getResource("duke.png").toExternalForm());
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        HBox hBox = new HBox(markdownView, imageView);

        SectionPane sectionPane = new SectionPane(hBox);
        sectionPane.setTitle("Welcome");

        VBox.setVgrow(sectionPane, Priority.NEVER);
        content.getChildren().add(sectionPane);
    }

    private void createRecentItemsSection() {
        AdvancedListView<ModelObject> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.visibleRowCountProperty().bind(DataRepository.getInstance().recentItemsProperty().sizeProperty());
        listView.setCellFactory(view -> new RecentItemCell());
        listView.itemsProperty().bind(DataRepository.getInstance().recentItemsProperty());
        VBox.setVgrow(listView, Priority.ALWAYS);

        SectionPane sectionPane = new SectionPane(listView);
        sectionPane.setTitle("Latest Additions & Updates");
        sectionPane.setSubtitle("Libraries, news, books, people, etc... that have recently been added or updated");
        VBox.setVgrow(sectionPane, Priority.ALWAYS);

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

        twitter.setOnMouseClicked(evt -> Util.browse("https://twitter.com/dlemmermann"));
        linkedIn.setOnMouseClicked(evt -> Util.browse("https://www.linkedin.com/in/dlemmermann/"));
        github.setOnMouseClicked(evt -> Util.browse("https://github.com/dlemmermann"));
        youtube.setOnMouseClicked(evt -> Util.browse("https://www.youtube.com/channel/UCFyRQ_euxxPDwlqyhff-x0Q"));
        mail.setOnMouseClicked(evt -> Util.browse("mailto:dlemmermann@gmail.com?subject=JFXCentral"));

        GridPane.setMargin(twitterLabel, new Insets(0, 10, 0, 0));
        GridPane.setMargin(linkedInLabel, new Insets(0, 10, 0, 0));

        twitterLabel.setOnMouseClicked(evt -> Util.browse("https://twitter.com/dlemmermann"));
        linkedInLabel.setOnMouseClicked(evt -> Util.browse("https://www.linkedin.com/in/dlemmermann/"));
        gitHubLabel.setOnMouseClicked(evt -> Util.browse("https://github.com/dlemmermann"));
        youTubeLabel.setOnMouseClicked(evt -> Util.browse("https://www.youtube.com/channel/UCFyRQ_euxxPDwlqyhff-x0Q"));
        mailLabel.setOnMouseClicked(evt -> Util.browse("mailto:dlemmermann@gmail.com?subject=JFXCentral"));

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