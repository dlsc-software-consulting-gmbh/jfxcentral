package com.dlsc.jfxcentral.views.mobile.master.cells;

import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.RealWorldApp;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.View;
import com.dlsc.jfxcentral.views.mobile.MobileAdvancedListCell;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class MobileMasterRealWorldAppCell extends MobileAdvancedListCell<RealWorldApp> {

    private final Label nameLabel = new Label();
    private final Label companyLabel = new Label();
    private final Label domainLabel = new Label();

    private final ImageView imageView = new ImageView();
    private final MarkdownView summaryMarkdownView = new MarkdownView();
    private final GridPane gridPane;

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

        gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(summaryMarkdownView, 0, 1);
        gridPane.add(wrap(new FontIcon(MaterialDesign.MDI_FACTORY), companyLabel), 0, 2);
        gridPane.add(wrap(new FontIcon(MaterialDesign.MDI_DOMAIN), domainLabel), 0, 3);
        gridPane.add(imageView, 1, 0);

        GridPane.setValignment(imageView, VPos.TOP);
        GridPane.setRowSpan(imageView, 4);
        GridPane.setHgrow(nameLabel, Priority.ALWAYS);

        imageView.setFitWidth(120);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(gridPane);

        gridPane.visibleProperty().bind(emptyProperty().not());
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
