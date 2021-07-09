package com.dlsc.jfxcentral.views.autocomplete;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.time.Duration;
import java.util.List;

/**
 * A textfield with an integrated {@link OmniBox}. A search will be executed
 * (after an initial delay) whenever the value of {@link #textProperty()}
 * changes or after an explicit call to {@link #startSearch(boolean)}. This
 * behaviour can be controlled via the {@link #autoSearchProperty()}. Searches
 * will be run agains one or more {@link OmniBoxService} instances that can be
 * added via {@link #getOmniBoxServices()}.
 * <h3>Code Example</h3>
 *
 * <pre>
 * OmniBoxTextField field = new OmniBoxTextField();
 * field.setPromptText("Search");
 * field.setAutoSearch(true);
 * field.setSearchDelay(Duration.ofSeconds(1));
 * field.getOmniBoxServices().add(new GoogleSearchService());
 * field.getOmniBoxServices().add(new YahooSearchService());
 * field.getOmniBoxServices().add(new BingSearchService());
 * </pre>
 *
 * @author Dirk Lemmermann
 */
public class OmniBoxTextField extends CustomTextField implements OmniBoxOwner {

	private final OmniBox omniBox = new OmniBox(this);

	private final ToggleButton lookupButton = new ToggleButton("Go!");

	/**
	 * Constructs a new textfield.
	 */
	public OmniBoxTextField() {
		init();
	}

	/**
	 * Constructs a new textfield with the given text.
	 *
	 * @param text
	 *            the initial text shown by the field.
	 */
	public OmniBoxTextField(String text) {
		setText(text);
		init();
	}

	public final OmniBox getOmniBox() {
		return omniBox;
	}

	private boolean updateFieldWithSelectedResult = true;

	public void setUpdateFieldWithSelectedResult(boolean updateFieldWithSelectedResult) {
		this.updateFieldWithSelectedResult = updateFieldWithSelectedResult;
	}

	public boolean isUpdateFieldWithSelectedResult() {
		return updateFieldWithSelectedResult;
	}

	private void init() {
	    getStyleClass().add("omnibox-text-field");

	    setPromptText("Search all ...");

	    setFocusTraversable(false);

		// work-around for bug in CustomTextField
		setPrefWidth(0);
		setMinWidth(0);

		omniBox.getListView().setPrefWidth(600);
//		omniBox.getListView().getItems().addListener((Observable it) -> omniBox.getListView().setPrefWidth(Math.max(getWidth() - 2, omniBox.getListView().prefWidth(-1))));

		textProperty().addListener(it -> {
			if (!isDisable() && !isDisabled()) {
				omniBox.setSearchText(getText());
			}
		});

		lookupButton.setOnMouseClicked(evt -> requestFocus());
		lookupButton.setFocusTraversable(false);
		lookupButton.setCursor(Cursor.DEFAULT);
		lookupButton.selectedProperty().bindBidirectional(omniBox.findAllProperty());

		FontIcon clearSearch = new FontIcon(Material.CLEAR);
		clearSearch.setOnMouseClicked(evt -> clear());
		clearSearch.visibleProperty().bind(textProperty().isNotEmpty());
		clearSearch.managedProperty().bind(textProperty().isNotEmpty());
		clearSearch.setCursor(Cursor.DEFAULT);

		HBox hBox = new HBox(10, clearSearch, lookupButton);
		hBox.setAlignment(Pos.CENTER_RIGHT);
		setRight(hBox);

		focusedProperty().addListener(it -> {
			if (!isFocused()) {
				omniBox.hide();
				omniBox.cancelSearch();
			}
		});

		addEventHandler(KeyEvent.KEY_PRESSED, evt -> {
			switch (evt.getCode()) {
			case DOWN:
			case UP:
				jumpToOmniBox();
				break;
			case ESCAPE:
				cancelSearch();
				break;
			default:
				break;
			}
		});
	}

