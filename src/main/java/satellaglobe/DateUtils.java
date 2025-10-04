package satellaglobe;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for date formatting.
 */
public class DateUtils {
	/**
	 * Convert a Date object to an ISO 8601 basic formatted string (yyyyMMdd'T'HHmmss'Z').
	 * @param date The Date object to convert
	 * @return The ISO 8601 formatted date string
	 */
	public static String toIsoDateString(Date date) {
		return (new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")).format(date);
	}
}
