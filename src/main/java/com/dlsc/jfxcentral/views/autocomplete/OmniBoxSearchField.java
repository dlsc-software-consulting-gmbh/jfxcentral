package com.dlsc.jfxcentral.views.autocomplete;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

/**
 * A specialized text field used for searches. The field displays an icon on its
 * left-hand side to indicate that it can be used for searches.
 *
 * @author Dirk Lemmermann
 */
public class OmniBoxSearchField extends OmniBoxTextField {

	/**
	 * Constructs a new instance.
	 */
	public OmniBoxSearchField() {
		setLeft(new FontIcon(Material.SEARCH));
	}
}
