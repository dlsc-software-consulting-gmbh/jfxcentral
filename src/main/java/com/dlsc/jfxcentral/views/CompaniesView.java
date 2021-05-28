package com.dlsc.jfxcentral.views;

import com.dlsc.jfxcentral.DataRepository;
import com.dlsc.jfxcentral.ImageManager;
import com.dlsc.jfxcentral.MarkdownView;
import com.dlsc.jfxcentral.RootPane;
import com.dlsc.jfxcentral.model.Company;
import com.dlsc.jfxcentral.panels.PrettyListView;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class CompaniesView extends PageView {

    public CompaniesView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("company-view");

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Companies");

        PrettyListView<Company> listView = new PrettyListView<>();
        listView.setCellFactory(view -> new CompanyCell(rootPane));
        listView.itemsProperty().bind(DataRepository.getInstance().companiesProperty());
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        setContent(sectionPane);
    }

    private final ObjectProperty<Company> company = new SimpleObjectProperty<>(this, "video");

    public Company getCompany() {
        return company.get();
    }

    public ObjectProperty<Company> companyProperty() {
        return company;
    }

    public void setCompany(Company company) {
        this.company.set(company);
    }

    static class CompanyCell extends AdvancedListCell<Company> {

        private final Label titleLabel = new Label();
        private final MarkdownView descriptionMarkdownView = new MarkdownView();
        private final ImageView thumbnailView = new ImageView();
        private final Button homepageButton = new Button("Homepage");
        private final RootPane rootPane;

        public CompanyCell(RootPane rootPane) {
            this.rootPane = rootPane;

            getStyleClass().add("company-cell");

            setPrefWidth(0);

            homepageButton.setGraphic(new FontIcon(MaterialDesign.MDI_HOME));
            homepageButton.setOnAction(evt -> Util.browse(getItem().getHomepage()));

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
                titleLabel.setText(company.getName());
                descriptionMarkdownView.mdStringProperty().bind(DataRepository.getInstance().companyDescriptionProperty(company));
                thumbnailView.setVisible(true);
                thumbnailView.setManaged(true);
                thumbnailView.imageProperty().bind(ImageManager.getInstance().companyImageProperty(company));
            }
        }
    }
}