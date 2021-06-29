package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Tool;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailToolCell extends AdvancedListCell<Tool> {

    private final Button homepageButton;
    private final Button repositoryButton;

    private Label titleLabel = new Label();
    private MarkdownView descriptionMarkdownView = new MarkdownView();

    private ImageView logoImageView = new ImageView();
    private HBox buttonBox = new HBox();

    public DetailToolCell(RootPane rootPane) {
        getStyleClass().add("detail-tool-cell");

        titleLabel.getStyleClass().addAll("header2", "title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
        titleLabel.setAlignment(Pos.TOP_LEFT);
        titleLabel.setContentDisplay(ContentDisplay.RIGHT);

        buttonBox.getStyleClass().add("button-box");

        homepageButton = new Button("Homepage");
        homepageButton.getStyleClass().addAll("library-button", "homepage");
        homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_WEB));
        buttonBox.getChildren().add(homepageButton);

        repositoryButton = new Button("Repository");
        repositoryButton.getStyleClass().addAll("library-button", "repository");
        repositoryButton.setGraphic(new FontIcon(MaterialDesign.MDI_GITHUB_CIRCLE));
        buttonBox.getChildren().add(repositoryButton);

        VBox vBox = new VBox(titleLabel, descriptionMarkdownView);
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

        VBox outerBox = new VBox(hBox, buttonBox);
        outerBox.visibleProperty().bind(itemProperty().isNotNull());
        outerBox.getStyleClass().add("outer-box");

        setGraphic(outerBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        outerBox.visibleProperty().bind(emptyProperty().not());
    }

    @Override
    protected void updateItem(Tool item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            Util.setLink(homepageButton, getItem().getHomepage(), getItem().getHomepage());
            Util.setLink(repositoryButton, getItem().getRepository(), getItem().getRepository());

            logoImageView.imageProperty().bind(ImageManager.getInstance().toolImageProperty(item));
            logoImageView.setVisible(true);

            titleLabel.setText(item.getName());
            descriptionMarkdownView.setMdString(item.getDescription());

            homepageButton.setVisible(StringUtils.isNotEmpty(item.getHomepage()));
            homepageButton.setManaged(StringUtils.isNotEmpty(item.getHomepage()));

            repositoryButton.setVisible(StringUtils.isNotEmpty(item.getRepository()));
            repositoryButton.setManaged(StringUtils.isNotEmpty(item.getRepository()));

            buttonBox.setVisible(homepageButton.isVisible() || repositoryButton.isVisible());
            buttonBox.setManaged(buttonBox.isVisible());
        }
    }
}
