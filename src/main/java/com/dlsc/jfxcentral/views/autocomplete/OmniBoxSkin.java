package com.dlsc.jfxcentral.views.autocomplete;

import com.dlsc.jfxcentral.views.AdvancedListView;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * Skin class of {@link OmniBox}.
 *
 * @author Dirk Lemmermann
 */
public class OmniBoxSkin implements Skin<OmniBox> {

	private final OmniBox box;
	private final VBox vbox;
	private final AdvancedListView<?> listView;

	public OmniBoxSkin(OmniBox box) {
		this.box = Objects.requireNonNull(box);

		listView = box.getListView();
		listView.setMinHeight(0);
		listView.setMaxWidth(Double.MAX_VALUE);

        vbox = new VBox(listView);
        vbox.getStyleClass().add("omnibox");
		
		VBox.setVgrow(listView, Priority.ALWAYS);
	}

	@Override
	public OmniBox getSkinnable() {
		return box;
	}

	@Override
	public Node getNode() {
		return vbox;
	}

	@Override
	public void dispose() {
	}
}
