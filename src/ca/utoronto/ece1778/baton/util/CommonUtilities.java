package ca.utoronto.ece1778.baton.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ca.utoronto.ece1778.baton.models.StudentProfile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 *
 */
public class CommonUtilities {
	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "CommonUtilities";

	//public static final String DISPLAY_MESSAGE_ACTION = "/ca.utoronto.ece1778.baton.DISPLAY_MESSAGE";

	//public static final String EXTRA_MESSAGE = "TalkTicket";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */
	/*public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}*/

	/**
	 * MD5
	 * 
	 * @param str
	 * @return MD5 of str
	 */
	public static String getMD5Str(String str) {
		//TODO password MD5
		return str;
	}
	
	public static String getGlobalVar(Activity context,String key)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		return global.get(key);
	}
	
	public static void putGlobalVar(Activity context,String key, String value)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		global.put(key, value);
	}
	
	public static String getStrTimeFromMillis(long date)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		SimpleDateFormat myFormat = new SimpleDateFormat(Constants.DATE_FORMAT_LONG); 
		return myFormat.format(c.getTime());
	}
}
