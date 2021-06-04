package com.dlsc.jfxcentral.views.detail;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.FilterView;
import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.pull.PullRequest;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.panels.SectionPaneWithFilterView;
import com.dlsc.jfxcentral.util.FilterUtil;
import com.dlsc.jfxcentral.views.*;
import javafx.beans.Observable;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        DataRepository.getInstance().videosProperty().addListener((Observable it) -> updateFilters());

        updateFilters();
    }

    private void updateFilters() {
        stateGroup.getFilters().clear();
        labelGroup.getFilters().clear();
        userGroup.getFilters().clear();
        timeGroup.getFilters().clear();

        updateStateGroup();
        updateLabelGroup();
        updateUserGroup();

        timeGroup.getFilters().clear();

        FilterUtil.createFilters(timeGroup, "Date", pr -> DateTimeFormatter.ISO_DATE_TIME.parse(pr.getUpdatedAt() != null ? pr.getUpdatedAt() : pr.getCreatedAt(), ZonedDateTime::from));
    }

    private void updateUserGroup() {
        List<String> userList = new ArrayList<>();

        DataRepository.getInstance().getPullRequests().forEach(pr -> {
            String id = pr.getUser().getLogin();
            if (!userList.contains(id.trim())) {
                userList.add(id.trim());
            }
        });

        List<FilterView.Filter<PullRequest>> filters = new ArrayList<>();

        userList.forEach(item -> filters.add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(PullRequest pr) {
                return pr.getUser().getLogin().equals(item);
            }
        }));

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        userGroup.getFilters().setAll(filters);
    }

    private void updateStateGroup() {
        List<String> stateList = new ArrayList<>();
        stateList.add("open");
        stateList.add("closed");

        List<FilterView.Filter<PullRequest>> filters = new ArrayList<>();

        stateList.forEach(item -> filters.add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(PullRequest pr) {
                return pr.getState().equals(item);
            }
        }));

        stateGroup.getFilters().setAll(filters);
    }

    private void updateLabelGroup() {
        List<String> labels = new ArrayList<>();

        DataRepository.getInstance().getPullRequests().forEach(pr -> {
            pr.getLabels().forEach(label -> {
                if (!labels.contains(label.getName())) {
                    labels.add(label.getName());
                }
            });
        });

        List<FilterView.Filter<PullRequest>> filters = new ArrayList<>();

        labels.forEach(item -> filters.add(new FilterView.Filter<>(item) {
            @Override
            public boolean test(PullRequest pr) {
                return pr.getLabels().stream().anyMatch(label -> label.getName().equals(item));
            }
        }));

        filters.sort(Comparator.comparing(FilterView.Filter::getName));
        labelGroup.getFilters().setAll(filters);
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
                photoView.photoProperty().bind(ImageManager.getInstance().githubAvatarImageProperty(pr.getUser().getLogin()));

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