	private void jumpToOmniBox() {
		showOmniBox();
		omniBox.getListView().setFocusTraversable(true);
		omniBox.getListView().requestFocus();
		omniBox.getListView().getSelectionModel().select(0);
	}

	@Override
	public final void showOmniBox() {
		if (isFocused() && !omniBox.isCancelled()) {
			VBox vBox = (VBox) omniBox.getSkin().getNode();
			vBox.applyCss();
			((VBox) omniBox.getSkin().getNode()).requestLayout();
			((VBox) omniBox.getSkin().getNode()).layout();
			Point2D pos = getPrefPopupPosition(vBox);
//			omniBox.show(getScene().getWindow(), pos.getX(), pos.getY());
			omniBox.show(getScene().getWindow(), localToScreen(getBoundsInLocal()).getMinX() + getWidth() - omniBox.getListView().getPrefWidth(), pos.getY());
		}
	}

	private Point2D getPrefPopupPosition(Node content) {
		return pointRelativeTo(this, content, HPos.CENTER, VPos.BOTTOM, 0, 4, true);
	}

	@Override
	public final void hideOmniBox() {
		omniBox.hide();
	}

	/**
	 * The list of {@link OmniBoxService} instances against which the field will
	 * run searches.
	 *
	 * @return the search services
	 * @see OmniBox#getOmniBoxServices()
	 */
	public final ObservableList<OmniBoxService<?>> getOmniBoxServices() {
		return omniBox.getOmniBoxServices();
	}

	/**
	 * A property used to control whether the search services will be called
	 * automatically when the {@link #textProperty()} changes. The default is
	 * "true". If set to "false" the application needs to call
	 * {@link #startSearch(boolean)} explicitly.
	 *
	 * @return the auto search property
	 * @see OmniBox#autoSearchProperty()
	 */
	public final BooleanProperty autoSearchProperty() {
		return omniBox.autoSearchProperty();
	}

	/**
	 * Sets the value of {@link #autoSearchProperty()}.
	 *
	 * @param auto
	 *            if true the services will be called after a delay when the
	 *            {@link #textProperty()} changes
	 * @see OmniBox#setAutoSearch(boolean)
	 */
	public final void setAutoSearch(boolean auto) {
		omniBox.setAutoSearch(auto);
	}

	/**
	 * Returns the value of {@link #autoSearchProperty()}.
	 *
	 * @return true if auto search is enabled, false otherwise
	 * @see OmniBox#isAutoSearch()
	 */
	public final boolean isAutoSearch() {
		return omniBox.isAutoSearch();
	}

	/**
	 * A property used to set the initial delay before running a new search. The
	 * delay is needed so that a new search is not executed after every
	 * character pressed by the user but only when the user stops typing for a
	 * moment.
	 *
	 * @return the initial search delay
	 * @see OmniBox#searchDelayProperty()
	 */
	public final ObjectProperty<Duration> searchDelayProperty() {
		return omniBox.searchDelayProperty();
	}

	/**
	 * Returns the value of {@link #searchDelayProperty()}.
	 *
	 * @return the initial delay before running a new search
	 * @see OmniBox#getSearchDelay()
	 */
	public final Duration getSearchDelay() {
		return omniBox.getSearchDelay();
	}

	/**
	 * Sets the value of {@link #searchDelayProperty()}.
	 *
	 * @param duration
	 *            the initial delay before running a new search
	 * @see OmniBox#setSearchDelay
	 */
	public final void setSearchDelay(Duration duration) {
		omniBox.setSearchDelay(duration);
	}

	/**
	 * Perform a new search but only if the new search text is really different
	 * than the one used for the last search, or if the "force" parameter is
	 * true.
	 *
	 * @param force
	 *            determines if a search is forced to run, even when the search
	 *            text has not changed (e.g. when user hits ENTER).
	 * @see OmniBox#startSearch(boolean)
	 */
	public final void startSearch(boolean force) {
		omniBox.startSearch(force);
	}

