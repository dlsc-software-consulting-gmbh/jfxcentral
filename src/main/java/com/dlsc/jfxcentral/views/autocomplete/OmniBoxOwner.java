package com.dlsc.jfxcentral.views.autocomplete;

/**
 * The interfaces that needs to be implemented by any control that wants to utilize an {@link OmniBox}.
 *
 * @author Dirk Lemmermann
 */
public interface OmniBoxOwner {

	/**
	 * Called by the {@link OmniBox} when it wants its owner to display it.
	 *
	 * @see OmniBoxTextField#showOmniBox()
	 */
	void showOmniBox();

	/**
	 * Called by the {@link OmniBox} when it wants its owner to hide it.
	 *
	 * @see OmniBoxTextField#hideOmniBox()
	 */
	void hideOmniBox();
}
