package ca.utoronto.ece1778.baton.util;

import android.content.Context;
import android.content.Intent;

public class CommonUtilities {
	// give your server registration url here
	public static final String SERVER_URL = "http://54.213.105.123:8080/BatonSyncServer";

	// Google project id
	public static final String SENDER_ID = "553157495789";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "CommonUtilities";

	public static final String DISPLAY_MESSAGE_ACTION = "/ca.utoronto.ece1778.baton.DISPLAY_MESSAGE";

	public static final String EXTRA_MESSAGE = "TalkTicket";

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
	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}

	/**
	 * MD5
	 * 
	 * @param str
	 * @return MD5 of str
	 */
	public static String getMD5Str(String str) {
		//TODO
		return str;
	}

}
