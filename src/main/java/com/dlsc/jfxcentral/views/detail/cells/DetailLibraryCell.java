package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Library;
import com.dlsc.jfxcentral.panels.LicenseLabel;
import com.dlsc.jfxcentral.util.PageUtil;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.detail.ThumbnailScrollPane;
import com.dlsc.jfxcentral.views.detail.cells.ResponsiveBox.ImageLocation;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailLibraryCell extends AdvancedListCell<Library> {

    private final Button detailButton;
    private final Button homepageButton;
    private final Button repositoryButton;
    private final Button issueTrackerButton;
    private final Button discussionsButton;
    private final ThumbnailScrollPane infoView;
    private final ResponsiveBox responsiveBox;

    private LicenseLabel licenseLabel = new LicenseLabel();

    public DetailLibraryCell(RootPane rootPane, boolean largeImage) {
        getStyleClass().add("detail-library-cell");

        homepageButton = new Button("Homepage");
        homepageButton.getStyleClass().addAll("library-button", "homepage");
        homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));

        detailButton = new Button("Detail");
        detailButton.getStyleClass().addAll("library-button", "detail");
        detailButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));

        repositoryButton = new Button("Repository");
        repositoryButton.getStyleClass().addAll("library-button", "repository");
        repositoryButton.setGraphic(new FontIcon(MaterialDesign.MDI_GITHUB_CIRCLE));

        issueTrackerButton = new Button("Issues");
        issueTrackerButton.getStyleClass().addAll("library-button", "issues");
        issueTrackerButton.setGraphic(new FontIcon(MaterialDesign.MDI_BUG));

        discussionsButton = new Button("Discussion");
        discussionsButton.getStyleClass().addAll("library-button", "discussion");
        discussionsButton.setGraphic(new FontIcon(MaterialDesign.MDI_COMMENT));

        infoView = new ThumbnailScrollPane(rootPane);
        infoView.libraryProperty().bind(itemProperty());

        itemProperty().addListener(it -> {
            Library library = getItem();
            if (library != null) {
                infoView.libraryInfoProperty().bind(DataRepository.getInstance().libraryInfoProperty(getItem()));
            }
        });

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ImageLocation.HIDE : largeImage ? ImageLocation.LARGE_ON_SIDE : ImageLocation.SMALL_ON_SIDE);
        responsiveBox.setFooter(infoView);
        responsiveBox.setSmallImageWidth(48);
        responsiveBox.setLargeImageWidth(48);
        responsiveBox.getExtraControls().addAll(homepageButton, detailButton, repositoryButton, issueTrackerButton, discussionsButton);

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        responsiveBox.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(Library item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            Util.setLink(detailButton, PageUtil.getLink(item), getItem().getName());
            Util.setLink(homepageButton, getItem().getHomepage(), getItem().getName());
            Util.setLink(repositoryButton, getItem().getRepository(), getItem().getName());
            Util.setLink(issueTrackerButton, getItem().getIssueTracker(), getItem().getName());
            Util.setLink(discussionsButton, getItem().getDiscussionBoard(), getItem().getName());

            licenseLabel.setLicense(item.getLicense());
            licenseLabel.getStyleClass().setAll("label", "license-label", item.getLicense().name().toLowerCase());

            responsiveBox.setTitle(item.getName());
            responsiveBox.setDescription(item.getDescription());
            responsiveBox.imageProperty().bind(ImageManager.getInstance().libraryImageProperty(item));

            homepageButton.setVisible(StringUtils.isNotEmpty(item.getHomepage()));
            homepageButton.setManaged(StringUtils.isNotEmpty(item.getHomepage()));

            repositoryButton.setVisible(StringUtils.isNotEmpty(item.getRepository()));
            repositoryButton.setManaged(StringUtils.isNotEmpty(item.getRepository()));

            issueTrackerButton.setVisible(StringUtils.isNotEmpty(item.getIssueTracker()));
            issueTrackerButton.setManaged(StringUtils.isNotEmpty(item.getIssueTracker()));

            discussionsButton.setVisible(StringUtils.isNotEmpty(item.getDiscussionBoard()));
            discussionsButton.setManaged(StringUtils.isNotEmpty(item.getDiscussionBoard()));
        }
    }
}
