package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Company;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class DetailCompanyCell extends DetailCell<Company> {

    private final Label titleLabel = new Label();
    private final MarkdownView descriptionMarkdownView = new MarkdownView();
    private final ImageView thumbnailView = new ImageView();
    private final Button homepageButton = new Button("Homepage");
    private final RootPane rootPane;

    public DetailCompanyCell(RootPane rootPane) {
        this.rootPane = rootPane;

        getStyleClass().add("company-cell");

        setPrefWidth(0);

        homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_HOME));

        titleLabel.getStyleClass().addAll("header2", "name-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);

        descriptionMarkdownView.getStyleClass().add("description-markdown");

        thumbnailView.setPreserveRatio(true);
        thumbnailView.setFitHeight(64);
        thumbnailView.setFitWidth(200);

        StackPane logoWrapper = new StackPane(thumbnailView);
        logoWrapper.getStyleClass().add("logo-wrapper");
        logoWrapper.setMaxHeight(Region.USE_PREF_SIZE);
        logoWrapper.setPrefSize(thumbnailView.getFitWidth(), thumbnailView.getFitHeight());
        logoWrapper.setMinSize(thumbnailView.getFitWidth(), thumbnailView.getFitHeight());
        logoWrapper.setMaxSize(thumbnailView.getFitWidth(), thumbnailView.getFitHeight());

        StackPane.setAlignment(thumbnailView, Pos.TOP_LEFT);

        HBox buttonBox = new HBox(10, homepageButton);
        buttonBox.setMinHeight(Region.USE_PREF_SIZE);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);

        VBox vBox = new VBox(titleLabel, descriptionMarkdownView, buttonBox);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setFillWidth(true);
        vBox.getStyleClass().add("vbox");

        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hBox = new HBox(logoWrapper, vBox);
        hBox.getStyleClass().add("hbox");
        hBox.setAlignment(Pos.TOP_LEFT);

        setGraphic(hBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        hBox.visibleProperty().bind(itemProperty().isNotNull());
    }

    @Override
    protected void updateItem(Company company, boolean empty) {
        super.updateItem(company, empty);

        if (!empty && company != null) {
            Util.setLink(homepageButton, getItem().getHomepage(), getItem().getName());
            titleLabel.setText(company.getName());
            descriptionMarkdownView.mdStringProperty().bind(DataRepository.getInstance().companyDescriptionProperty(company));
            thumbnailView.setVisible(true);
            thumbnailView.setManaged(true);
            thumbnailView.imageProperty().bind(ImageManager.getInstance().companyImageProperty(company));
        }
    }
}
