package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.pull.PullRequest;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.PhotoView;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DetailPullRequestCell extends DetailCell<PullRequest> {

    private final RootPane rootPane;
    private final Label titleLabel = new Label();
    private final Label summaryLabel = new Label();
    private final Label statusLabel = new Label();
    private final HBox labelBox = new HBox();
    private final PhotoView photoView = new PhotoView();
    private final DateTimeFormatter dateTimeFormatter;

    public DetailPullRequestCell(RootPane rootPane) {
        this.rootPane = rootPane;

        dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(rootPane.getLocale());

        getStyleClass().add("detail-pull-request-cell");

        setPrefWidth(0);

        photoView.setEditable(false);
        photoView.setPlaceholder(new Label(""));
        photoView.visibleProperty().bind(photoView.photoProperty().isNotNull());
        photoView.managedProperty().bind(photoView.photoProperty().isNotNull());

        titleLabel.getStyleClass().add("title-label");
        titleLabel.setContentDisplay(ContentDisplay.RIGHT);

        summaryLabel.getStyleClass().add("summary-label");
        statusLabel.getStyleClass().add("status-label");
        labelBox.getStyleClass().add("label-box");

        VBox vBox = new VBox();

        if (rootPane.isMobile()) {
            titleLabel.setWrapText(true);
            titleLabel.setMinHeight(Region.USE_PREF_SIZE);
            summaryLabel.setWrapText(true);
            summaryLabel.setMinHeight(Region.USE_PREF_SIZE);
            vBox.getChildren().setAll(statusLabel, titleLabel, summaryLabel, labelBox);
        } else {
            titleLabel.setGraphic(labelBox);
            vBox.getChildren().setAll(titleLabel, summaryLabel);
        }

        vBox.getStyleClass().add("vbox");
        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.getStyleClass().add("hbox");

        if (rootPane.isMobile()) {
            hBox.getChildren().setAll(photoView, vBox);
            hBox.setAlignment(Pos.TOP_LEFT);
        } else {
            hBox.getChildren().setAll(photoView, vBox, statusLabel);
            hBox.setAlignment(Pos.CENTER_LEFT);
        }

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        setOnMouseClicked(evt -> {
            if (evt.getButton() == MouseButton.PRIMARY && evt.isStillSincePress() && (evt.getClickCount() == 2 || rootPane.isMobile())) {
                PullRequest item = getItem();
                if (item != null) {
                    showPullRequestBody(getItem());
                }
            }
        });

      //  visibleProperty().bind(itemProperty().isNotNull());
      //  managedProperty().bind(itemProperty().isNotNull());
    }

    private void showPullRequestBody(PullRequest pr) {
        MarkdownView markdownView = new MarkdownView();
        String body = pr.getBody();
        int index = body.indexOf("<!-- ");
        body = body.substring(0, index);

        markdownView.setMdString(body);
        markdownView.setPrefWidth(0);

        ScrollPane scrollPane = new ScrollPane(markdownView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("transparent");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Label titleLabel = new Label(pr.getTitle());
        titleLabel.getStyleClass().add("title-label");

        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        Button showOnGitHub = new Button("Show on GitHub");
        Util.setLink(showOnGitHub, pr.getHtmlUrl(), "GitHub");

        HBox buttonBar = new HBox(showOnGitHub);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.getStyleClass().add("button-bar");

        VBox vBox = new VBox(titleLabel, scrollPane, buttonBar);
        vBox.getStyleClass().add("content-wrapper");

        rootPane.getOverlayPane().setContent(vBox);
    }

    @Override
    protected void updateItem(PullRequest pr, boolean empty) {
        super.updateItem(pr, empty);

        labelBox.getChildren().clear();

        if (!empty && pr != null) {
            setText(pr.getTitle());
            titleLabel.setText(pr.getTitle());
            statusLabel.getStyleClass().removeAll("open", "closed");
            photoView.photoProperty().bind(ImageManager.getInstance().githubAvatarImageProperty(pr.getUser().getLogin()));

            switch (pr.getState()) {
                case "open":
                    statusLabel.setText("Open");
                    statusLabel.setVisible(false);
                    statusLabel.setManaged(false);
                    summaryLabel.setText("#" + pr.getNumber() + " opened by " + pr.getUser().getLogin() + " " + createTimeString(pr.getCreatedAt()));
                    statusLabel.getStyleClass().add("open");
                    break;
                case "closed":
                    statusLabel.setText("Closed");
                    statusLabel.setVisible(true);
                    statusLabel.setManaged(true);
                    summaryLabel.setText("#" + pr.getNumber() + " closed by " + pr.getUser().getLogin() + " " + createTimeString(pr.getUpdatedAt()));
                    statusLabel.getStyleClass().add("closed");
                    break;
            }

            pr.getLabels().forEach(githubLabel -> {
                Label label = new Label(githubLabel.getName());
                label.getStyleClass().add("pull-request-label");

                // Color comes in as "aa8954".
                // We need to add the hash symbol in front of it.
                // Opacity can be controlled by adding a fourth byte.
                label.setStyle("-fx-background-color: #" + githubLabel.getColor() + "33; -fx-border-color: #" + githubLabel.getColor() + "cc;");

                labelBox.getChildren().add(label);
            });
        } else {
            photoView.photoProperty().unbind();
            titleLabel.setText("");
            summaryLabel.setText("");
            statusLabel.setText("");
            statusLabel.setVisible(false);
        }
    }

    private String createTimeString(String timeString) {
        ZonedDateTime time = DateTimeFormatter.ISO_DATE_TIME.parse(timeString, ZonedDateTime::from);

        Duration between = Duration.between(time, ZonedDateTime.now());

        if (between.toDays() < 1) {
            return between.toHours() + " hours ago.";
        } else if (between.toDays() == 1) {
            return "one day ago.";
        } else if (between.toDays() < 14) {
            return between.toDays() + " days ago.";
        }

        return "on " + dateTimeFormatter.format(time) + ".";
    }
}
