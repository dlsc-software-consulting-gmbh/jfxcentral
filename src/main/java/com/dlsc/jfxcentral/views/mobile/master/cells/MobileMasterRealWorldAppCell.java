package com.dlsc.jfxcentral.views.mobile.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileAdvancedListCell;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class MobileMasterRealWorldAppCell extends MobileAdvancedListCell<RealWorldApp> {

    private final Label nameLabel = new Label();
    private final Label companyLabel = new Label();
    private final Label domainLabel = new Label();

    private final ImageView imageView = new ImageView();
    private final MarkdownView summaryMarkdownView = new MarkdownView();
    private final VBox vBox;

    public MobileMasterRealWorldAppCell() {
        getStyleClass().add("mobile-master-app-list-cell");

        setPrefWidth(0);
        setMinWidth(0);

        imageView.visibleProperty().bind(imageView.imageProperty().isNotNull());
        imageView.managedProperty().bind(imageView.imageProperty().isNotNull());
        imageView.setPreserveRatio(true);

        nameLabel.getStyleClass().add("title-label");
        nameLabel.setMinWidth(0);
        nameLabel.setWrapText(true);
        nameLabel.setMinHeight(Region.USE_PREF_SIZE);

        companyLabel.getStyleClass().add("company-label");
        companyLabel.setMinWidth(0);
        companyLabel.setWrapText(true);
        companyLabel.setMinHeight(Region.USE_PREF_SIZE);
        companyLabel.visibleProperty().bind(companyLabel.textProperty().isNotEmpty());
        companyLabel.managedProperty().bind(companyLabel.textProperty().isNotEmpty());
        companyLabel.setAlignment(Pos.TOP_LEFT);

        domainLabel.getStyleClass().add("domain-label");
        domainLabel.setMinWidth(0);
        domainLabel.setWrapText(true);
        domainLabel.setMinHeight(Region.USE_PREF_SIZE);
        domainLabel.visibleProperty().bind(domainLabel.textProperty().isNotEmpty());
        domainLabel.managedProperty().bind(domainLabel.textProperty().isNotEmpty());
        domainLabel.setAlignment(Pos.TOP_LEFT);

        vBox = new VBox(nameLabel, summaryMarkdownView, wrap(new FontIcon(MaterialDesign.MDI_FACTORY), companyLabel), wrap(new FontIcon(MaterialDesign.MDI_DOMAIN), domainLabel));
        vBox.getStyleClass().add("vbox");
        vBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(vBox, Priority.ALWAYS);

        HBox hBox = new HBox(vBox, imageView);
        hBox.getStyleClass().add("hbox");

        imageView.setFitWidth(120);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(hBox);

        hBox.visibleProperty().bind(emptyProperty().not());
    }

    private Node wrap(FontIcon fontIcon, Label label) {
        fontIcon.visibleProperty().bind(label.visibleProperty());
        fontIcon.managedProperty().bind(label.managedProperty());
        HBox wrapper = new HBox(fontIcon, label);
        wrapper.setAlignment(Pos.TOP_LEFT);
        wrapper.getStyleClass().add("icon-wrapper");
        return wrapper;
    }

    @Override
    protected void updateItem(RealWorldApp app, boolean empty) {
        super.updateItem(app, empty);

        if (!empty && app != null) {
            nameLabel.setText(app.getName());
            companyLabel.setText(app.getCompany());
            domainLabel.setText(app.getDomain());
            imageView.imageProperty().bind(ImageManager.getInstance().realWorldAppImageProperty(app));
            summaryMarkdownView.setMdString(app.getSummary());
            setMasterCellLink(MobileMasterRealWorldAppCell.this, app, app.getSummary(), View.REAL_WORLD);
        } else {
            nameLabel.setText("");
            companyLabel.setText("");
            domainLabel.setText("");
            imageView.imageProperty().unbind();
            summaryMarkdownView.setMdString("");
        }
    }
}
