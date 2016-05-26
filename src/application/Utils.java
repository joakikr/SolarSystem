package application;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class Utils {

	/**
	 * The time scale in these formulae are counted in days. 
	 * Hours, minutes, seconds are expressed as fractions of a day. 
	 * 
	 * Day 0.0 occurs at 2000 Jan 0.0 UT (or 1999 Dec 31, 0:00 UT). 
	 * This "day number" d is computed as follows (y=year, m=month, D=date, UT=UT in hours+decimals):
	 * 
	 * 		d = 367*y - 7 * ( y + (m+9)/12 ) / 4 + 275*m/9 + D - 730530 (this is integer divison)
	 * 
	 * Finally, include the time of the day, by adding:
	 * 
	 * 		d = d + UT/24.0        (this is a floating-point division)
	 * 
	 */
	public static double dayNumber(Date current) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH) + 1;
		int D = cal.get(Calendar.DATE);
		double UT = cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) / 60.0;
		
		double d = 367*y - 7 * (y + (m+9)/12) / 4 + 275*m/9 + D - 730530;
		d = d + UT/24.0;

		return d;
	}
	
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
