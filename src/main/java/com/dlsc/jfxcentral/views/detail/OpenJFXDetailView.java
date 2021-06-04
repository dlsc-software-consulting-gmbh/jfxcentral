package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.pull.PullRequest;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.views.*;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class OpenJFXDetailView extends DetailView {

    private final FilterView.FilterGroup<PullRequest> stateGroup = new FilterView.FilterGroup<>("State");
    private final FilterView.FilterGroup<PullRequest> labelGroup = new FilterView.FilterGroup<>("Label");
    private final FilterView.FilterGroup<PullRequest> userGroup = new FilterView.FilterGroup<>("User");
    private final FilterView.FilterGroup<PullRequest> timeGroup = new FilterView.FilterGroup<>("Time");

    private VBox content = new VBox();

    public OpenJFXDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("openjfx-detail-view");

        createHeader();
        createPullRequests();

        VBox.setVgrow(content, Priority.ALWAYS);
        setContent(content);
    }

    private void createHeader() {
        SectionPane sectionPane = new SectionPane();

        MarkdownView markdownView = new MarkdownView();
        markdownView.mdStringProperty().bind(DataRepository.getInstance().openJFXTextProperty());
        sectionPane.getNodes().add(markdownView);

        content.getChildren().add(sectionPane);
    }

    private void createPullRequests() {
        SectionPaneWithFilterView<PullRequest> sectionPane = new SectionPaneWithFilterView();
        sectionPane.setTitle("Pull Requests");

        FilterView<PullRequest> filterView = sectionPane.getFilterView();
        filterView.setItems(DataRepository.getInstance().getPullRequests());
        filterView.getFilterGroups().setAll(stateGroup, labelGroup, userGroup, timeGroup);

        filterView.setTextFilterProvider(text -> pullRequest -> {
            if (pullRequest.getTitle().toLowerCase().contains(text)) {
                return true;
            }

            if (pullRequest.getBody().toLowerCase().contains(text)) {
                return true;
            }

            if (StringUtils.containsIgnoreCase(pullRequest.getUser().getLogin(), text)) {
                return true;
            }

            return false;
        });

        AdvancedListView<PullRequest> listView = new AdvancedListView<>();
        listView.setPaging(true);
        listView.setVisibleRowCount(8);
        listView.setCellFactory(view -> new PullRequestListCell());
        listView.setItems(filterView.getFilteredItems());
        sectionPane.getNodes().add(listView);

        VBox.setVgrow(listView, Priority.ALWAYS);
        VBox.setVgrow(sectionPane, Priority.ALWAYS);

        content.getChildren().add(sectionPane);
    }

    private class PullRequestListCell extends AdvancedListCell<PullRequest> {

        private Label titleLabel = new Label();
        private Label summaryLabel = new Label();
        private Label statusLabel = new Label();
        private HBox labelBox = new HBox();
        private PhotoView photoView = new PhotoView();

        public PullRequestListCell() {
            getStyleClass().add("pull-request-list-cell");

            setPrefWidth(0);

            photoView.setEditable(false);
            photoView.setPlaceholder(new Label(""));
            photoView.visibleProperty().bind(photoView.photoProperty().isNotNull());
            photoView.managedProperty().bind(photoView.photoProperty().isNotNull());

            titleLabel.getStyleClass().add("title-label");
            titleLabel.setGraphic(labelBox);
            titleLabel.setContentDisplay(ContentDisplay.RIGHT);

            summaryLabel.getStyleClass().add("summary-label");
            statusLabel.getStyleClass().add("status-label");
            labelBox.getStyleClass().add("label-box");

            VBox vBox = new VBox(titleLabel, summaryLabel);
            vBox.getStyleClass().add("vbox");
            HBox.setHgrow(vBox, Priority.ALWAYS);

            HBox hBox = new HBox(photoView, vBox, statusLabel);
            hBox.getStyleClass().add("hbox");

            setGraphic(hBox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            setOnMouseClicked(evt -> {
                if (evt.getButton() == MouseButton.PRIMARY && evt.getClickCount() == 2) {
                    PullRequest item = getItem();
                    if (item != null) {
                        showPullRequestBody(getItem());
                    }
                }
            });

            visibleProperty().bind(itemProperty().isNotNull());
            managedProperty().bind(itemProperty().isNotNull());
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


            getRootPane().getDialogPane().showNode(DialogPane.Type.INFORMATION, "#" + pr.getNumber() + " - " + pr.getTitle(), scrollPane, false);
        }

        @Override
        protected void updateItem(PullRequest pr, boolean empty) {
            super.updateItem(pr, empty);

            labelBox.getChildren().clear();

            if (!empty && pr != null) {
                setText(pr.getTitle());
                titleLabel.setText(pr.getTitle());
                statusLabel.getStyleClass().removeAll("open", "closed");
                photoView.photoProperty().bind(ImageManager.getInstance().githubAvatarImageProperty(pr));
                switch (pr.getState()) {
                    case "open":
                        statusLabel.setText("Open");
                        statusLabel.setVisible(false);
                        summaryLabel.setText("#" + pr.getNumber() + " opened by " + pr.getUser().getLogin() + " " + createTimeString(pr.getCreatedAt()));
                        statusLabel.getStyleClass().add("open");
                        break;
                    case "closed":
                        statusLabel.setText("Closed");
                        statusLabel.setVisible(true);
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

            return "on " + DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(time) + ".";
        }
    }
}
