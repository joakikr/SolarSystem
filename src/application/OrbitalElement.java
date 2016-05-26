package application;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;

/**
 * @author GladeJoa
 */
public abstract class OrbitalElement implements Coordinate, Commons {
	
	/**
	 * Distinguish orbital elements in four sizes.
	 * VERY_LARGE: Example, Sun
	 * LARGE: Example, Jupiter/Saturn
	 * MEDIUM: Example, Neptune/Uranus
	 * SMALL: Example, Earth/Mars/Mercury
	 */
	enum ElementSize {
		SMALL(16), MEDIUM(32), LARGE(64), VERY_LARGE(128);
		
		final int value;
		private ElementSize(int value) {
			this.value = value;
		}
	}
	
	/**
	 * Name of object
	 */
	protected String name;
	
	/**
	 * Indicator of size
	 */
	protected ElementSize size;
	
	/**
	 * Icon related variables
	 */
	protected Image icon;
	protected double iconSize;
	
	/**
	 * Coordinates (Heliocentric)
	 */
	public Point3D position;
	
	/**
     * N = longitude of the ascending node (large omega)
     * i = inclination to the ecliptic (plane of the Earth's orbit)
     * w = argument of perihelion (small omega)
     * a = semi-major axis, or mean distance from Sun
     * e = eccentricity (0=circle, 0-1=ellipse, 1=parabola)
     * M = mean anomaly (0 at perihelion; increases uniformly with time)
	 * v = true anomaly (angle between position and perihelion)
	 * E = eccentric anomaly
	 * n = angle the planet traverses on average per day, as seen from the Sun
	 */
	protected double N;
	protected double i;
	protected double w;
	protected double a;
	protected double e;
	protected double M0;
	protected double v;
	protected double E;
	protected double n;
	
	public OrbitalElement(String nameIn, ElementSize sizeIn) {
		this.name = nameIn;
		this.size = sizeIn;
		
		// === Icon ===
		iconSize = size.value;
		
		// === Create Element Icon ===
		String path = ICON_PATH + name.toLowerCase() + ICON_SUFFIX;
		icon = new Image(getClass().getResource(path).toExternalForm(), iconSize, iconSize, true, true);
	}
	
	public OrbitalElement(String name, ElementSize size, double NIn, double iIn, double wIn, double aIn, double eIn, double M0In, double nIn) {
		this(name, size);
		this.N = NIn;
		this.i = iIn;
		this.w = wIn;
		this.a = aIn;
		this.e = eIn;
		this.M0 = M0In;
		this.n = nIn;
	}
	
	public Point3D getCoordinate() {
		return position;
	}
	
	public abstract void calculatePostion(double d);
}