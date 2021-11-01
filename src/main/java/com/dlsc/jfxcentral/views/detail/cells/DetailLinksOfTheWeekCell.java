package com.dlsc.jfxcentral.views.detail.cells;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.data.model.LinksOfTheWeek;
import com.dlsc.jfxcentral.views.MarkdownView;
import com.dlsc.jfxcentral.views.RootPane;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class DetailLinksOfTheWeekCell extends DetailCell<LinksOfTheWeek> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd yyyy");
    private final Label titleLabel = new Label();
    private final MarkdownView markdownView = new MarkdownView();

    public DetailLinksOfTheWeekCell(RootPane rootPane) {
        setPrefWidth(0);


        getStyleClass().add("detail-links-of-the-week-cell");

        titleLabel.getStyleClass().add("title-label");
        titleLabel.setWrapText(true);
        titleLabel.setMinHeight(Region.USE_PREF_SIZE);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        VBox vBox = new VBox(titleLabel, markdownView);
        vBox.getStyleClass().add("vbox");

        setGraphic(vBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(LinksOfTheWeek links, boolean empty) {
        super.updateItem(links, empty);

        if (!empty && links != null) {
            titleLabel.setText("Week of " + dateTimeFormatter.format(links.getCreatedOn()));
            markdownView.mdStringProperty().bind(DataRepository.getInstance().linksOfTheWeekTextProperty(links));
        } else {
            titleLabel.setText("");
            markdownView.mdStringProperty().unbind();
        }
    }
}
