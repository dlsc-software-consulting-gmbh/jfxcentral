package com.dlsc.jfxcentral.views.detail;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.data.model.Company;
import com.dlsc.jfxcentral.panels.PrettyListView;
import com.dlsc.jfxcentral.panels.SectionPane;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.AdvancedListCell;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.util.Comparator;

public class CompaniesDetailView extends DetailView<Company> {

    public CompaniesDetailView(RootPane rootPane) {
        super(rootPane);

        getStyleClass().add("companies-detail-view");

        SectionPane sectionPane = new SectionPane();
        sectionPane.setTitle("Companies");

        SortedList<Company> sortedList = new SortedList<>(DataRepository.getInstance().companiesProperty());
        sortedList.setComparator(Comparator.comparing(Company::getName));

        PrettyListView<Company> listView = new PrettyListView<>();
        listView.setCellFactory(view -> new CompanyCell(rootPane));
        listView.setItems(sortedList);
        listView.getSelectionModel().selectedItemProperty().addListener(it -> setSelectedItem(listView.getSelectionModel().getSelectedItem()));
        VBox.setVgrow(listView, Priority.ALWAYS);
        sectionPane.getNodes().add(listView);

        selectedItemProperty().addListener(it -> listView.getSelectionModel().select(getSelectedItem()));

        setContent(sectionPane);
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