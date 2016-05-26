package application;

import javafx.geometry.Point3D;

/**
 * @author GladeJoa
 */
public class Star extends OrbitalElement  {

	public Star(String name, ElementSize size) {
		super(name, size);
	}

	@Override
	public void calculatePostion(double d) {
		// === Star is always in center (simplification) ===
		position = new Point3D(0, 0, 0);
	}
}