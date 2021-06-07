package com.dlsc.jfxcentral.views.autocomplete;

import javafx.concurrent.Task;

/**
 * An interface used for giving the search services access to the search
 * context. The context is usually defined by the {@link Task} that is executing
 * the services, hence the context allows the service to check if the task has
 * been cancelled.
 *
 * @author Dirk Lemmermann
 */
public interface SearchContext {

	/**
	 * Returns true if the background search task was cancelled.
	 *
	 * @return true if the search has been cancelled
	 */
	boolean isCancelled();

	/**
	 * Returns true if the user wants to see all possible results. Should
	 * only be used if total number of results is not very big.
	 *
	 * @return true if the search shall return all possible results
	 */
	boolean isFindAll();
}
