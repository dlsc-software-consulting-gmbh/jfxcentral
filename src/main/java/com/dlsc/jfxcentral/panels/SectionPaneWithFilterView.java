package com.dlsc.jfxcentral.panels;

import com.dlsc.gemsfx.FilterView;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SectionPaneWithFilterView<T> extends SectionPane {

    private final VBox titleBox;
    private final Node placeholder;
    private final VBox childrenBox;

    private final FilterView<T> filterView = new FilterView<>();

    public SectionPaneWithFilterView() {
        filterView.titleProperty().bindBidirectional(titleProperty());
        filterView.subtitleProperty().bindBidirectional(subtitleProperty());

        filterView.visibleProperty().bind(expandedProperty());
        filterView.managedProperty().bind(expandedProperty());

        filterView.extrasProperty().bind(extrasProperty());
        filterView.getFilteredItems().addListener((Observable it) -> {
            if (isEnableAutoSubtitle() && isExpanded()) {
                int itemCount = filterView.getItems().size();
                int filterCount = filterView.getFilteredItems().size();

                if (!filterView.getFilters().isEmpty() || itemCount != filterCount) {
                    if (filterCount > 1) {
                        setSubtitle("Showing " + filterCount + " items out of " + itemCount);
                    } else if (filterCount == 1) {
                        setSubtitle("Showing one item out of " + itemCount);
                    } else {
                        setSubtitle("No items match");
                    }
                } else {
                    if (itemCount > 1) {
                        setSubtitle("Showing all " + itemCount + " items");
                    } else if (itemCount == 1){
                        setSubtitle("Showing the only item found");
                    } else {
                        setSubtitle("No items found");
                    }
                }
            }
        });

        expandedProperty().addListener(it -> {
            if (isEnableAutoSubtitle() && !isExpanded()) {
                setSubtitle(null);
            }
        });

        Label titleLabel = getTitleLabel();
        titleLabel.getStyleClass().add("title-label");
        titleLabel.textProperty().bind(titleProperty());
        titleLabel.managedProperty().bind(titleProperty().isNotEmpty());

        Label subtitleLabel = getSubtitleLabel();
        subtitleLabel.getStyleClass().add("subtitle-label");
        subtitleLabel.textProperty().bind(subtitleProperty());
        subtitleLabel.managedProperty().bind(subtitleProperty().isNotEmpty());

        titleBox = new VBox(titleLabel, subtitleLabel);
        titleBox.setFillWidth(true);
        titleBox.setAlignment(Pos.TOP_LEFT);
        titleBox.getStyleClass().add("title-box");

        placeholder = createPlaceholder();

        childrenBox = new VBox();
        childrenBox.getStyleClass().add("children-box");
        childrenBox.setFillWidth(true);
        childrenBox.visibleProperty().bind(expandedProperty());
        childrenBox.managedProperty().bind(expandedProperty());
        VBox.setVgrow(childrenBox, Priority.ALWAYS);
        Bindings.bindContent(childrenBox.getChildren(), getNodes());
        VBox.setMargin(childrenBox, new Insets(20, 0, 0, 0));

        InvalidationListener updateViewListener = it -> updateView();
        expandedProperty().addListener(updateViewListener);
        footerProperty().addListener(updateViewListener);

        updateView();
    }

    private void updateView() {
        VBox vbox = new VBox();
        VBox.setVgrow(vbox, Priority.ALWAYS);
        vbox.getStyleClass().add("section-pane-container");

        if (isExpanded()) {
            vbox.getChildren().setAll(getFilterView(), childrenBox);
        } else {
            vbox.getChildren().setAll(titleBox, placeholder);
        }

        Node footer = getFooter();
        if (footer != null) {
            vbox.getChildren().add(footer);
        }

        getChildren().setAll(vbox);
    }
    
    public SectionPaneWithFilterView(Node content) {
        this();
        getNodes().add(content);
    }

    private final BooleanProperty enableAutoSubtitle = new SimpleBooleanProperty(this, "enableAutoSubtitle", true);

    public boolean isEnableAutoSubtitle() {
        return enableAutoSubtitle.get();
    }

    public BooleanProperty enableAutoSubtitleProperty() {
        return enableAutoSubtitle;
    }

    public void setEnableAutoSubtitle(boolean enableAutoSubtitle) {
        this.enableAutoSubtitle.set(enableAutoSubtitle);
    }

    public FilterView<T> getFilterView() {
        return filterView;
    }
}