	/**
	 * Cancel the currently running search. Calling this method will ask the
	 * {@link OmniBox} to cancel its currently runnning task. It will also ask
	 * each individual service to do so, too.
	 *
	 * @see OmniBox#cancelSearch()
	 */
	public final void cancelSearch() {
		omniBox.cancelSearch();
	}

	// -----------------------------------------------
	// UTILITY METHODS COPIED FROM INTERNAL JAVAFX API
	// -----------------------------------------------

	private static Point2D pointRelativeTo(Node parent, Node node, HPos hpos, VPos vpos, double dx, double dy, boolean reposition) {
		double nodeWidth = node.prefWidth(-1);
		double nodeHeight = node.prefHeight(-1);
		return pointRelativeTo(parent, nodeWidth, nodeHeight, hpos, vpos, dx, dy, reposition);
	}

	private static Point2D pointRelativeTo(Node parent, double anchorWidth, double anchorHeight, HPos hpos, VPos vpos, double dx, double dy, boolean reposition) {
		Bounds parentBounds = getBounds(parent);
		Scene scene = parent.getScene();
		NodeOrientation orientation = parent.getEffectiveNodeOrientation();

		if (orientation == NodeOrientation.RIGHT_TO_LEFT) {
			if (hpos == HPos.LEFT) {
				hpos = HPos.RIGHT;
			} else if (hpos == HPos.RIGHT) {
				hpos = HPos.LEFT;
			}
			dx *= -1;
		}

		double layoutX = positionX(parentBounds, anchorWidth, hpos) + dx;
		double layoutY = positionY(parentBounds, anchorHeight, vpos) + dy;

		if (orientation == NodeOrientation.RIGHT_TO_LEFT && hpos == HPos.CENTER) {
			if (scene.getWindow() instanceof Stage) {
				layoutX = layoutX + parentBounds.getWidth() - anchorWidth;
			} else {
				layoutX = layoutX - parentBounds.getWidth() - anchorWidth;
			}
		}

		if (reposition) {
			return pointRelativeTo(parent, anchorWidth, anchorHeight, layoutX, layoutY, hpos, vpos);
		} else {
			return new Point2D(layoutX, layoutY);
		}
	}

	/**
	 * This is the fallthrough function that most other functions fall into. It
	 * takes care specifically of the repositioning of the item such that it
	 * remains onscreen as best it can, given it's unique qualities.
	 *
	 * As will all other functions, this one returns a Point2D that represents
	 * an x,y location that should safely position the item onscreen as best as
	 * possible.
	 *
	 * Note that <code>width</code> and <height> refer to the width and height
	 * of the node/popup that is needing to be repositioned, not of the parent.
	 *
	 * Don't use the BASELINE vpos, it doesn't make sense and would produce
	 * wrong result.
	 */
	private static Point2D pointRelativeTo(Object parent, double width, double height, double screenX, double screenY, HPos hpos, VPos vpos) {
		double finalScreenX = screenX;
		double finalScreenY = screenY;
		Bounds parentBounds = getBounds(parent);

		// ...and then we get the bounds of this screen
		Screen currentScreen = getScreen(parent);
		Rectangle2D screenBounds = hasFullScreenStage(currentScreen) ? currentScreen.getBounds() : currentScreen.getVisualBounds();

		// test if this layout will force the node to appear outside
		// of the screens bounds. If so, we must reposition the item to a better
		// position.
		// We firstly try to do this intelligently, so as to not overlap the
		// parent if
		// at all possible.
		if (hpos != null) {
			// Firstly we consider going off the right hand side
			if ((finalScreenX + width) > screenBounds.getMaxX()) {
				finalScreenX = positionX(parentBounds, width, getHPosOpposite(hpos, vpos));
			}

			// don't let the node go off to the left of the current screen
			if (finalScreenX < screenBounds.getMinX()) {
				finalScreenX = positionX(parentBounds, width, getHPosOpposite(hpos, vpos));
			}
		}

		if (vpos != null) {
			// don't let the node go off the bottom of the current screen
			if ((finalScreenY + height) > screenBounds.getMaxY()) {
				finalScreenY = positionY(parentBounds, height, getVPosOpposite(hpos, vpos));
			}

			// don't let the node out of the top of the current screen
			if (finalScreenY < screenBounds.getMinY()) {
				finalScreenY = positionY(parentBounds, height, getVPosOpposite(hpos, vpos));
			}
		}

		// --- after all the moving around, we do one last check / rearrange.
		// Unlike the check above, this time we are just fully committed to
		// keeping
		// the item on screen at all costs, regardless of whether or not that
		// results
		/// in overlapping the parent object.
		if ((finalScreenX + width) > screenBounds.getMaxX()) {
			finalScreenX -= (finalScreenX + width - screenBounds.getMaxX());
		}
		if (finalScreenX < screenBounds.getMinX()) {
			finalScreenX = screenBounds.getMinX();
		}
		if ((finalScreenY + height) > screenBounds.getMaxY()) {
			finalScreenY -= (finalScreenY + height - screenBounds.getMaxY());
		}
		if (finalScreenY < screenBounds.getMinY()) {
			finalScreenY = screenBounds.getMinY();
		}

		return new Point2D(finalScreenX, finalScreenY);
	}

