package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.panels.LicenseLabel;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.detail.ThumbnailScrollPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailLibraryCell extends AdvancedListCell<Library> {

    private final Button homepageButton;
    private final Button repositoryButton;
    private final Button issueTrackerButton;
    private final Button discussionsButton;
    private final ThumbnailScrollPane infoView;

    private Label titleLabel = new Label();
    private Label summaryLabel = new Label();

    private ImageView logoImageView = new ImageView();
    private HBox buttonBox = new HBox();

    private LicenseLabel licenseLabel = new LicenseLabel();

    public DetailLibraryCell(RootPane rootPane) {
        getStyleClass().add("detail-library-cell");

        titleLabel.getStyleClass().addAll("header3", "title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
        titleLabel.setAlignment(Pos.TOP_LEFT);
        titleLabel.setGraphic(licenseLabel);
        titleLabel.setContentDisplay(ContentDisplay.RIGHT);

        buttonBox.getStyleClass().add("button-box");

        summaryLabel.getStyleClass().add("summary-label");
        summaryLabel.setWrapText(true);
        summaryLabel.setMinHeight(Region.USE_PREF_SIZE);
        summaryLabel.setAlignment(Pos.TOP_LEFT);

        homepageButton = new Button("Homepage");
        homepageButton.getStyleClass().addAll("library-button", "homepage");
        homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));
        buttonBox.getChildren().add(homepageButton);

        repositoryButton = new Button("Repository");
        repositoryButton.getStyleClass().addAll("library-button", "repository");
        repositoryButton.setGraphic(new FontIcon(MaterialDesign.MDI_GITHUB_CIRCLE));
        buttonBox.getChildren().add(repositoryButton);

        issueTrackerButton = new Button("Issues");
        issueTrackerButton.getStyleClass().addAll("library-button", "issues");
        issueTrackerButton.setGraphic(new FontIcon(MaterialDesign.MDI_BUG));
        buttonBox.getChildren().add(issueTrackerButton);

        discussionsButton = new Button("Discussion");
        discussionsButton.getStyleClass().addAll("library-button", "discussion");
        discussionsButton.setGraphic(new FontIcon(MaterialDesign.MDI_COMMENT));
        buttonBox.getChildren().add(discussionsButton);

        VBox vBox = new VBox(titleLabel, summaryLabel);
        vBox.getStyleClass().add("vbox");
        vBox.setAlignment(Pos.TOP_LEFT);

        HBox.setHgrow(vBox, Priority.ALWAYS);

        logoImageView.setFitWidth(48);
        logoImageView.setPreserveRatio(true);

        StackPane logoWrapper = new StackPane(logoImageView);
        logoWrapper.setMinWidth(48);
        logoWrapper.setMaxWidth(48);
        StackPane.setAlignment(logoImageView, Pos.TOP_LEFT);

        HBox hBox = new HBox(vBox, logoWrapper);
        hBox.setAlignment(Pos.TOP_LEFT);
        hBox.getStyleClass().add("hbox");

        infoView = new ThumbnailScrollPane(rootPane);
        infoView.libraryProperty().bind(itemProperty());

        itemProperty().addListener(it -> {
            Library library = getItem();
            if (library != null) {
                infoView.libraryInfoProperty().bind(DataRepository.getInstance().libraryInfoProperty(getItem()));
            }
        });

        VBox outerBox = new VBox(hBox, infoView, buttonBox);
        outerBox.visibleProperty().bind(itemProperty().isNotNull());
        outerBox.getStyleClass().add("outer-box");

        setGraphic(outerBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        outerBox.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(Library item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            Util.setLink(homepageButton, getItem().getHomepage(), getItem().getTitle());
            Util.setLink(repositoryButton, getItem().getRepository(), getItem().getTitle());
            Util.setLink(issueTrackerButton, getItem().getIssueTracker(), getItem().getTitle());
            Util.setLink(discussionsButton, getItem().getDiscussionBoard(), getItem().getTitle());

            licenseLabel.setLicense(item.getLicense());
            licenseLabel.getStyleClass().setAll("label", "license-label", item.getLicense().name().toLowerCase());

            logoImageView.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(item));
            logoImageView.setVisible(true);

            titleLabel.setText(item.getTitle());
            summaryLabel.setText(item.getSummary());

            homepageButton.setVisible(StringUtils.isNotEmpty(item.getHomepage()));
            homepageButton.setManaged(StringUtils.isNotEmpty(item.getHomepage()));

            repositoryButton.setVisible(StringUtils.isNotEmpty(item.getRepository()));
            repositoryButton.setManaged(StringUtils.isNotEmpty(item.getRepository()));

            issueTrackerButton.setVisible(StringUtils.isNotEmpty(item.getIssueTracker()));
            issueTrackerButton.setManaged(StringUtils.isNotEmpty(item.getIssueTracker()));

            discussionsButton.setVisible(StringUtils.isNotEmpty(item.getDiscussionBoard()));
            discussionsButton.setManaged(StringUtils.isNotEmpty(item.getDiscussionBoard()));

            buttonBox.setVisible(homepageButton.isVisible() || repositoryButton.isVisible() || issueTrackerButton.isVisible() || discussionsButton.isVisible());
            buttonBox.setManaged(buttonBox.isVisible());
        }
    }
}
