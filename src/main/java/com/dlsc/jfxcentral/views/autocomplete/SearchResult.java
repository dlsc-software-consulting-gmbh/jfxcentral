package com.dlsc.jfxcentral.views.autocomplete;

import javafx.scene.Node;
import javafx.scene.control.ListView;

import java.util.Objects;

/**
 * A wrapper object used for nicely returning a search result. The object wraps
 * the actual value and provides ways to decorate the result in the
 * {@link OmniBox} control.
 *
 * @param <T>
 *            the type of the wrapped actual search result
 *
 * @author Dirk Lemmermann
 */
public class SearchResult<T> {

	// the search result
	private final T value;

	// the category to which the result belongs
	private final String category;

	// the primary image / icon used for the result inside the view
	private final Node icon;

	// additional images / badges shown by the view
	private final Node[] badges;

	/**
	 * Constructs a new search result.
	 *
	 * @param value
	 *            the wrapped actual search result
	 */
	public SearchResult(T value) {
		this(value, null, null, (Node[]) null);
	}

	/**
	 * Constructs a new search result.
	 *
	 * @param value
	 *            the wrapped actual search result
	 * @param icon
	 *            the icon shown in front of the result in the view
	 */
	public SearchResult(T value, Node icon) {
		this(value, icon, null, (Node[]) null);
	}

	/**
	 * Constructs a new search result.
	 *
	 * @param value
	 *            the wrapped actual search result
	 * @param category
	 *            the category to which the result belongs (the view might
	 *            distinguish visually between different categories)
	 */
	public SearchResult(T value, String category) {
		this(value, null, category, (Node[]) null);
	}

	/**
	 * Constructs a new search result.
	 *
	 * @param value
	 *            the wrapped actual search result
	 * @param icon
	 *            the icon shown in front of the result in the view
	 * @param category
	 *            the category to which the result belongs (the view might
	 *            distinguish visually between different categories)
	 */
	public SearchResult(T value, Node icon, String category) {
		this(value, icon, category, (Node[]) null);
	}

	/**
	 * Constructs a new search result.
	 *
	 * @param value
	 *            the wrapped actual search result
	 * @param icon
	 *            the icon shown in front of the result in the view
	 * @param category
	 *            the category to which the result belongs (the view might
	 *            distinguish visually between different categories)
	 * @param badges
	 *            various additional icons shown for the entry inside the view
	 */
	public SearchResult(T value, Node icon, String category, Node... badges) {
		this.value = Objects.requireNonNull(value);
		this.category = category;
		this.icon = icon;
		this.badges = badges;
	}

	/**
	 * Returns the wrapped actual search result object.
	 *
	 * @return the seach result value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Returns the category to which the search result belongs.
	 *
	 * @return the search category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the icon used for the search result inside the view.
	 *
	 * @return the icon
	 */
	public Node getIcon() {
		return icon;
	}

	/**
	 * Returns the badges used for the search result inside the view.
	 *
	 * @return the badges
	 */
	public Node[] getBadges() {
		return badges;
	}

	/**
	 * Returns a human-readable text for the search result. This text will
	 * be shown by the {@link ListView} of the {@link OmniBox}.
	 *
	 * @return the human-readable text for the entry
	 */
	public String getText() {
		T v = getValue();
		if (v == null) {
			return "<Missing Value>";
		}

		return v.toString();
	}

	@Override
	public String toString() {
		return "SearchResult [value=" + value + ", category=" + category + "]";
	}
}
