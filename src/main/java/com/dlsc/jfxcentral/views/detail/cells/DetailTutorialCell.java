package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.ImageManager;
import com.dlsc.jfxcentral.data.model.Tutorial;
import com.dlsc.jfxcentral.util.Util;
import com.dlsc.jfxcentral.views.RootPane;
import com.dlsc.jfxcentral.views.page.StandardIcons;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.javafx.FontIcon;

public class DetailTutorialCell extends DetailCell<Tutorial> {

    private final Button visitButton = new Button("Visit Tutorial");
    private final Label commercialLabel = new Label("$$$");
    private final RootPane rootPane;
    private final ResponsiveBox responsiveBox;

    public DetailTutorialCell(RootPane rootPane, boolean largeImage) {
        this.rootPane = rootPane;

        getStyleClass().add("detail-tutorial-cell");

        setPrefWidth(0);

        commercialLabel.getStyleClass().add("commercial-label");

        visitButton.setGraphic(new FontIcon(StandardIcons.TUTORIAL));

        responsiveBox = new ResponsiveBox(rootPane.isMobile() ? ResponsiveBox.ImageLocation.BANNER : largeImage ? ResponsiveBox.ImageLocation.LARGE_ON_SIDE : ResponsiveBox.ImageLocation.SMALL_ON_SIDE);
        responsiveBox.getTitleLabel().setGraphic(commercialLabel);
        responsiveBox.getExtraControls().addAll(visitButton);
        responsiveBox.visibleProperty().bind(itemProperty().isNotNull());

        setGraphic(responsiveBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {
                showLargeImage(getItem());
            }
        });
    }

    private void showLargeImage(Tutorial tutorial) {
        ImageView largeImageView = new ImageView();
        largeImageView.setFitWidth(800);
        largeImageView.setPreserveRatio(true);
        largeImageView.imageProperty().bind(ImageManager.getInstance().tutorialImageLargeProperty(tutorial));
        rootPane.getOverlayPane().setContent(largeImageView);
    }

    @Override
    protected void updateItem(Tutorial tutorial, boolean empty) {
        super.updateItem(tutorial, empty);

        if (!empty && tutorial != null) {
            responsiveBox.setTitle(tutorial.getName());

            commercialLabel.setVisible(tutorial.isCommercial());

            responsiveBox.imageProperty().bind(ImageManager.getInstance().tutorialImageProperty(tutorial));
            responsiveBox.getMarkdownView().setBaseURL(DataRepository.BASE_URL + "tutorials/" + tutorial.getId());
            responsiveBox.descriptionProperty().bind(DataRepository.getInstance().tutorialTextProperty(tutorial));

            Util.setLink(visitButton, tutorial.getUrl(), tutorial.getName());
        }
    }
}