	/**
	 * Utility function that returns the x-axis position that an object should
	 * be positioned at, given the parents screen bounds, the width of the
	 * object, and the required HPos.
	 */
	private static double positionX(Bounds parentBounds, double width, HPos hpos) {
		if (hpos == HPos.CENTER) {
			// this isn't right, but it is needed for root menus to show
			// properly
			return parentBounds.getMinX();
		} else if (hpos == HPos.RIGHT) {
			return parentBounds.getMaxX();
		} else if (hpos == HPos.LEFT) {
			return parentBounds.getMinX() - width;
		} else {
			return 0;
		}
	}

	/**
	 * Utility function that returns the y-axis position that an object should
	 * be positioned at, given the parents screen bounds, the height of the
	 * object, and the required VPos.
	 *
	 * The BASELINE vpos doesn't make sense here, 0 is returned for it.
	 */
	private static double positionY(Bounds parentBounds, double height, VPos vpos) {
		if (vpos == VPos.BOTTOM) {
			return parentBounds.getMaxY();
		} else if (vpos == VPos.CENTER) {
			return parentBounds.getMinY();
		} else if (vpos == VPos.TOP) {
			return parentBounds.getMinY() - height;
		} else {
			return 0;
		}
	}

	/**
	 * To facilitate multiple types of parent object, we unfortunately must
	 * allow for Objects to be passed in. This method handles determining the
	 * bounds of the given Object. If the Object type is not supported, a
	 * default Bounds will be returned.
	 */
	private static Bounds getBounds(Object obj) {
		if (obj instanceof Node) {
			Node n = (Node) obj;
			Bounds b = n.localToScreen(n.getLayoutBounds());
			return b != null ? b : new BoundingBox(0, 0, 0, 0);
		} else if (obj instanceof Window) {
			Window window = (Window) obj;
			return new BoundingBox(window.getX(), window.getY(), window.getWidth(), window.getHeight());
		} else {
			return new BoundingBox(0, 0, 0, 0);
		}
	}

	/*
	 * Simple utitilty function to return the 'opposite' value of a given HPos,
	 * taking into account the current VPos value. This is used to try and avoid
	 * overlapping.
	 */
	private static HPos getHPosOpposite(HPos hpos, VPos vpos) {
		if (vpos == VPos.CENTER) {
			if (hpos == HPos.LEFT) {
				return HPos.RIGHT;
			} else if (hpos == HPos.RIGHT) {
				return HPos.LEFT;
			} else if (hpos == HPos.CENTER) {
				return HPos.CENTER;
			} else {
				// by default center for now
				return HPos.CENTER;
			}
		} else {
			return HPos.CENTER;
		}
	}

