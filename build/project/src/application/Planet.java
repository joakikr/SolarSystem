package application;

import javafx.geometry.Point3D;

/**
 * @author GladeJoa
 */
public class Planet extends OrbitalElement {

	public Planet(String name, ElementSize size, double NIn, double iIn, double wIn, double aIn, double eIn, double M0In, double nIn) {
		super(name, size, NIn, iIn, wIn, aIn, eIn, M0In, nIn);

		// === Justify N, w, i (0 <= val <= 2pi) ===
		N = Math.toRadians(Utils.rev(N));
		w = Math.toRadians(Utils.rev(w));
		i = Math.toRadians(Utils.rev(i));
	}
	
	@Override
	public void calculatePostion(double d) {
		double M = M0 + d*n;
		
		// === Justify M (0 <= val <= 2pi) ===
		M = Math.toRadians(Utils.rev(M));
		
		// === Eccentric Anomaly ===
		E = M + e * Math.sin(M) * (1.0 + e * Math.cos(M));

		if(e > 0.05) {
			int cnt = 25;
			double E0 = E;
			while(cnt-- > 0 && Math.abs(E0 - E) > Math.toRadians(0.001)) {
				E = E0 - (E0 - e * Math.sin(E0) - M) / (1 - e * Math.cos(E0));
				E0 = E;
			}
		}
		
		// === Distance r and true anomaly v ===
		double xv = a * (Math.cos(E) - e);
		double yv = a * (Math.sqrt(1.0 - e*e) * Math.sin(E));

		double r = Math.sqrt(xv*xv + yv*yv);
		v = Math.atan2(yv, xv);

		// === Position in space (heliocentric) ===
		double xh = r * (Math.cos(N) * Math.cos(v+w) - Math.sin(N) * Math.sin(v+w) * Math.cos(i));
		double yh = r * (Math.sin(N) * Math.cos(v+w) + Math.cos(N) * Math.sin(v+w) * Math.cos(i));
		double zh = r * (Math.sin(v+w) * Math.sin(i));
		position = new Point3D(xh, yh, zh);
	}
}