package ca.utoronto.ece1778.baton.util;

import android.content.Context;
import android.content.Intent;

/**@deprecated
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

}
