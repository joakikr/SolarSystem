package application;

/**
 * @author GladeJoa
 */
public interface Commons {
	public final int APP_WIDTH = 800;
	public final int APP_HEIGHT = 800;
	
	public final String ICON_PATH = "/elements/";
	public final String ICON_SUFFIX = ".png";
	public final String ORBITAL_ELEMENTS_PATH = "/data/orbital_elements.txt";
	public final int CANVAS_UPDATE = 60; // How often to redraw canvas
	public final String GUI_PATH = "GUI.fxml";
	public final String CSS_PATH = "application.css";

	public final double PLANET_SPACING = 35;
	public final double STAR_SPACING = 80;
}