	/*
	 * Simple utitilty function to return the 'opposite' value of a given VPos,
	 * taking into account the current HPos value. This is used to try and avoid
	 * overlapping.
	 */
	private static VPos getVPosOpposite(HPos hpos, VPos vpos) {
		if (hpos == HPos.CENTER) {
			if (vpos == VPos.BASELINE) {
				return VPos.BASELINE;
			} else if (vpos == VPos.BOTTOM) {
				return VPos.TOP;
			} else if (vpos == VPos.CENTER) {
				return VPos.CENTER;
			} else if (vpos == VPos.TOP) {
				return VPos.BOTTOM;
			} else {
				// by default center for now
				return VPos.CENTER;
			}
		} else {
			return VPos.CENTER;
		}
	}

	private static boolean hasFullScreenStage(Screen screen) {
		List<Window> allWindows = Window.getWindows();

		for (Window window : allWindows) {
			if (window instanceof Stage) {
				Stage stage = (Stage) window;
				if (stage.isFullScreen() && (getScreen(stage) == screen)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * This function attempts to determine the best screen given the parent
	 * object from which we are wanting to position another item relative to.
	 * This is particularly important when we want to keep items from going off
	 * screen, and for handling multiple monitor support.
	 */
	private static Screen getScreen(Object obj) {
		Bounds parentBounds = getBounds(obj);

		Rectangle2D rect = new Rectangle2D(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getWidth(), parentBounds.getHeight());

		return getScreenForRectangle(rect);
	}

	private static Screen getScreenForRectangle(Rectangle2D rect) {
		List<Screen> screens = Screen.getScreens();

		double rectX0 = rect.getMinX();
		double rectX1 = rect.getMaxX();
		double rectY0 = rect.getMinY();
		double rectY1 = rect.getMaxY();

		Screen selectedScreen;

		selectedScreen = null;
		double maxIntersection = 0;
		for (Screen screen : screens) {
			Rectangle2D screenBounds = screen.getBounds();
			double intersection = getIntersectionLength(rectX0, rectX1, screenBounds.getMinX(), screenBounds.getMaxX())
					* getIntersectionLength(rectY0, rectY1, screenBounds.getMinY(), screenBounds.getMaxY());

			if (maxIntersection < intersection) {
				maxIntersection = intersection;
				selectedScreen = screen;
			}
		}

		if (selectedScreen != null) {
			return selectedScreen;
		}

		selectedScreen = Screen.getPrimary();
		double minDistance = Double.MAX_VALUE;
		for (Screen screen : screens) {
			Rectangle2D screenBounds = screen.getBounds();
			double dx = getOuterDistance(rectX0, rectX1, screenBounds.getMinX(), screenBounds.getMaxX());
			double dy = getOuterDistance(rectY0, rectY1, screenBounds.getMinY(), screenBounds.getMaxY());
			double distance = dx * dx + dy * dy;

			if (minDistance > distance) {
				minDistance = distance;
				selectedScreen = screen;
			}
		}

		return selectedScreen;
	}

	private static double getIntersectionLength(double a0, double a1, double b0, double b1) {
		// (a0 <= a1) && (b0 <= b1)
		return (a0 <= b0) ? getIntersectionLengthImpl(b0, b1, a1) : getIntersectionLengthImpl(a0, a1, b1);
	}

	private static double getIntersectionLengthImpl(double v0, double v1, double v) {
		// (v0 <= v1)
		if (v <= v0) {
			return 0;
		}

		return (v <= v1) ? v - v0 : v1 - v0;
	}

	private static double getOuterDistance(double a0, double a1, double b0, double b1) {
		// (a0 <= a1) && (b0 <= b1)
		if (a1 <= b0) {
			return b0 - a1;
		}

		if (b1 <= a0) {
			return b1 - a0;
		}

		return 0;
	}
}