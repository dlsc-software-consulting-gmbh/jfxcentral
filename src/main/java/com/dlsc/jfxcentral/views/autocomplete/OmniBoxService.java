package com.dlsc.jfxcentral.views.autocomplete;

import javafx.scene.image.Image;

import java.util.List;

/**
 * An {@link OmniBoxService} is an interface that needs to be implemented by any
 * kind of service that shall be used as a search service by the {@link OmniBox}
 * control.
 *
 * @see OmniBox#getOmniBoxServices()
 *
 * @param <T>
 *            the type of the objects wrapped inside the {@link SearchResult}
 *            instances
 * @author Dirk Lemmermann
 */
public interface OmniBoxService<T> {

	/**
	 * The name of the service, e.g. "PDF Files", "Images", "Music Files".
	 *
	 * @return the name of the service
	 */
	String getServiceName();

	/**
	 * Performs the actual search based on the given pattern. It is up to the
	 * service to interpret the pattern.
	 *
	 * @param ctx
	 *            a reference back to the context in which the search is
	 *            running. The context can be asked if the search has been
	 *            cancelled.
	 * @param pattern
	 *            the search pattern
	 * @return a list of search results
	 */
	List<SearchResult<T>> search(SearchContext ctx, String pattern);

	/**
	 * Returns an icon image for the service. This icon can be used by the view
	 * to create a nice separation of the search results from different
	 * services. The default implementation returns null.
	 *
	 * @return the icon for the service
	 */
	default Image getServiceIcon() {
		return null;
	}
}
