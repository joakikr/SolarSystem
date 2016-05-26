package application;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class Utils {

	/**
	 * Find the number of days between two dates.
	 */
	public static int daysBetweenDates(DateTime old, DateTime now) {
		return Days.daysBetween(old, now).getDays();
	}
	
	/**
	 * Justify an angle in degrees such that:
	 * 		
	 * 		0 <= angle < 360 
	 */
	public static double rev(double x) {
		return x - Math.floor(x/360.0)*360.0;
	}
}